package com.gsc.mkformularios.controller;

import com.google.gson.Gson;
import com.gsc.mkformularios.config.SecurityConfig;
import com.gsc.mkformularios.config.environment.EnvironmentConfig;
import com.gsc.mkformularios.constants.api.PVMEnpoints;
import com.gsc.mkformularios.repository.toyota.ClientRepository;
import com.gsc.mkformularios.repository.toyota.ConfigurationRepository;
import com.gsc.mkformularios.repository.toyota.LoginKeyRepository;
import com.gsc.mkformularios.repository.toyota.ServiceLoginRepository;
import com.gsc.mkformularios.sample.data.provider.PVMData;
import com.gsc.mkformularios.sample.data.provider.SecurityData;
import com.gsc.mkformularios.security.TokenProvider;
import com.gsc.mkformularios.service.BudgetService;
import com.gsc.mkformularios.service.PlanService;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@Import({SecurityConfig.class, TokenProvider.class})
@ActiveProfiles(profiles = SecurityData.ACTIVE_PROFILE)
@WebMvcTest(PlanController.class)
class PlanControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private PlanService planService;


    @MockBean
    private ConfigurationRepository configurationRepository;
    @MockBean
    private LoginKeyRepository loginKeyRepository;
    @MockBean
    private ServiceLoginRepository serviceLoginRepository;
    @MockBean
    private EnvironmentConfig environmentConfig;
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
    void whenEditPlanThenReturnInfo() throws Exception {
        String accessToken = generatedToken;

        when(planService.goToEditPlan(any(), any()))
                .thenReturn(PVMData.getPlanDto());


        mvc.perform(get(BASE_REQUEST_MAPPING+PVMEnpoints.PLAN_EDIT+"?year=1").header("accessToken", accessToken))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.car[0].type").value("A"))
                .andExpect(jsonPath("$.car[0].active").value("A"))
                .andExpect(jsonPath("$.car[0].name").value("N"))
                .andExpect(jsonPath("$.car[0].changedBy").value("user"));
    }

    @Test
    void whenUploadPlanThenReturnSaved() throws Exception {
        String accessToken = generatedToken;
        byte[] fileContent = "Test file content".getBytes(StandardCharsets.UTF_8);

        MockMultipartFile file = new MockMultipartFile("file", "test.txt", MediaType.TEXT_PLAIN_VALUE, fileContent);


        when(planService.uploadPlan(any(), any(), any()))
                .thenReturn(Arrays.asList("saved"));

        String uri = BASE_REQUEST_MAPPING+PVMEnpoints.PLAN_UPLOAD+"?yearPlanUpload=1";
        ResultActions response = mvc.perform(MockMvcRequestBuilders.multipart(uri)
                        .file(file)
                .header("accessToken", accessToken));

                response
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void whenDownloadPlanThenReturnInfo() throws Exception {
        String accessToken = generatedToken;
        List<String[]> csvResponse = new ArrayList<>();
        String[] csv1 = new String[]{"H1", "H2", "H3", "H4"};

        csvResponse.add(csv1);

        when(planService.downloadPlan(any(), any(), any())).thenReturn(csvResponse);

        mvc.perform(get(BASE_REQUEST_MAPPING+PVMEnpoints.PLAN_DOWNLOAD+"?yearPlan=1").header("accessToken", accessToken))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void whenDownloadPlanThenThrows() throws Exception {
        String accessToken = generatedToken;
        List<String[]> csvResponse = new ArrayList<>();
        String[] csv1 = new String[]{"H1", "H2", "H3", "H4"};

        csvResponse.add(csv1);

        when(planService.downloadPlan(any(), any(), any())).thenThrow(RuntimeException.class);

        mvc.perform(get(BASE_REQUEST_MAPPING+PVMEnpoints.PLAN_DOWNLOAD+"?yearPlan=1").header("accessToken", accessToken))
                .andExpect(status().is5xxServerError());
    }

}
