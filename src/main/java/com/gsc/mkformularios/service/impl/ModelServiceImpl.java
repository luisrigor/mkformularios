package com.gsc.mkformularios.service.impl;

import com.gsc.mkformularios.config.datasource.DbClient;
import com.gsc.mkformularios.config.datasource.DbContext;
import com.gsc.mkformularios.dto.GoToModelDTO;
import com.gsc.mkformularios.dto.ModelDTO;
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

import java.sql.Date;
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

    public GoToModelDTO goToModel(UserPrincipal userPrincipal, boolean isDetail, int idModel,String year) {
        try {
            this.setDataSourceContext(userPrincipal.getClientId());
            List<PVMCarModelYearForecast> hmForecasts = null;
            Optional<PVMCarmodel> oPVMCarModel = null;
            List<PVMCarmodel> car = pvmCarmodelRepository.getCar();
            if(isDetail){
                oPVMCarModel = pvmCarmodelRepository.findById(idModel);
                log.debug("Year: "+year);
                if(String.valueOf(userPrincipal.getClientId()).equals(Dealer.OID_NET_TOYOTA)){
                    hmForecasts =pvmCarmodelYearForecastRepository.findAllById(idModel);
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
    public void saveModel(UserPrincipal userPrincipal, ModelDTO model,int idModel) {
        this.setDataSourceContext(userPrincipal.getClientId());
        try{
            if (idModel > 0) {
                PVMCarmodel oPVMCarmodel = pvmCarmodelRepository.findById(idModel).get();
            }else {
                PVMCarmodel carModel = saveCarmodel(model);
                idModel > 0 ? carModel.save(/*agregar getUserStamp*/) : pvmCarmodelRepository.save(carModel);
            }
        }catch (Exception e){
            //Agregar SCErrorException
            log.error("CarModel,Erro ao gerir Modelos" + e.getMessage());
        }
        //agregar de return un boolean o DTO
    }

    public PVMCarmodel saveCarmodel(ModelDTO model){
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
