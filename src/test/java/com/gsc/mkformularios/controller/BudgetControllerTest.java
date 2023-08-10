package com.gsc.mkformularios.controller;

import com.google.gson.Gson;
import com.gsc.mkformularios.config.SecurityConfig;
import com.gsc.mkformularios.config.datasource.toyota.DbContext;
import com.gsc.mkformularios.config.environment.EnvironmentConfig;
import com.gsc.mkformularios.constants.api.PVMEnpoints;
import com.gsc.mkformularios.model.toyota.entity.PVMBudget;
import com.gsc.mkformularios.repository.toyota.ClientRepository;
import com.gsc.mkformularios.repository.toyota.ConfigurationRepository;
import com.gsc.mkformularios.repository.toyota.LoginKeyRepository;
import com.gsc.mkformularios.repository.toyota.ServiceLoginRepository;
import com.gsc.mkformularios.sample.data.provider.PVMData;
import com.gsc.mkformularios.sample.data.provider.SecurityData;
import com.gsc.mkformularios.security.TokenProvider;
import com.gsc.mkformularios.service.BudgetService;
import com.gsc.mkformularios.service.PVMService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({SecurityConfig.class, TokenProvider.class})
@ActiveProfiles(profiles = SecurityData.ACTIVE_PROFILE)
@WebMvcTest(BudgetController.class)
class BudgetControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BudgetService budgetService;


    @MockBean
    private ConfigurationRepository configurationRepository;
    @MockBean
    private LoginKeyRepository loginKeyRepository;
    @MockBean
    private ServiceLoginRepository serviceLoginRepository;
    @MockBean
    private EnvironmentConfig environmentConfig;
    @MockBean
    private DbContext dbContext;
    @MockBean
    private ClientRepository clientRepository;
    private Gson gson;
    private SecurityData securityData;

    private String BASE_REQUEST_MAPPING = "/formularios"+ PVMEnpoints.PVM_BASE;
    private static String generatedToken;
    @BeforeEach
    void setUp() {
        gson = new Gson();
        securityData = new SecurityData();
        when(loginKeyRepository.findById(anyLong())).thenReturn(Optional.of(securityData.getLoginKey()));
    }

    @BeforeAll
    static void beforeAll() {
        SecurityData secData = new SecurityData();
        generatedToken = secData.generateNewToken();
    }

    @Test
    void whenEditBudgetThenReturnInfo() throws Exception {
        String accessToken = generatedToken;

        when(budgetService.editBudget(any(), any()))
                .thenReturn(PVMData.getBudgetDto());

        mvc.perform(get(BASE_REQUEST_MAPPING+PVMEnpoints.BUDGET_EDIT+"?year=1").header("accessToken", accessToken)
                .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.budgetDealers[0].budgets[0].oidDealer").value(1))
                .andExpect(jsonPath("$.budgetDealers[0].budgets[0].year").value(1))
                .andExpect(jsonPath("$.budgetDealers[0].budgets[0].month").value(1))
                .andExpect(jsonPath("$.budgetDealers[0].budgets[0].idPvmCarModel").value(1));

    }

    @Test
    void whenSaveBudgetThenReturnSaved() throws Exception {
        String accessToken = generatedToken;
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

        doNothing().when(budgetService).saveBudget(any(), any(),any());

        mvc.perform(post(BASE_REQUEST_MAPPING+PVMEnpoints.BUDGET_SAVE+"?yearSelect=1").header("accessToken", accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(pvmBudgetList)))

                .andExpect(status().is2xxSuccessful());

    }

    @Test
    void whenDownloadBudgetThenReturnInfo() throws Exception {
        String accessToken = generatedToken;
        List<String[]> csvResponse = new ArrayList<>();
        String[] csv1 = new String[]{"H1", "H2", "H3", "H4"};

        csvResponse.add(csv1);

        when(budgetService.downloadBudget(any(), any(), any())).thenReturn(csvResponse);

        mvc.perform(get(BASE_REQUEST_MAPPING+PVMEnpoints.BUDGET_DOWNLOAD+"?yearBudget=1").header("accessToken", accessToken))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void whenDownloadBudgetThenThrows() throws Exception {
        String accessToken = generatedToken;
        List<String[]> csvResponse = new ArrayList<>();
        String[] csv1 = new String[]{"H1", "H2", "H3", "H4"};

        csvResponse.add(csv1);

        when(budgetService.downloadBudget(any(), any(), any())).thenThrow(RuntimeException.class);

        mvc.perform(get(BASE_REQUEST_MAPPING+PVMEnpoints.BUDGET_DOWNLOAD+"?yearBudget=1").header("accessToken", accessToken))
                .andExpect(status().is5xxServerError());
    }

    @Test
    void whenUploadBudgetThenReturnUploaded() throws Exception {
        String accessToken = generatedToken;
        byte[] fileContent = "Test file content".getBytes(StandardCharsets.UTF_8);
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", MediaType.TEXT_PLAIN_VALUE, fileContent);

        doNothing().when(budgetService).uploadBudget(any(), any(), any());
        String uri = BASE_REQUEST_MAPPING+PVMEnpoints.BUDGET_UPLOAD+"?yearBudget=1";
        mvc.perform(MockMvcRequestBuilders.multipart(uri)
                        .file(file)
                        .header("accessToken", accessToken))
                .andExpect(status().is2xxSuccessful());
    }

}
