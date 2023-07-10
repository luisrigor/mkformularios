package com.gsc.mkformularios.controller;

import com.google.gson.Gson;
import com.gsc.mkformularios.config.SecurityConfig;
import com.gsc.mkformularios.config.environment.EnvironmentConfig;
import com.gsc.mkformularios.constants.api.PVMEnpoints;
import com.gsc.mkformularios.dto.ModelDTO;
import com.gsc.mkformularios.dto.PVMGetDTO;
import com.gsc.mkformularios.dto.PVMRequestDTO;
import com.gsc.mkformularios.dto.ReportDetailRequestDto;
import com.gsc.mkformularios.model.toyota.entity.PVMCarmodel;
import com.gsc.mkformularios.repository.toyota.ClientRepository;
import com.gsc.mkformularios.repository.toyota.ConfigurationRepository;
import com.gsc.mkformularios.repository.toyota.LoginKeyRepository;
import com.gsc.mkformularios.repository.toyota.ServiceLoginRepository;
import com.gsc.mkformularios.sample.data.provider.PVMData;
import com.gsc.mkformularios.sample.data.provider.SecurityData;
import com.gsc.mkformularios.security.TokenProvider;
import com.gsc.mkformularios.security.UserPrincipal;
import com.gsc.mkformularios.service.ModelService;
import com.gsc.mkformularios.service.PVMService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({SecurityConfig.class, TokenProvider.class})
@ActiveProfiles(profiles = SecurityData.ACTIVE_PROFILE)
@WebMvcTest(PVMController.class)
class PVMControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private PVMService pvmService;


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

    private String BASE_REQUEST_MAPPING = "/formularios"+PVMEnpoints.PVM_BASE;
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
    void whenGetPVMGotoThenReturnInfo() throws Exception {
        String accessToken = generatedToken;

        PVMRequestDTO pvmRequestDTO = PVMRequestDTO.builder()
                .year(2023)
                .month(6)
                .build();

        when(pvmService.getPVM(any(PVMRequestDTO.class), any(UserPrincipal.class)))
                .thenReturn(new PVMGetDTO());

        mvc.perform(post(BASE_REQUEST_MAPPING+PVMEnpoints.PVM_GOTO_PVM).header("accessToken", accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(pvmRequestDTO)))

                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void whenGetPVMDetailThenReturnInfo() throws Exception {
        String accessToken = generatedToken;

        when(pvmService.getPVMDetail(anyInt(), any()))
                .thenReturn(PVMData.getPVMDetailData());

        mvc.perform(get(BASE_REQUEST_MAPPING+PVMEnpoints.PVM_DETAIL+"?idPVM=1").header("accessToken", accessToken))

                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.car.[0].id").value(93))
                .andExpect(jsonPath("$.car.[0].name").value("teste PR COL"))
                .andExpect(jsonPath("$.car.[0].type").value("PASSAGEIROS"))
                .andExpect(jsonPath("$.car.[0].active").value("S"))
                .andExpect(jsonPath("$.monthlyReport.id").value(436))
                .andExpect(jsonPath("$.salesAndPlates.[0].idPvmMonthlyreport").value(436));
    }

    @Test
    void  whenInsertNewPVMThenReturnTrue() throws Exception {
        String accessToken = generatedToken;

        when(pvmService.newPVM(any(), anyInt()))
                .thenReturn(true);

        mvc.perform(post(BASE_REQUEST_MAPPING+PVMEnpoints.PVM_NEW+"?subDealer=1").header("accessToken", accessToken))

                .andExpect(status().is2xxSuccessful());
    }


    @Test
    void whenSaveReportThenReturn() throws Exception {
        String accessToken = generatedToken;

        ReportDetailRequestDto requestDto = ReportDetailRequestDto.builder()
                .contract("1")
                .s2("1")
                .s3("1")
                .p1("1")
                .p2("1")
                .build();

        List<ReportDetailRequestDto> reportDetailRequestDtoList = new ArrayList<>();

        reportDetailRequestDtoList.add(requestDto);

        doNothing().when(pvmService).saveReportDetail(any(), any(), anyString());

        mvc.perform(post(BASE_REQUEST_MAPPING+PVMEnpoints.PVM_SAVE_REPORT_DETAIL+"?idPVMS=1").header("accessToken", accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(reportDetailRequestDtoList)))

                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void whenSendReportThenReturnOk() throws Exception {
        String accessToken = generatedToken;

        ReportDetailRequestDto requestDto = ReportDetailRequestDto.builder()
                .contract("1")
                .s2("1")
                .s3("1")
                .p1("1")
                .p2("1")
                .build();

        List<ReportDetailRequestDto> reportDetailRequestDtoList = new ArrayList<>();

        reportDetailRequestDtoList.add(requestDto);

        doNothing().when(pvmService).sendReportDetail(any(), any(), anyString());

        mvc.perform(post(BASE_REQUEST_MAPPING+PVMEnpoints.PVM_SEND_REPORTDETAIL+"?idPVMS=1").header("accessToken", accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(reportDetailRequestDtoList)))

                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void whenProvidePVMToDealerThenReturnOk() throws Exception {
        String accessToken = generatedToken;

        doNothing().when(pvmService).providePVMToDealer(any(), any(), anyInt());

        mvc.perform(post(BASE_REQUEST_MAPPING+PVMEnpoints.PVM_PROVIDE_TO_DEALER+"?idPVM=1&cancelReasons=tests").header("accessToken", accessToken))

                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void whenrequestToChangeThenReturnOk() throws Exception {
        String accessToken = generatedToken;


        doNothing().when(pvmService).requestToChange(any(), any(), anyString());

        mvc.perform(post(BASE_REQUEST_MAPPING+PVMEnpoints.PVM_REQUEST_TO_CHANGE+"?idPVMS=1&cancelReasons=tests").header("accessToken", accessToken))

                .andExpect(status().is2xxSuccessful());
    }






}
