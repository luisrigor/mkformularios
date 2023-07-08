package com.gsc.mkformularios.service.impl;


import com.gsc.mkformularios.config.datasource.toyota.DbClient;
import com.gsc.mkformularios.config.datasource.toyota.DbContext;
import com.gsc.mkformularios.dto.PlanDTO;
import com.gsc.mkformularios.exceptions.FileUploadException;
import com.gsc.mkformularios.exceptions.GetPVMException;
import com.gsc.mkformularios.model.toyota.entity.PVMCarModelYearForecast;
import com.gsc.mkformularios.model.toyota.entity.PVMCarmodel;
import com.gsc.mkformularios.repository.toyota.PVMCarModelYearForecastRepository;
import com.gsc.mkformularios.repository.toyota.PVMCarmodelRepository;
import com.gsc.mkformularios.security.UserPrincipal;
import com.gsc.mkformularios.service.PlanService;
import com.opencsv.CSVReader;
import com.rg.dealer.Dealer;
import com.sc.commons.utils.StringTasks;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.Timestamp;
import java.util.*;

import static com.gsc.mkformularios.constants.DATAConstants.APP_LEXUS;
import static com.gsc.mkformularios.constants.DATAConstants.APP_TOYOTA;

@RequiredArgsConstructor
@Service
@Log4j
public class PlanServiceImpl implements PlanService {

    private final DbContext dbContext;
    private final PVMCarmodelRepository pvmCarmodelRepository;
    private final PVMCarModelYearForecastRepository pvmCarmodelYearForecastRepository;

    public void setDataSourceContext(Long client) {
        if (client == APP_LEXUS) {
            dbContext.setBranchContext(DbClient.DB_LEXUS);
        } else if (client == APP_TOYOTA) {
            dbContext.setBranchContext(DbClient.DB_TOYOTA);
        }
    }

    @Override
    public PlanDTO goToEditPlan(UserPrincipal userPrincipal, String yearSelect) {
        this.setDataSourceContext(userPrincipal.getClientId());
        int year = StringTasks.cleanInteger(yearSelect, Calendar.getInstance().get(Calendar.YEAR));
        try {
            List<PVMCarmodel> car = pvmCarmodelRepository.getCar();
            List<PVMCarModelYearForecast> aLForecasts = null;
            if (String.valueOf(userPrincipal.getOidNet()).equals(Dealer.OID_NET_TOYOTA)) {
                aLForecasts = pvmCarmodelYearForecastRepository.findAllByYear(year);
            }
            return PlanDTO.builder().car(car).forecast(aLForecasts).build();
        } catch (Exception e) {
            log.error("goToEditPlan,Erro ao consultar aplica��o PVM" + e.getMessage());
            throw new RuntimeException("Erro ao consultar aplica��o PVM");
        }
    }

    @Transactional
    public List<String> uploadPlan(MultipartFile file, String yearPlanUpload, UserPrincipal userPrincipal) {
        List<String> response = new ArrayList<>();
        response.add( "uploaded");
        if(file.isEmpty())
            throw new FileUploadException("File not found");

        int year =  StringTasks.cleanInteger(yearPlanUpload,0);

        try(Reader reader = new BufferedReader(new InputStreamReader((file.getInputStream())))) {
            CSVReader csvReader = new CSVReader(reader);
            List<String[]> rows = csvReader.readAll();
            Map<String, List<PVMCarModelYearForecast>> result = new HashMap<>();
            for (String[] currentRow : rows.subList(1, rows.size())) {
                List<PVMCarModelYearForecast> pvmCarModelYearForecasts = this.formatRowToObject(currentRow, year);
                result.put(currentRow[1], pvmCarModelYearForecasts);
            }
            List<String> savedResponse = this.save(result, userPrincipal);
            if(!savedResponse.isEmpty())
                response = savedResponse;
            return response;
        } catch (Exception e) {
            throw new FileUploadException("There was an error uploading the file", e);
        }
    }

