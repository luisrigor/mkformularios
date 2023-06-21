package com.gsc.mkformularios.service.impl;

import com.gsc.mkformularios.config.datasource.DbClient;
import com.gsc.mkformularios.config.datasource.DbContext;
import com.gsc.mkformularios.dto.PVMDetailDTO;
import com.gsc.mkformularios.dto.SalesPlates;
import com.gsc.mkformularios.model.entity.PVMCarmodel;
import com.gsc.mkformularios.model.entity.PVMMonthlyReport;
import com.gsc.mkformularios.repository.PVMCarmodelRepository;
import com.gsc.mkformularios.repository.PVMMonthlyReportDetailRepository;
import com.gsc.mkformularios.repository.PVMMonthlyReportRepository;
import com.gsc.mkformularios.security.UserPrincipal;
import com.gsc.mkformularios.service.PVMService;
import com.sc.commons.exceptions.SCErrorException;
import com.sc.commons.user.GSCUser;
import com.sc.commons.utils.DateTimerTasks;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Log4j
public class PVMServiceImpl implements PVMService {


    private final DbContext dbContext;
    private final PVMCarmodelRepository pvmCarmodelRepository;
    private final PVMMonthlyReportRepository pvmMonthlyReportRepository;
    private final PVMMonthlyReportDetailRepository pvmMonthlyReportDetailRepository;

    public void setDataSourceContext(Long client) {
        if (client == 2L)
            dbContext.setBranchContext(DbClient.DB_LEXUS);
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
    public Boolean newPVM(GSCUser oGSCUser, int subDealer) {
        boolean success = true;
        try{
            Optional<PVMMonthlyReport> data = pvmMonthlyReportRepository.newPVM(DateTimerTasks.getCurYear(), DateTimerTasks.getCurMonth(), oGSCUser.getOidDealerParent(), subDealer);
            if(data.isPresent()){
                log.info("PVM j� criado para o m�s actual");
                success = false;
            }else{
                PVMMonthlyReport oPVMMonthlyReport = insertMonthlyReport(oGSCUser,subDealer);
                pvmMonthlyReportDetailRepository.mergePVMMonthlyReportDetail(oPVMMonthlyReport.getId());
                success = true;
            }
        }catch (Exception ex) {
            //Revisar para aplicar SCErrorException
            log.error("oGSCUser.getLogin(),PVM,Erro ao criar nova Previs�o de Vendas Mensais" + ex.getMessage());
            throw new RuntimeException("An error occurred while getting Golden Record Relationships", ex);
        }
       return success;
    }

    public PVMMonthlyReport insertMonthlyReport(GSCUser oGSCUser, int subDealer){
        PVMMonthlyReport oPVMMonthlyReport = new PVMMonthlyReport();
        oPVMMonthlyReport.setYear(DateTimerTasks.getCurYear());
        oPVMMonthlyReport.setMonth(DateTimerTasks.getCurMonth());
        oPVMMonthlyReport.setOidDealer(oGSCUser.getOidDealerParent());
        oPVMMonthlyReport.setAvailable(0);
        oPVMMonthlyReport.setSubDealer(subDealer);
        return pvmMonthlyReportRepository.save(oPVMMonthlyReport);
    }
}
