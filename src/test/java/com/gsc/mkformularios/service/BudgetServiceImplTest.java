package com.gsc.mkformularios.service;

import com.gsc.mkformularios.config.datasource.toyota.DbContext;
import com.gsc.mkformularios.dto.BudgetDTO;
import com.gsc.mkformularios.exceptions.CreatePVMException;
import com.gsc.mkformularios.exceptions.GetPVMException;
import com.gsc.mkformularios.model.toyota.entity.PVMBudget;
import com.gsc.mkformularios.repository.toyota.PVMBudgetRepository;
import com.gsc.mkformularios.repository.toyota.PVMCarmodelRepository;
import com.gsc.mkformularios.sample.data.provider.PVMData;
import com.gsc.mkformularios.sample.data.provider.SecurityData;
import com.gsc.mkformularios.security.UserPrincipal;
import com.gsc.mkformularios.service.impl.BudgetServiceImpl;
import com.gsc.mkformularios.utils.DealersUtils;
import com.rg.dealer.Dealer;
import com.sc.commons.exceptions.SCErrorException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ActiveProfiles(SecurityData.ACTIVE_PROFILE)
public class BudgetServiceImplTest {

    @Mock
    private DbContext dbContext;
    @Mock
    private PVMBudgetRepository pvmBudgetRepository;
    @Mock
    private DealersUtils dealersUtils;
    @Mock
    private PVMCarmodelRepository pvmCarmodelRepository;


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
                .thenReturn(PVMData.getBudgetDealerDto());

        when(dealersUtils.getRetailerDealers(anyString()))
                .thenReturn(new Vector<>());

        when(pvmCarmodelRepository.getCarTypes()).thenReturn(PVMData.getMapTypes());

        BudgetDTO budgetDTO = budgetService.editBudget("2023", userPrincipal);

        assertEquals("SC00020001" ,budgetDTO.getBudgetDealers().get(0).getDealers()[0]);
        assertEquals("75" ,budgetDTO.getBudgetDealers().get(0).getDealers()[2]);
        assertEquals("1" ,budgetDTO.getBudgetDealers().get(0).getDealers()[3]);



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

    @Test
    void whenSaveBudgetThenReturnInfo()  {
        List<PVMBudget> pvmBudgetList = new ArrayList<>();

        PVMBudget pvmBudget = PVMBudget.builder()
                .plates(1)
                .oidDealer("1")
                .subDealer(1)
                .month(1)
                .year(1)
                .idPvmCarModel(1)
                .build();

        pvmBudgetList.add(pvmBudget);

        UserPrincipal userPrincipal = securityData.getUserPrincipal();
        userPrincipal.setOidNet("1");
        userPrincipal.setOidDealerParent("1");

        doNothing().when(pvmBudgetRepository).deleteBudgetsByDealerAndYear(anyString(), anyInt(), anyInt());

        when(pvmBudgetRepository.save(any())).thenReturn(pvmBudget);

        budgetService.saveBudget("2023", userPrincipal, pvmBudgetList);

        verify(pvmBudgetRepository, times(1)).deleteBudgetsByDealerAndYear(anyString(), anyInt(), anyInt());



    }

    @Test
    void whenSaveBudgetThenThrows() {
        List<PVMBudget> pvmBudgetList = new ArrayList<>();

        PVMBudget pvmBudget = PVMBudget.builder()
                .plates(1)
                .oidDealer("1")
                .subDealer(1)
                .month(1)
                .year(1)
                .idPvmCarModel(1)
                .build();

        pvmBudgetList.add(pvmBudget);

        UserPrincipal userPrincipal = securityData.getUserPrincipal();
        userPrincipal.setOidNet("1");
        userPrincipal.setOidDealerParent("1");

        doThrow(new CreatePVMException("Error Test")).when(pvmBudgetRepository).deleteBudgetsByDealerAndYear(any(), anyInt(), anyInt());

        assertThrows(CreatePVMException.class, ()->budgetService.saveBudget("2023", userPrincipal, pvmBudgetList));
    }


    @Test
    void whenDownloadBudgetThenReturnInfo() throws SCErrorException {
        UserPrincipal userPrincipal = securityData.getUserPrincipal();
        userPrincipal.setOidNet("1");
        userPrincipal.setOidDealerParent("1");
        HttpServletResponse response = new MockHttpServletResponse();


        when(pvmBudgetRepository.getPVMBudgetByYear(any()))
                .thenReturn(PVMData.getBudgetDealerDto());

        when(dealersUtils.getRetailerDealers(anyString()))
                .thenReturn(new Vector<>());

        when(pvmCarmodelRepository.getCarTypes()).thenReturn(PVMData.getMapTypes());

        List<String[]> budget = budgetService.downloadBudget("2023", userPrincipal, response);
        assertEquals("=75", budget.get(1)[0]);
        assertEquals("1", budget.get(1)[1]);
        assertEquals("0", budget.get(1)[2]);
        assertEquals("=99", budget.get(2)[0]);
        assertEquals("1", budget.get(2)[1]);
        assertEquals("0", budget.get(2)[2]);
    }

