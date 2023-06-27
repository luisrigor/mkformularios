package com.gsc.mkformularios.service;

import com.gsc.mkformularios.config.datasource.toyota.DbContext;
import com.gsc.mkformularios.constants.api.PVMEnpoints;
import com.gsc.mkformularios.dto.GoToModelDTO;
import com.gsc.mkformularios.exceptions.CreatePVMException;
import com.gsc.mkformularios.repository.toyota.PVMCarModelYearForecastRepository;
import com.gsc.mkformularios.repository.toyota.PVMCarmodelRepository;
import com.gsc.mkformularios.sample.data.provider.PVMData;
import com.gsc.mkformularios.sample.data.provider.SecurityData;
import com.gsc.mkformularios.security.UserPrincipal;
import com.gsc.mkformularios.service.impl.ModelServiceImpl;
import com.gsc.mkformularios.service.impl.PVMServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles(SecurityData.ACTIVE_PROFILE)

public class ModelServiceImplTest {
    @Mock
    private DbContext dbContext;
    @Mock
    private PVMCarmodelRepository pvmCarmodelRepository;
    @Mock
    private PVMCarModelYearForecastRepository pvmCarModelYearForecastRepository;

    @InjectMocks
    private ModelServiceImpl modelService;

    private SecurityData securityData;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        securityData = new SecurityData();
    }

    @Test
    void  whenGetModelThenReturnInfo() {
        when(pvmCarmodelRepository.getCar())
                .thenReturn(PVMData.getModelDTO().getCar());

        when(pvmCarmodelRepository.findById(anyInt()))
                .thenReturn(Optional.of(PVMData.getModelDTO().getCarModel()));

        when(pvmCarModelYearForecastRepository.findPVMCarModelYearForecastById(anyInt()))
                .thenReturn(PVMData.getModelDTO().getForecast());

        UserPrincipal userPrincipal = securityData.getUserPrincipal();
        userPrincipal.setOidNet("SC00010001");
        userPrincipal.setOidDealerParent("1");

        GoToModelDTO goToModel = modelService.goToModel(userPrincipal, true, 1, "1");

        assertEquals("A", goToModel.getCarModel().getType());
        assertEquals("A", goToModel.getCarModel().getActive());
        assertEquals("N", goToModel.getCarModel().getName());
        assertEquals("A", goToModel.getCar().get(0).getType());
        assertEquals("A", goToModel.getCar().get(0).getActive());
        assertEquals("N", goToModel.getCar().get(0).getName());

    }

    @Test
    void  whenGetModelThenThrowException()  {
        when(pvmCarmodelRepository.getCar())
                .thenThrow(RuntimeException.class);

        UserPrincipal userPrincipal = securityData.getUserPrincipal();
        userPrincipal.setOidNet("1");
        userPrincipal.setOidDealerParent("1");

        assertThrows(RuntimeException.class, ()-> modelService.goToModel(userPrincipal, true, 1, "1"));
    }

    @Test
    void  whenSaveModelThenReturnTrue() {

        when(pvmCarmodelRepository.findById(anyInt()))
                .thenReturn(Optional.of(PVMData.getModelDTO().getCarModel()));


        UserPrincipal userPrincipal = securityData.getUserPrincipal();
        userPrincipal.setOidNet("SC00010001");
        userPrincipal.setOidDealerParent("1");

        Boolean saveModel = modelService.saveModel(userPrincipal, PVMData.getSModelDTO(), 1);

        assertTrue(saveModel);
    }

    @Test
    void  whenSaveModelThenReturnTrueSaved() {
        when(pvmCarmodelRepository.save(any()))
                .thenReturn(PVMData.getModelDTO().getCarModel());


        UserPrincipal userPrincipal = securityData.getUserPrincipal();
        userPrincipal.setOidNet("SC00010001");
        userPrincipal.setOidDealerParent("1");

        Boolean saveModel = modelService.saveModel(userPrincipal, PVMData.getSModelDTO(), 0);

        assertTrue(saveModel);
    }

    @Test
    void  whenSaveModelThenReturnFalse() {
        when(pvmCarmodelRepository.save(any()))
                .thenThrow(RuntimeException.class);


        UserPrincipal userPrincipal = securityData.getUserPrincipal();
        userPrincipal.setOidNet("SC00010001");
        userPrincipal.setOidDealerParent("1");

        assertThrows(RuntimeException.class, ()->modelService.saveModel(userPrincipal, PVMData.getSModelDTO(), 0));

    }
}
