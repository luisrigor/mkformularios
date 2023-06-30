package com.gsc.mkformularios.service.impl;


import com.gsc.mkformularios.config.datasource.toyota.DbClient;
import com.gsc.mkformularios.config.datasource.toyota.DbContext;
import com.gsc.mkformularios.dto.PlanDTO;
import com.gsc.mkformularios.model.toyota.entity.PVMCarModelYearForecast;
import com.gsc.mkformularios.model.toyota.entity.PVMCarmodel;
import com.gsc.mkformularios.repository.toyota.PVMCarModelYearForecastRepository;
import com.gsc.mkformularios.repository.toyota.PVMCarmodelRepository;
import com.gsc.mkformularios.security.UserPrincipal;
import com.gsc.mkformularios.service.PlanService;
import com.rg.dealer.Dealer;
import com.sc.commons.utils.StringTasks;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;

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
}
