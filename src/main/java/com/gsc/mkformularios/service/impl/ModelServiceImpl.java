package com.gsc.mkformularios.service.impl;

import com.gsc.mkformularios.config.datasource.DbClient;
import com.gsc.mkformularios.config.datasource.DbContext;
import com.gsc.mkformularios.model.entity.PVMCarModelYearForecast;
import com.gsc.mkformularios.model.entity.PVMCarmodel;
import com.gsc.mkformularios.repository.PVMCarModelYearForecastRepository;
import com.gsc.mkformularios.repository.PVMCarmodelRepository;
import com.gsc.mkformularios.security.UserPrincipal;
import com.gsc.mkformularios.service.ModelService;
import com.rg.dealer.Dealer;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Log4j
public class ModelServiceImpl implements ModelService {

    private final DbContext dbContext;
    private final PVMCarmodelRepository pvmCarmodelRepository;
    private final PVMCarModelYearForecastRepository pvmCarmodelYearForecastRepository;

    public void setDataSourceContext(Long client) {
        if (client == 2L)
            dbContext.setBranchContext(DbClient.DB_LEXUS);
    }

    public void goToModel(UserPrincipal userPrincipal, boolean isDetail, int idModel,String year) {
        try {
            this.setDataSourceContext(userPrincipal.getClientId());
            List<PVMCarmodel> car = pvmCarmodelRepository.getCar();
            if(isDetail){
                Optional<PVMCarmodel> oPVMCarModel = pvmCarmodelRepository.findById(idModel);
                log.debug("Year: "+year);
                if(String.valueOf(userPrincipal.getClientId()).equals(Dealer.OID_NET_TOYOTA)){
                    List<PVMCarModelYearForecast> hmForecasts =pvmCarmodelYearForecastRepository.findAllById(idModel);
                }
            }
        } catch (Exception e) {
            //Agregar SCErrorException
            log.error("Erro ao consultar detalhe do modelo"+ e.getMessage());
        }
    }
}