    @Override
    public List<String[]> downloadPlan(String yearPlan, UserPrincipal userPrincipal, HttpServletResponse response) {
        this.setDataSourceContext(userPrincipal.getClientId());
        int year = StringTasks.cleanInteger(yearPlan, Calendar.getInstance().get(Calendar.YEAR));

        String [] header = {"idModelo","Modelo","Janeiro","Fevereiro","Março","Abril","Maio","Junho","Julho","Agosto","Setembro","Outubro","Novembro","Dezembro"};
        String displayFileName = "PlanForm01_"+year+".csv";
        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + displayFileName + "\"");
        try {
            List<String[]> rowsToWrite = new ArrayList<>();

            List<PVMCarmodel> vecCar = pvmCarmodelRepository.getCar();
            List<PVMCarModelYearForecast> aLForecasts = new ArrayList<PVMCarModelYearForecast>();
            if(userPrincipal.getOidNet().equals(Dealer.OID_NET_TOYOTA)){
                aLForecasts = pvmCarmodelYearForecastRepository.findAllByYear(year);
            }
            rowsToWrite.add(header);
            for (PVMCarmodel oPVMCarModel: vecCar) {
                StringBuilder row = new StringBuilder();
                row.append(oPVMCarModel.getId()).append(";");
                row.append(oPVMCarModel.getName()).append(";");

                for(int i = 1 ; i <= 12; i++){
                    boolean foundMonth = false;
                    for (PVMCarModelYearForecast forecast : aLForecasts) {
                        if((forecast.getIdCarModel() == oPVMCarModel.getId()) && (forecast.getMonth() == i)){
                            foundMonth = true;
                            row.append(forecast.getForecast()).append(";");
                        }
                    }
                    if(!foundMonth)
                        row.append(0).append(";");
                }
                String rowToWrite = row.toString();
                rowToWrite = rowToWrite.substring(0, rowToWrite.length()-1);
                rowsToWrite.add(rowToWrite.split(";"));
            }
            return rowsToWrite;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Erro ao gerar template do Plano Form 01", e);
            throw new GetPVMException("Erro ao gerar template do Plano Form 01", e);
        }
    }

    @Transactional
    public List<String> save(Map<String, List<PVMCarModelYearForecast>> result, UserPrincipal userPrincipal) {
        List<String> response = new ArrayList<>();
        this.setDataSourceContext(userPrincipal.getClientId());
        Set<String> keySet = result.keySet();
        String userStamp = userPrincipal.getUsername().split("\\|\\|")[0]+"||"+userPrincipal.getUsername().split("\\|\\|")[1];

        for (String key: keySet) {
            log.info("saving plan for " + key);
            List<PVMCarModelYearForecast> currentForecastList = result.get(key);
            boolean deleteFlag = true;
            for (PVMCarModelYearForecast currentForecast: currentForecastList) {
                try {
                    if(deleteFlag) {
                        deleteFlag = false;
                        pvmCarmodelYearForecastRepository.deleteCarModelByIdAndYear(currentForecast.getIdCarModel(), currentForecast.getYear());
                    }
                    Timestamp ts = new Timestamp(System.currentTimeMillis());
                    currentForecast.setCreatedBy(userStamp);
                    currentForecast.setDtCreated(ts.toLocalDateTime());
                    pvmCarmodelYearForecastRepository.save(currentForecast);
                } catch (Exception e) {
                    response.add("Error saving " +key+ " for month " +currentForecast.getMonth());
                    e.printStackTrace();
                    log.error("Error saving Plano Form 01 for key " + key, e);
                }
            }
        }
        return response;
    }

    private List<PVMCarModelYearForecast> formatRowToObject(String[] row, int year) {
        List<PVMCarModelYearForecast> pvmCarModelYearForecasts = new ArrayList<>();

        for (int i = 1; i<13; i++){
            PVMCarModelYearForecast currentForecast = PVMCarModelYearForecast
                    .builder()
                    .idCarModel(StringTasks.cleanInteger(row[0],0))
                    .year(year)
                    .month(i)
                    .forecast(StringTasks.cleanInteger(row[i+1],0))
                    .build();
            pvmCarModelYearForecasts.add(currentForecast);
        }
        return pvmCarModelYearForecasts;
    }


}
