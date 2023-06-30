package com.gsc.mkformularios.service;

import com.gsc.mkformularios.config.datasource.toyota.DbContext;
import com.gsc.mkformularios.dto.PlanDTO;
import com.gsc.mkformularios.repository.toyota.PVMBudgetRepository;
import com.gsc.mkformularios.repository.toyota.PVMCarModelYearForecastRepository;
import com.gsc.mkformularios.repository.toyota.PVMCarmodelRepository;
import com.gsc.mkformularios.sample.data.provider.PVMData;
import com.gsc.mkformularios.sample.data.provider.SecurityData;
import com.gsc.mkformularios.security.UserPrincipal;
import com.gsc.mkformularios.service.impl.BudgetServiceImpl;
import com.gsc.mkformularios.service.impl.PlanServiceImpl;
import com.gsc.mkformularios.utils.DealersUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ActiveProfiles(SecurityData.ACTIVE_PROFILE)
public class PlanServiceImplTest {

    @Mock
    private DbContext dbContext;
    @Mock
    private PVMCarmodelRepository pvmCarmodelRepository;
    @Mock
    private PVMCarModelYearForecastRepository pvmCarmodelYearForecastRepository;

    @InjectMocks
    private PlanServiceImpl planService;

    private SecurityData securityData;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        securityData = new SecurityData();
    }

    @Test
    void whenEditPlanReturnInfo() {
        UserPrincipal userPrincipal = securityData.getUserPrincipal();
        userPrincipal.setOidNet("SC00010001");
        userPrincipal.setOidDealerParent("1");

        when(pvmCarmodelRepository.getCar()).thenReturn(PVMData.getPlanDto().getCar());

        when(pvmCarmodelYearForecastRepository.findAllByYear(anyInt())).thenReturn(PVMData.getPlanDto().getForecast());

        PlanDTO planDTO = planService.goToEditPlan(userPrincipal, "2023");

        assertEquals("A", planDTO.getCar().get(0).getType());
        assertEquals("A", planDTO.getCar().get(0).getActive());
        assertEquals("N", planDTO.getCar().get(0).getName());
        assertEquals("user", planDTO.getCar().get(0).getChangedBy());
        assertEquals(1, planDTO.getForecast().get(0).getForecast());
        assertEquals(1, planDTO.getForecast().get(0).getIdCarModel());
        assertEquals(1, planDTO.getForecast().get(0).getId());
    }

    @Test
    void whenEditPlanThrows() {
        UserPrincipal userPrincipal = securityData.getUserPrincipal();
        userPrincipal.setOidNet("SC00010001");
        userPrincipal.setOidDealerParent("1");

        when(pvmCarmodelRepository.getCar()).thenThrow(RuntimeException.class);

        assertThrows(RuntimeException.class, ()->planService.goToEditPlan(userPrincipal, "2023"));



    }

}
