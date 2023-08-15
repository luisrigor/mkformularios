package com.gsc.mkformularios.service.impl;

import com.gsc.mkformularios.config.datasource.toyota.DbClient;
import com.gsc.mkformularios.config.datasource.toyota.DbContext;
import com.gsc.mkformularios.constants.PVMConstants;
import com.gsc.mkformularios.dto.*;
import com.gsc.mkformularios.exceptions.CreatePVMException;
import com.gsc.mkformularios.exceptions.GetPVMException;
import com.gsc.mkformularios.model.toyota.entity.PVMBudget;
import com.gsc.mkformularios.repository.toyota.PVMBudgetRepository;
import com.gsc.mkformularios.repository.toyota.PVMCarmodelRepository;
import com.gsc.mkformularios.security.UserPrincipal;
import com.gsc.mkformularios.service.BudgetService;
import com.gsc.mkformularios.utils.DealersUtils;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import com.rg.dealer.Dealer;
import com.sc.commons.exceptions.SCErrorException;
import com.sc.commons.utils.PortletMultipartWrapper;
import com.sc.commons.utils.PortletTasks;
import com.sc.commons.utils.StringTasks;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import static com.gsc.mkformularios.constants.DATAConstants.APP_LEXUS;
import static com.gsc.mkformularios.constants.DATAConstants.APP_TOYOTA;
import static com.gsc.mkformularios.utils.DealersUtils.convertHashDealers;

@Service
@Log4j
@RequiredArgsConstructor
public class BudgetServiceImpl implements BudgetService {

    private final DbContext dbContext;
    private final PVMBudgetRepository pvmBudgetRepository;
    private final DealersUtils dealersUtils;
    private final PVMCarmodelRepository pvmCarmodelRepository;
    public final static String HEADER_FILE_DEALER_CODE = "Cod. Concessão";
    public final static String HEADER_FILE_MONTH = "Mês";



    public void setDataSourceContext(Long client) {
        if (client == APP_LEXUS) {
            dbContext.setBranchContext(DbClient.DB_LEXUS);
        } else if (client == APP_TOYOTA) {
            dbContext.setBranchContext(DbClient.DB_TOYOTA);
        }
    }

    @Override
    public BudgetDTO editBudget(String yearSelect, UserPrincipal userPrincipal) {
        this.setDataSourceContext(userPrincipal.getClientId());

        List<PVMBudgetDTO> vecBudget;
        int year = StringTasks.cleanInteger(yearSelect, Calendar.getInstance().get(Calendar.YEAR));

        try{
            vecBudget = pvmBudgetRepository.getPVMBudgetByYear(year);

            List<String[]> vecDealers = this.getDealers(userPrincipal);

            List<MapTypesDTO> carTypes = pvmCarmodelRepository.getCarTypes();

            List<BudgetDealerDTO> budgetDTO = this.mapBudgetDealer(vecBudget, vecDealers);

            return BudgetDTO.builder()
                    .budgetDealers(budgetDTO)
                    .year(year)
                    .carTypes(carTypes)
                    .build();

        } catch(Exception e){
            log.error("Error fetching budget ", e);
            throw new GetPVMException("Error fetching budget ", e);
        }
    }