    @Test
    void whenDownloadBudgetThenThrows() {
        UserPrincipal userPrincipal = securityData.getUserPrincipal();
        userPrincipal.setOidNet("1");
        userPrincipal.setOidDealerParent("1");
        HttpServletResponse response = new MockHttpServletResponse();


        when(pvmBudgetRepository.getPVMBudgetByYear(any()))
                .thenThrow(GetPVMException.class);

        assertThrows(GetPVMException.class, ()-> budgetService.downloadBudget("2023", userPrincipal, response));
    }

    @Test
    void whenUploadThenReturnUploaded() throws IOException, SCErrorException {

        List<PVMBudget> pvmBudgetList = new ArrayList<>();

        PVMBudget pvmBudget = PVMBudget.builder()
                .plates(1)
                .oidDealer("1")
                .subDealer(1)
                .month(1)
                .year(1)
                .idPvmCarModel(1)
                .build();

        pvmBudgetList.add(pvmBudget);

        Dealer dealer1 = new Dealer();
        dealer1.setDistrict("D");
        dealer1.setMunicipality("M");
        dealer1.setCp3(1);
        dealer1.setEmail("a@a");
        dealer1.setResp("R");
        dealer1.setCp4(2);
        dealer1.setDealerCode("1");
        Hashtable<String, Dealer> allDealers = new Hashtable<>();

        allDealers.put("1", dealer1);


        UserPrincipal userPrincipal = securityData.getUserPrincipal();
        userPrincipal.setOidNet("1");
        userPrincipal.setOidDealerParent("1");

        when(pvmCarmodelRepository.getCarTypes()).thenReturn(PVMData.getMapTypes());

        doNothing().when(pvmBudgetRepository).deleteBudgetsByDealerAndYear(anyString(), anyInt(), anyInt());

        when(pvmBudgetRepository.save(any())).thenReturn(pvmBudget);

        budgetService.saveBudget("2023", userPrincipal, pvmBudgetList);

        verify(pvmBudgetRepository, times(1)).deleteBudgetsByDealerAndYear(anyString(), anyInt(), anyInt());

        when(dealersUtils.getAllActiveMainDealers(anyString()))
                .thenReturn(allDealers);


        byte[] fileData = (
                "Cod. Concessão;Mês;testNewType;COMERCIAIS;TESTESTS;PASSAGEIROS \n" +
                        "=61;1;888;0;0;0 \n" +
                        "=42;1;0;0;0;0 \n" +
                        "=22;1;0;0;0;0 \n" +
                        "=75;1;0;0;0;0 \n"
                ).getBytes(StandardCharsets.ISO_8859_1);
        InputStream inputStream = new ByteArrayInputStream(fileData);
        MockMultipartFile file = new MockMultipartFile("file", "test.csv",
                "text/csv", inputStream);


        budgetService.uploadBudget("2023", userPrincipal, file);


        verify(pvmBudgetRepository, times(2)).save(any());
        verify(pvmCarmodelRepository).getCarTypes();
        verify(dealersUtils).getAllActiveMainDealers(any());

    }

    @Test
    void whenUploadThenThrows() throws IOException, SCErrorException {

        List<PVMBudget> pvmBudgetList = new ArrayList<>();

        PVMBudget pvmBudget = PVMBudget.builder()
                .plates(1)
                .oidDealer("1")
                .subDealer(1)
                .month(1)
                .year(1)
                .idPvmCarModel(1)
                .build();

        pvmBudgetList.add(pvmBudget);

        Dealer dealer1 = new Dealer();
        dealer1.setDistrict("D");
        dealer1.setMunicipality("M");
        dealer1.setCp3(1);
        dealer1.setEmail("a@a");
        dealer1.setResp("R");
        dealer1.setCp4(2);
        dealer1.setDealerCode("1");
        Hashtable<String, Dealer> allDealers = new Hashtable<>();

        allDealers.put("1", dealer1);


        UserPrincipal userPrincipal = securityData.getUserPrincipal();
        userPrincipal.setOidNet("1");
        userPrincipal.setOidDealerParent("1");

        when(pvmCarmodelRepository.getCarTypes()).thenThrow(CreatePVMException.class);


        byte[] fileData = (
                "Cod. Concessão;Mês;testNewType;COMERCIAIS;TESTESTS;PASSAGEIROS \n" +
                        "=\"61\";1;888;0;0;0 \n" +
                        "=\"42\";1;0;0;0;0 \n" +
                        "=\"22\";1;0;0;0;0 \n" +
                        "=\"75\";1;0;0;0;0 \n"
        ).getBytes(StandardCharsets.ISO_8859_1);
        InputStream inputStream = new ByteArrayInputStream(fileData);
        MockMultipartFile file = new MockMultipartFile("file", "test.csv",
                "text/csv", inputStream);


        assertThrows(CreatePVMException.class, ()->budgetService.uploadBudget("2023", userPrincipal, file));

    }

}
