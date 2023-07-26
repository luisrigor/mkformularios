package com.gsc.mkformularios.service.impl;

import com.gsc.mkformularios.config.datasource.toyota.DbClient;
import com.gsc.mkformularios.config.datasource.toyota.DbContext;
import com.gsc.mkformularios.dto.PVMDetailDTO;
import com.gsc.mkformularios.dto.PVMRequestDTO;
import com.gsc.mkformularios.dto.SalesPlates;
import com.gsc.mkformularios.model.toyota.entity.PVMCarmodel;
import com.gsc.mkformularios.model.toyota.entity.PVMMonthlyReport;
import com.gsc.mkformularios.repository.toyota.PVMCarmodelRepository;
import com.gsc.mkformularios.repository.toyota.PVMMonthlyReportRepository;
import com.gsc.mkformularios.security.UserPrincipal;
import com.gsc.mkformularios.service.PVMService;
import com.gsc.mkformularios.service.impl.pvm.PVM1MonthReport;
import com.gsc.mkformularios.service.impl.pvm.PVM3MonthReport;
import com.rg.dealer.Dealer;
import com.sc.commons.exceptions.SCErrorException;
import com.sc.commons.utils.DateTimerTasks;
import com.sc.commons.utils.StringTasks;
import lombok.RequiredArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PVMServiceImpl implements PVMService {


    private final DbContext dbContext;
    private final PVMCarmodelRepository pvmCarmodelRepository;
    private final PVMMonthlyReportRepository pvmMonthlyReportRepository;
    private final PVM3MonthReport pvm3MonthReport;
    private final PVM1MonthReport pvm1MonthReport;

    public void setDataSourceContext(Long client){
        if (client == 2L)
            dbContext.setBranchContext(DbClient.DB_LEXUS);
    }

    @Override
    public PVMDetailDTO getPVM(PVMRequestDTO pvmRequestDTO, UserPrincipal userPrincipal) {
       return null;

    }

    @Override
    public PVMDetailDTO getPVMDetail(int idPVM, UserPrincipal userPrincipal) {
        this.setDataSourceContext(userPrincipal.getClientId());
        List<PVMCarmodel> car = pvmCarmodelRepository.getCar();
        Optional<PVMMonthlyReport> monthlyReport = pvmMonthlyReportRepository.findById(idPVM);
        List<SalesPlates> salesAndPlates = pvmMonthlyReportRepository.getSalesAndPlates(idPVM);

        return PVMDetailDTO.builder()
                .car(car)
                .monthlyReport(monthlyReport.orElse(null))
                .salesAndPlates(salesAndPlates)
                .build();

    }

    @Override
    public void getPVMExcelByMonth(PVMRequestDTO pvmRequestDTO, String pvmMonth, UserPrincipal userPrincipal, HttpServletResponse response) {
        //TODO remove this user init
        userPrincipal = new UserPrincipal(null, null, null);
        userPrincipal.setCaMember(false);
        int year	= pvmRequestDTO.getYear();
        int month	= pvmRequestDTO.getMonth();
        String oidNet = StringTasks.cleanString(pvmRequestDTO.getOidNet(), "");
        if (year==0 || month==0)
            return;

        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String filename = "Previsão_de_Vendas_e_Matrículas_(" +DateTimerTasks.ptMonths[month-1]+ ").xls";
        String headerValue = "attachment; filename="+filename;
        response.setHeader(headerKey, headerValue);

        try {
            HSSFWorkbook oHSSFWorkbook = null;

            if ("1MONTH".equals(pvmMonth)) {
                if (oidNet.equals(Dealer.OID_NET_TOYOTA))	{
                    oHSSFWorkbook = pvm1MonthReport.execute(year, month, "Toyota", userPrincipal.isCAMember());
                } else if (oidNet.equals(Dealer.OID_NET_LEXUS))	{
                    oHSSFWorkbook = pvm1MonthReport.execute(year, month, "Lexus", userPrincipal.isCAMember());
                }
            } else if ("3MONTH".equals(pvmMonth)) {
                if (oidNet.equals(Dealer.OID_NET_TOYOTA))	{
                    oHSSFWorkbook = pvm3MonthReport.executeForToyota(year, month, userPrincipal.isCAMember());
                } else if (oidNet.equals(Dealer.OID_NET_LEXUS))	{
                    oHSSFWorkbook = pvm3MonthReport.executeForLexus(year, month, userPrincipal.isCAMember());
                }
            }

            ServletOutputStream outputStream = response.getOutputStream();
            oHSSFWorkbook.write(outputStream);
            oHSSFWorkbook.close();
            outputStream.close();

        } catch (Exception e) {
            throw new RuntimeException("Error generating file", e);
        }
    }
}