    @Override
    @Transactional
    public void saveBudget(String yearSelect, UserPrincipal userPrincipal, List<PVMBudget> lstPVMBudget) {
        this.setDataSourceContext(userPrincipal.getClientId());
        ArrayList<String> deletedDealersBudgets = new ArrayList<String>();
        try {
            for (PVMBudget oBudget: lstPVMBudget) {
                if(!deletedDealersBudgets.contains(oBudget.getOidDealer()+oBudget.getSubDealer())){
                    pvmBudgetRepository.deleteBudgetsByDealerAndYear(oBudget.getOidDealer(), oBudget.getYear(), oBudget.getSubDealer());
                    deletedDealersBudgets.add(oBudget.getOidDealer()+oBudget.getSubDealer());
                }
                pvmBudgetRepository.save(oBudget);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error saving budget", e);
            throw new CreatePVMException("Error saving budget", e);
        }
    }

    @Override
    public List<String[]> downloadBudget(String yearBudget, UserPrincipal userPrincipal, HttpServletResponse response) {
        this.setDataSourceContext(userPrincipal.getClientId());
        int year = StringTasks.cleanInteger(yearBudget,Calendar.getInstance().get(Calendar.YEAR));

        List<MapTypesDTO> mapTypes = pvmCarmodelRepository.getCarTypes();

        String[] headers = getHeader(mapTypes);
        String displayFileName = "Orcamento_"+year+".csv";
        response.setContentType("text/csv;charset="+StandardCharsets.UTF_8);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + displayFileName + "\"");
        try {

            List<String[]> vecDealers = this.getDealers(userPrincipal);

            List<PVMBudgetDTO> vecBudget = pvmBudgetRepository.getPVMBudgetByYear(year);
            List<String[]> rowsToWrite = new ArrayList<>();
            rowsToWrite.add(headers);

            for(int month = 1; month<13; month++) {
                for (String[] oDealer: vecDealers) {
                    StringBuilder row = new StringBuilder();
                    row.append("="+oDealer[2]).append(";");
//                    row.append("=\""+oDealer[2]+"\"").append(";");
                    row.append(month).append(";");
                    for(MapTypesDTO typePair: mapTypes){
                        boolean foundBudget = false;
                        for (PVMBudgetDTO oBudget: vecBudget) {
                            if((oBudget.getOidDealer().equalsIgnoreCase(oDealer[0])) && (oBudget.getMonth() == month)
                                    && (oBudget.getIdPvmCarModel() == typePair.getId())
                                    && (String.valueOf(oBudget.getSubDealer()).equalsIgnoreCase(oDealer[3]))){
                                row.append(oBudget.getPlates()).append(";");
                                foundBudget = true;
                            }
                        }
                        if(!foundBudget) {
                            row.append(0).append(";");
                        }
                    }
                    String rowToWrite = row.toString();
                    rowToWrite = rowToWrite.substring(0, rowToWrite.length()-1);
                    rowsToWrite.add(rowToWrite.split(";"));
                }
            }
            return rowsToWrite;
        } catch (Exception e) {
            e.printStackTrace();
            throw new GetPVMException("Error generating Sales Forecast template", e);
        }
    }

