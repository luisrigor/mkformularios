package com.gsc.mkformularios.service.impl;

import com.gsc.mkformularios.config.datasource.DbClient;
import com.gsc.mkformularios.config.datasource.DbContext;
import com.gsc.mkformularios.dto.PlanDTO;
import com.gsc.mkformularios.model.entity.PVMCarModelYearForecast;
import com.gsc.mkformularios.model.entity.PVMCarmodel;
import com.gsc.mkformularios.repository.PVMCarModelYearForecastRepository;
import com.gsc.mkformularios.repository.PVMCarmodelRepository;
import com.gsc.mkformularios.security.UserPrincipal;
import com.gsc.mkformularios.service.PlanService;
import com.rg.dealer.Dealer;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Log4j
public class PlanServiceImpl implements PlanService {

    private final DbContext dbContext;
    private final PVMCarmodelRepository pvmCarmodelRepository;
    private final PVMCarModelYearForecastRepository pvmCarmodelYearForecastRepository;

    public void setDataSourceContext(Long client) {
        if (client == 2L)
            dbContext.setBranchContext(DbClient.DB_LEXUS);
    }

    @Override
    public PlanDTO goToEditPlan(UserPrincipal userPrincipal, int year) {
        this.setDataSourceContext(userPrincipal.getClientId());
        try {
            List<PVMCarmodel> car = pvmCarmodelRepository.getCar();
            List<PVMCarModelYearForecast> aLForecasts = null;
            if (String.valueOf(userPrincipal.getClientId()).equals(Dealer.OID_NET_TOYOTA)) {
                aLForecasts = pvmCarmodelYearForecastRepository.findAllYear(year);
            }
            return PlanDTO.builder().car(car).forecast(aLForecasts).build();
        } catch (Exception e) {
            log.error("goToEditPlan,Erro ao consultar aplica��o PVM" + e.getMessage());
        }
        return null;
    }
}