package com.gsc.mkformularios.service;

import com.gsc.mkformularios.config.datasource.toyota.DbContext;
import com.gsc.mkformularios.dto.BudgetDTO;
import com.gsc.mkformularios.exceptions.GetPVMException;
import com.gsc.mkformularios.repository.toyota.PVMBudgetRepository;
import com.gsc.mkformularios.sample.data.provider.PVMData;
import com.gsc.mkformularios.sample.data.provider.SecurityData;
import com.gsc.mkformularios.security.UserPrincipal;
import com.gsc.mkformularios.service.impl.BudgetServiceImpl;
import com.gsc.mkformularios.utils.DealersUtils;
import com.sc.commons.exceptions.SCErrorException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import java.util.Vector;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ActiveProfiles(SecurityData.ACTIVE_PROFILE)
public class BudgetServiceImplTest {

    @Mock
    private DbContext dbContext;
    @Mock
    private PVMBudgetRepository pvmBudgetRepository;
    @Mock
    private DealersUtils dealersUtils;

    @InjectMocks
    private BudgetServiceImpl budgetService;

    private SecurityData securityData;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        securityData = new SecurityData();
    }

    @Test
    void whenEditBudgetThenReturnInfo() throws SCErrorException {
        UserPrincipal userPrincipal = securityData.getUserPrincipal();
        userPrincipal.setOidNet("1");
        userPrincipal.setOidDealerParent("1");

        when(pvmBudgetRepository.getPVMBudgetByYear(any()))
                .thenReturn(PVMData.getBudgetDto().getBudgets());

        when(dealersUtils.getRetailerDealers(anyString()))
                .thenReturn(new Vector<>());

        BudgetDTO budgetDTO = budgetService.editBudget("2023", userPrincipal);

        assertEquals(1 ,budgetDTO.getBudgets().get(0).getIdPvmCarModel());
        assertEquals(1 ,budgetDTO.getBudgets().get(0).getYear());
        assertEquals(1 ,budgetDTO.getBudgets().get(0).getMonth());
        assertEquals("1" ,budgetDTO.getBudgets().get(0).getOidDealer());


    }

    @Test
    void whenEditBudgetThenThrows() {
        UserPrincipal userPrincipal = securityData.getUserPrincipal();
        userPrincipal.setOidNet("1");
        userPrincipal.setOidDealerParent("1");

        when(pvmBudgetRepository.getPVMBudgetByYear(any()))
                .thenThrow(GetPVMException.class);


       assertThrows(GetPVMException.class, ()->budgetService.editBudget("2023", userPrincipal));


    }


}