    @Override
    @Transactional
    public void uploadBudget(String yearBudget, UserPrincipal userPrincipal, MultipartFile file) {
        this.setDataSourceContext(userPrincipal.getClientId());

        List<PVMBudget> lstPVMBudget = new ArrayList<>();
        int year = StringTasks.cleanInteger(yearBudget,0);


        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.ISO_8859_1))) {
            CSVParser csvParser = new CSVParserBuilder().withSeparator(';').build();
            CSVReader csvReader = new CSVReaderBuilder(reader).withCSVParser(csvParser).build();
            List<String[]> rows = csvReader.readAll();

            List<MapTypesDTO> mapCarCategoryTypes = pvmCarmodelRepository.getCarTypes();
            Hashtable<String, Dealer> allDealersByOid = dealersUtils.getAllActiveMainDealers(userPrincipal.getOidNet());
            Map<String, Dealer> allDealersByDlrCode = convertHashDealers(allDealersByOid);



            for (String[] currentRow: rows.subList(1, rows.size())) {
                Map<String, String> csvMapRecord = this.rowToMap(currentRow, rows.get(0));
                String dealerCode = csvMapRecord.get(HEADER_FILE_DEALER_CODE);
                if(dealerCode.startsWith("="))
                    dealerCode = dealerCode.substring(1);
                String oidDealer = !allDealersByDlrCode.containsKey(dealerCode) ? "" : allDealersByDlrCode.get(dealerCode).getObjectId();
                if (oidDealer.equals("")) {
                    if (dealerCode.equals(PVMConstants.DLR_CODE_FROTAS_SUL) || dealerCode.equals(PVMConstants.DLR_CODE_IMOBILIZADO) || dealerCode.equals(PVMConstants.DLR_CODE_FROTAS_GAIA) || dealerCode.equals(PVMConstants.DLR_CODE_EXPORTACAO))
                        oidDealer = Dealer.OID_NMSC;
                }
                if (oidDealer.equals(""))
                    continue;

                int subDealer = this.getSubDealerCodeStat(dealerCode);
                String month = csvMapRecord.get(HEADER_FILE_MONTH);
                for (MapTypesDTO carCategoryType: mapCarCategoryTypes) {
                    PVMBudget oBudget = new PVMBudget();
                    oBudget.setOidDealer(oidDealer);
                    oBudget.setSubDealer(subDealer);
                    oBudget.setMonth(Integer.parseInt(month));
                    oBudget.setYear(year);
                    oBudget.setIdPvmCarModel(carCategoryType.getId());
                    if(csvMapRecord.containsKey(carCategoryType.getType()))
                        oBudget.setPlates(Integer.parseInt(csvMapRecord.get(carCategoryType.getType())));
                    lstPVMBudget.add(oBudget);
                }
            }
            this.saveBudget(yearBudget, userPrincipal, lstPVMBudget);
        } catch (Exception ex) {
            log.error("Error loading budget", ex);
            throw new CreatePVMException("Error loading budget",ex);
        }
    }


    private List<String[]> getDealers(UserPrincipal userPrincipal) throws SCErrorException {
        List<String[]> vecDealers = dealersUtils.getRetailerDealers(userPrincipal.getOidNet());
        vecDealers.add(new String[]{Dealer.OID_NMSC, PVMConstants.DLR_CODE_FROTAS_SUL 	+ "-[TCAP Frotas Sul]",			 	PVMConstants.DLR_CODE_FROTAS_SUL,    String.valueOf(PVMConstants.SUB_DLR_CODE_FROTAS_SUL)});
        vecDealers.add(new String[]{Dealer.OID_NMSC, PVMConstants.DLR_CODE_IMOBILIZADO 	+ "-[TCAP Imobilizado (Gaia)]", 	PVMConstants.DLR_CODE_IMOBILIZADO,   String.valueOf(PVMConstants.SUB_DLR_CODE_IMOBILIZADO)});
        vecDealers.add(new String[]{Dealer.OID_NMSC, PVMConstants.DLR_CODE_FROTAS_GAIA	+ "-[TCAP Frotas Gaia]",			PVMConstants.DLR_CODE_FROTAS_GAIA,	String.valueOf(PVMConstants.SUB_DLR_CODE_FROTAS_GAIA)});
        vecDealers.add(new String[]{Dealer.OID_NMSC, PVMConstants.DLR_CODE_EXPORTACAO	+ "-[TCAP Exportação (Ovar)]",		PVMConstants.DLR_CODE_EXPORTACAO,	String.valueOf(PVMConstants.SUB_DLR_CODE_EXPORTACAO)});
        return vecDealers;
    }


    private List<BudgetDealerDTO> mapBudgetDealer(List<PVMBudgetDTO> vecBudget, List<String[]> vecDealers) {
        List<BudgetDealerDTO> budgetDealers = new ArrayList<>();
        for (String[]  currentDealer: vecDealers) {
            List<PVMBudgetDTO> currentBudgets = vecBudget.stream()
                    .filter(pvmBudget -> currentDealer[0].equals(pvmBudget.getOidDealer()))
                    .collect(Collectors.toList());

            budgetDealers.add(new BudgetDealerDTO(currentDealer, currentBudgets));
        }
        return budgetDealers;
    }

    public static String[] getHeader(List<MapTypesDTO> mapTypes) {
        String headerString = HEADER_FILE_DEALER_CODE + "," + HEADER_FILE_MONTH + ",";
        for (MapTypesDTO mapTypesDTO: mapTypes) {
            headerString += mapTypesDTO.getType()+ ",";
        }
        headerString = headerString.substring(0,headerString.length()-1);
        String[] header = headerString.split(",");
        return header;
    }

    private int getSubDealerCodeStat(String dealerCode){
        return dealerCode.equals(PVMConstants.DLR_CODE_FROTAS_SUL) ?
                PVMConstants.SUB_DLR_CODE_FROTAS_SUL :
                dealerCode.equals(PVMConstants.DLR_CODE_IMOBILIZADO)
                        ? PVMConstants.SUB_DLR_CODE_IMOBILIZADO : dealerCode.equals(PVMConstants.DLR_CODE_FROTAS_GAIA)
                        ? PVMConstants.SUB_DLR_CODE_FROTAS_GAIA : dealerCode.equals(PVMConstants.DLR_CODE_EXPORTACAO)
                        ? PVMConstants.SUB_DLR_CODE_EXPORTACAO : 0;
    }

    private Map<String, String> rowToMap(String[] row, String[] headers) {
        Map<String, String> csvMap = new HashMap<>();
        for(int i=0;i<row.length;i++) {
            csvMap.put(headers[i], row[i]);
        }

        return  csvMap;
    }

}
