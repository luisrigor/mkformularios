package com.gsc.mkformularios.service.impl;

import com.gsc.mkformularios.dto.GoToModelDTO;
import com.gsc.mkformularios.dto.ModelDTO;
import com.gsc.mkformularios.config.datasource.toyota.DbClient;
import com.gsc.mkformularios.config.datasource.toyota.DbContext;
import com.gsc.mkformularios.model.toyota.entity.PVMCarModelYearForecast;
import com.gsc.mkformularios.model.toyota.entity.PVMCarmodel;
import com.gsc.mkformularios.repository.toyota.PVMCarModelYearForecastRepository;
import com.gsc.mkformularios.repository.toyota.PVMCarmodelRepository;
import com.gsc.mkformularios.security.UserPrincipal;
import com.gsc.mkformularios.service.ModelService;
import com.rg.dealer.Dealer;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

import static com.gsc.mkformularios.constants.DATAConstants.APP_LEXUS;
import static com.gsc.mkformularios.constants.DATAConstants.APP_TOYOTA;

@RequiredArgsConstructor
@Service
@Log4j
public class ModelServiceImpl implements ModelService {

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

    public GoToModelDTO goToModel(UserPrincipal userPrincipal, boolean isDetail, Integer idModel,String year) {
        try {
            this.setDataSourceContext(userPrincipal.getClientId());
            List<PVMCarModelYearForecast> hmForecasts = null;
            Optional<PVMCarmodel> oPVMCarModel = Optional.empty();
            List<PVMCarmodel> car = pvmCarmodelRepository.getCar();
            if(isDetail){
                oPVMCarModel = pvmCarmodelRepository.findById(idModel);
                log.debug("Year: "+year);
                if(String.valueOf(userPrincipal.getOidNet()).equals(Dealer.OID_NET_TOYOTA)){
                    hmForecasts =pvmCarmodelYearForecastRepository.findPVMCarModelYearForecastById(idModel);
                }
            }
            return GoToModelDTO
                    .builder()
                    .car(car)
                    .forecast(hmForecasts)
                    .carModel(oPVMCarModel.orElse(null))
                    .build();
        } catch (Exception e) {
            //TODO Agregar SCErrorException
            log.error("Erro ao consultar detalhe do modelo"+ e.getMessage());
            throw new RuntimeException("Erro ao consultar detalhe do");
        }
    }

    @Override
    public Boolean saveModel(UserPrincipal userPrincipal, ModelDTO model,int idModel) {
        this.setDataSourceContext(userPrincipal.getClientId());
        try{
            if (idModel > 0) {
                PVMCarmodel oPVMCarmodel = pvmCarmodelRepository.findById(idModel).get();
            }else {
                PVMCarmodel carModel = saveCarModel(model);
                pvmCarmodelRepository.save(carModel);
            }
        }catch (Exception e){
            //Agregar SCErrorException
            log.error("CarModel,Erro ao gerir Modelos" + e.getMessage());
            throw new RuntimeException("Error saving model", e);
        }

        //agregar de return un boolean o DTO
        return true;
    }

    public PVMCarmodel saveCarModel(ModelDTO model){
        PVMCarmodel  oPVMCarmodel = new PVMCarmodel();
        oPVMCarmodel.setName(model.getModel());
        oPVMCarmodel.setActive("S");
        oPVMCarmodel.setType(model.getType());
        oPVMCarmodel.setDtFrom(model.getFrom());
        oPVMCarmodel.setDtTo(model.getTo());
        oPVMCarmodel.setExportOrder(model.getOrder());
        return oPVMCarmodel;
    }
}
