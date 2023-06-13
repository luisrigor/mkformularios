package com.gsc.mkformularios.service.impl;

import com.gsc.mkformularios.config.datasource.DbClient;
import com.gsc.mkformularios.config.datasource.DbContext;
import com.gsc.mkformularios.dto.PVMDetailDTO;
import com.gsc.mkformularios.dto.SalesPlates;
import com.gsc.mkformularios.model.entity.PVMCarmodel;
import com.gsc.mkformularios.model.entity.PVMMonthlyReport;
import com.gsc.mkformularios.repository.PVMCarmodelRepository;
import com.gsc.mkformularios.repository.PVMMonthlyReportRepository;
import com.gsc.mkformularios.security.UserPrincipal;
import com.gsc.mkformularios.service.PVMService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PVMServiceImpl implements PVMService {


    private final DbContext dbContext;
    private final PVMCarmodelRepository pvmCarmodelRepository;
    private final PVMMonthlyReportRepository pvmMonthlyReportRepository;

    public void setDataSourceContext(Long client){
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
}
