package com.gsc.mkformularios.service;


import com.gsc.mkformularios.config.datasource.toyota.DbContext;
import com.gsc.mkformularios.constants.AppProfile;
import com.gsc.mkformularios.constants.api.PVMEnpoints;
import com.gsc.mkformularios.dto.PVMDetailDTO;
import com.gsc.mkformularios.dto.PVMGetDTO;
import com.gsc.mkformularios.dto.PVMRequestDTO;
import com.gsc.mkformularios.dto.ReportDetailRequestDto;
import com.gsc.mkformularios.exceptions.CreatePVMException;
import com.gsc.mkformularios.exceptions.GetPVMException;
import com.gsc.mkformularios.model.toynet.entity.LexusRetailer;
import com.gsc.mkformularios.model.toyota.entity.PVMMonthlyReport;
import com.gsc.mkformularios.repository.toynet.LexusRetailerRepository;
import com.gsc.mkformularios.repository.toynet.ToyotaRetailerRepository;
import com.gsc.mkformularios.repository.toyota.PVMCarmodelRepository;
import com.gsc.mkformularios.repository.toyota.PVMMonthlyReportDetailRepository;
import com.gsc.mkformularios.model.toyota.entity.PVMMonthlyReport;
import com.gsc.mkformularios.repository.toyota.PVMCarmodelRepository;
import com.gsc.mkformularios.repository.toyota.PVMMonthlyReportRepository;
import com.gsc.mkformularios.sample.data.provider.PVMData;
import com.gsc.mkformularios.sample.data.provider.SecurityData;
import com.gsc.mkformularios.security.UserPrincipal;
import com.gsc.mkformularios.service.impl.PVMServiceImpl;
import com.gsc.mkformularios.service.impl.pvm.PVM1MonthReport;
import com.gsc.mkformularios.service.impl.pvm.PVM3MonthReport;
import com.gsc.mkformularios.utils.DealersUtils;
import com.gsc.mkformularios.utils.UsrlogonUtil;
import com.sc.commons.exceptions.SCErrorException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles(SecurityData.ACTIVE_PROFILE)
public class PVMServiceImplTest {

    @Mock
    private DbContext dbContext;
    @Mock
    private PVMCarmodelRepository pvmCarmodelRepository;
    @Mock
    private PVMMonthlyReportRepository pvmMonthlyReportRepository;
    @Mock
    private PVMMonthlyReportDetailRepository pvmMonthlyReportDetailRepository;
    @Mock
    private ToyotaRetailerRepository toyotaRetailerRepository;
    @Mock
    private LexusRetailerRepository lexusRetailerRepository;
    @Mock
    private DealersUtils dealersUtils;
    @Mock
    private UsrlogonUtil usrlogonUtil;
    @Mock
    private PVM1MonthReport pvm1MonthReport;
    @Mock
    private PVM3MonthReport pvm3MonthReport;

    @InjectMocks
    private PVMServiceImpl pvmService;

    private SecurityData securityData;



    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        securityData = new SecurityData();
    }

    @Test
    void whenGetPVMGotoThenThrow() {
        PVMRequestDTO pvmRequestDTO = PVMData.getPVMRequestDTO();
        UserPrincipal userPrincipal = securityData.getUserPrincipal();
        userPrincipal.setOidNet("1");
        userPrincipal.setOidDealerParent("1");

        when(pvmMonthlyReportRepository.getPVM(any(), any())).thenThrow(GetPVMException.class);

        assertThrows(GetPVMException.class, ()->pvmService.getPVM(pvmRequestDTO, userPrincipal));
    }

    @Test
    void whenGetPVMGotoThenReturnInfo() throws SCErrorException {

        Set<AppProfile> roles = new HashSet<>();
        roles.add(AppProfile.TCAP);
        UserPrincipal userPrincipal = new UserPrincipal("137||tcap1@tpo||tiago.fernandes@parceiro.rigorcg.pt",
                roles,33L);
        userPrincipal.setOidNet("SC00010001");
        userPrincipal.setOidDealerParent("1");

        PVMRequestDTO pvmRequestDTO = PVMData.getPVMRequestDTO();



        List<String> notSendOid =  new ArrayList<>();
        notSendOid.add("1");
        notSendOid.add("100C");
        notSendOid.add("2");

        when(pvmMonthlyReportRepository.getPVM(any(), any()))
                .thenReturn(PVMData.getPVMGotoData().getPvmMonthlyReports());

        when(dealersUtils.getMapDealers(anyString()))
                .thenReturn(PVMData.getPVMGotoData().getMapDealers());

        when(pvmMonthlyReportRepository.getNotSendPVMOid(anyInt(), anyInt()))
                .thenReturn(notSendOid);

        when(toyotaRetailerRepository.findAllByObjectidNotIn(any())).thenReturn(new ArrayList<>());



        PVMGetDTO pvm = pvmService.getPVM(pvmRequestDTO, userPrincipal);

        assertEquals(1, pvm.getPvmMonthlyReports().get(0).getId());
        assertEquals(1, pvm.getPvmMonthlyReports().get(0).getYear());
        assertEquals(1, pvm.getPvmMonthlyReports().get(0).getMonth());
        assertEquals("SC00020001", pvm.getPvmMonthlyReports().get(0).getOidDealer());
        assertEquals("D", pvm.getMapDealers().get(0).getDealer().getDistrict());
        assertEquals("M", pvm.getMapDealers().get(0).getDealer().getMunicipality());
    }

    @Test
    void whenGetPVMGotoThenReturnInfoLexus() throws SCErrorException {
        Set<AppProfile> roles = new HashSet<>();
        roles.add(AppProfile.TCAP);
        UserPrincipal userPrincipal = new UserPrincipal("137||tcap1@tpo||tiago.fernandes@parceiro.rigorcg.pt",
                roles,33L);
        userPrincipal.setOidNet("SC00010002");
        userPrincipal.setOidDealerParent("1");

        PVMRequestDTO pvmRequestDTO = PVMData.getPVMRequestDTO();



        List<String> notSendOid =  new ArrayList<>();
        notSendOid.add("1");
        notSendOid.add("100C");
        notSendOid.add("2");


        when(pvmMonthlyReportRepository.getPVM(any(), any()))
                .thenReturn(PVMData.getPVMGotoData().getPvmMonthlyReports());

        when(dealersUtils.getMapDealers(anyString()))
                .thenReturn(PVMData.getPVMGotoData().getMapDealers());

        when(pvmMonthlyReportRepository.getNotSendPVMOid(anyInt(), anyInt()))
                .thenReturn(notSendOid);

        when(lexusRetailerRepository.findAllByObjectIdNotIn(any())).thenReturn(PVMData.getLexusRetail());



        PVMGetDTO pvm = pvmService.getPVM(pvmRequestDTO, userPrincipal);

        assertEquals(1, pvm.getPvmMonthlyReports().get(0).getId());
        assertEquals(1, pvm.getPvmMonthlyReports().get(0).getYear());
        assertEquals(1, pvm.getPvmMonthlyReports().get(0).getMonth());
        assertEquals("SC00020001", pvm.getPvmMonthlyReports().get(0).getOidDealer());
        assertEquals("D", pvm.getMapDealers().get(0).getDealer().getDistrict());
        assertEquals("M", pvm.getMapDealers().get(0).getDealer().getMunicipality());
    }

    @Test
    void whenGetPVMDetailToyotaReturnInfo() {
        when(pvmCarmodelRepository.getCar())
                .thenReturn(PVMData.getPVMDetailData().getCar());

        when(pvmMonthlyReportRepository.findById(any()))
                .thenReturn(Optional.of(PVMData.getPVMDetailData().getMonthlyReport()));

        when(pvmMonthlyReportRepository.getSalesAndPlates(anyInt()))
                .thenReturn(PVMData.getPVMDetailData().getSalesAndPlates());


        PVMDetailDTO pvmDetail = pvmService.getPVMDetail(1, securityData.getUserPrincipal());

        assertEquals(93, pvmDetail.getCar().get(0).getId());
        assertEquals("teste PR COL", pvmDetail.getCar().get(0).getName());
        assertEquals("PASSAGEIROS", pvmDetail.getCar().get(0).getType());
        assertEquals("S", pvmDetail.getCar().get(0).getActive());
        assertEquals(436, pvmDetail.getMonthlyReport().getId());
        assertEquals(436, pvmDetail.getSalesAndPlates().get(0).getIdPvmMonthlyreport());
    }

    @Test
    void whenGetPVMDetailToyotaReturnNUllReport() {
        when(pvmCarmodelRepository.getCar())
                .thenReturn(PVMData.getPVMDetailData().getCar());

        when(pvmMonthlyReportRepository.findById(any()))
                .thenReturn(Optional.of(new PVMMonthlyReport()));

        when(pvmMonthlyReportRepository.getSalesAndPlates(anyInt()))
                .thenReturn(PVMData.getPVMDetailData().getSalesAndPlates());


        PVMDetailDTO pvmDetail = pvmService.getPVMDetail(1, securityData.getUserPrincipal());

        assertEquals(93, pvmDetail.getCar().get(0).getId());
        assertEquals("teste PR COL", pvmDetail.getCar().get(0).getName());
        assertEquals("PASSAGEIROS", pvmDetail.getCar().get(0).getType());
        assertEquals("S", pvmDetail.getCar().get(0).getActive());
        assertNull(pvmDetail.getMonthlyReport().getId());
        assertEquals(436, pvmDetail.getSalesAndPlates().get(0).getIdPvmMonthlyreport());
    }

    @Test
    void whenGetPVMDetailToyotaReturnEmptyReport() {
        when(pvmCarmodelRepository.getCar())
                .thenReturn(PVMData.getPVMDetailData().getCar());


        when(pvmMonthlyReportRepository.getSalesAndPlates(anyInt()))
                .thenReturn(PVMData.getPVMDetailData().getSalesAndPlates());


        PVMDetailDTO pvmDetail = pvmService.getPVMDetail(1, securityData.getUserPrincipal());

        assertEquals(93, pvmDetail.getCar().get(0).getId());
        assertEquals("teste PR COL", pvmDetail.getCar().get(0).getName());
        assertEquals("PASSAGEIROS", pvmDetail.getCar().get(0).getType());
        assertEquals("S", pvmDetail.getCar().get(0).getActive());
//        assertNull(436, pvmDetail.getMonthlyReport().getId());
        assertEquals(436, pvmDetail.getSalesAndPlates().get(0).getIdPvmMonthlyreport());
    }

    @Test
    void whenGetPVMDetailLexusReturnInfo() {
        Set<AppProfile> roles = new HashSet<>();
        roles.add(AppProfile.DEALER);
        UserPrincipal userPrincipal = new UserPrincipal("137||tcap1@tpo||tiago.fernandes@parceiro.rigorcg.pt",
                roles,2L);

        when(pvmCarmodelRepository.getCar())
                .thenReturn(PVMData.getPVMDetailData().getCar());

        when(pvmMonthlyReportRepository.findById(any()))
                .thenReturn(Optional.of(PVMData.getPVMDetailData().getMonthlyReport()));

        when(pvmMonthlyReportRepository.getSalesAndPlates(anyInt()))
                .thenReturn(PVMData.getPVMDetailData().getSalesAndPlates());


        PVMDetailDTO pvmDetail = pvmService.getPVMDetail(1, userPrincipal);

        assertEquals(93, pvmDetail.getCar().get(0).getId());
        assertEquals("teste PR COL", pvmDetail.getCar().get(0).getName());
        assertEquals("PASSAGEIROS", pvmDetail.getCar().get(0).getType());
        assertEquals("S", pvmDetail.getCar().get(0).getActive());
        assertEquals(436, pvmDetail.getMonthlyReport().getId());
        assertEquals(436, pvmDetail.getSalesAndPlates().get(0).getIdPvmMonthlyreport());
    }

    @Test
    void whenNewPVMThenThrow() {


        when(pvmMonthlyReportRepository.newPVM(anyInt(), anyInt(), anyString(), anyInt()))
                .thenThrow(RuntimeException.class);

        doNothing().when(pvmMonthlyReportDetailRepository).mergePVMMonthlyReportDetail(anyInt());

        when(pvmMonthlyReportRepository.save(any())).thenReturn(PVMData.newPVMData());

        UserPrincipal userPrincipal = securityData.getUserPrincipal();
        userPrincipal.setOidNet("1");
        userPrincipal.setOidDealerParent("1");

        assertThrows(CreatePVMException.class, ()-> pvmService.newPVM(userPrincipal, 1));


    }

    @Test
    void whenNewPVMThenReturnTrue() {
        when(pvmMonthlyReportRepository.newPVM(anyInt(), anyInt(), anyString(), anyInt()))
                .thenReturn(Optional.empty());

        doNothing().when(pvmMonthlyReportDetailRepository).mergePVMMonthlyReportDetail(anyInt());

        when(pvmMonthlyReportRepository.save(any())).thenReturn(PVMData.newPVMData());

        UserPrincipal userPrincipal = securityData.getUserPrincipal();
        userPrincipal.setOidNet("1");
        userPrincipal.setOidDealerParent("1");
        Integer success = pvmService.newPVM(userPrincipal, 1);

        assertEquals(success,1);
    }

    @Test
    void whenNewPVMThenReturnFalse() {

        when(pvmMonthlyReportRepository.newPVM(anyInt(), anyInt(), anyString(), anyInt()))
                .thenReturn(Optional.of(PVMData.newPVMData()));

        doNothing().when(pvmMonthlyReportDetailRepository).mergePVMMonthlyReportDetail(anyInt());

        UserPrincipal userPrincipal = securityData.getUserPrincipal();
        userPrincipal.setOidNet("1");
        userPrincipal.setOidDealerParent("1");
        Integer success = pvmService.newPVM(userPrincipal, 1);

        assertEquals(success,-1);

    }

    @Test
    void whenSaveReportThenUpdate() {
        ReportDetailRequestDto requestDto = ReportDetailRequestDto.builder()
                .contract("1")
                .s2("1")
                .s3("1")
                .p1("1")
                .p2("1")
                .build();

        List<ReportDetailRequestDto> reportDetailRequestDtoList = new ArrayList<>();

        reportDetailRequestDtoList.add(requestDto);

        UserPrincipal userPrincipal = securityData.getUserPrincipal();
        userPrincipal.setOidNet("1");
        userPrincipal.setOidDealerParent("1");

        doNothing().when(pvmMonthlyReportDetailRepository).updateReportDetail(any(), any(), any(),
                any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any());

        pvmService.saveReportDetail(userPrincipal, reportDetailRequestDtoList, "1");

        verify(pvmMonthlyReportDetailRepository, times(1)).updateReportDetail(any(), any(), any(),
                any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any());

    }

    @Test
    void whenSaveReportThenThrows() {
        ReportDetailRequestDto requestDto = ReportDetailRequestDto.builder()
                .contract("1")
                .s2("1")
                .s3("1")
                .p1("1")
                .p2("1")
                .build();

        List<ReportDetailRequestDto> reportDetailRequestDtoList = new ArrayList<>();

        reportDetailRequestDtoList.add(requestDto);

        UserPrincipal userPrincipal = securityData.getUserPrincipal();
        userPrincipal.setOidNet("1");
        userPrincipal.setOidDealerParent("1");

        doThrow(new CreatePVMException("Error test")).when(pvmMonthlyReportDetailRepository).updateReportDetail(any(), any(), any(),
                any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any());


        assertThrows(CreatePVMException.class ,()->pvmService.saveReportDetail(userPrincipal, reportDetailRequestDtoList, "1"));



    }

    @Test
    void whenSendReportThenUpdate() {

        PVMMonthlyReport build = PVMMonthlyReport.builder()
                .id(1)
                .year(1)
                .month(1)
                .oidDealer("SC00020001")
                .createdBy("205160085||tcap1@tpo")
                .available(1)
                .build();

        ReportDetailRequestDto requestDto = ReportDetailRequestDto.builder()
                .contract("1")
                .s2("1")
                .s3("1")
                .p1("1")
                .p2("1")
                .build();

        List<ReportDetailRequestDto> reportDetailRequestDtoList = new ArrayList<>();

        reportDetailRequestDtoList.add(requestDto);

        UserPrincipal userPrincipal = securityData.getUserPrincipal();
        userPrincipal.setOidNet("1");
        userPrincipal.setOidDealerParent("1");

        doNothing().when(pvmMonthlyReportDetailRepository).updateReportDetail(any(), any(), any(),
                any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any());

        when(pvmMonthlyReportRepository.findById(anyInt())).thenReturn(Optional.of(build));
        when(pvmMonthlyReportRepository.save(any())).thenReturn(build);

        pvmService.sendReportDetail(userPrincipal, reportDetailRequestDtoList, "1");


        verify(pvmMonthlyReportDetailRepository, times(1)).updateReportDetail(any(), any(), any(),
                any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any());
        verify(pvmMonthlyReportRepository, times(1)).findById(anyInt());
        verify(pvmMonthlyReportRepository, times(1)).save(any());

    }

    @Test
    void whenSendReportThenThrows() {
        ReportDetailRequestDto requestDto = ReportDetailRequestDto.builder()
                .contract("1")
                .s2("1")
                .s3("1")
                .p1("1")
                .p2("1")
                .build();

        List<ReportDetailRequestDto> reportDetailRequestDtoList = new ArrayList<>();

        reportDetailRequestDtoList.add(requestDto);

        UserPrincipal userPrincipal = securityData.getUserPrincipal();
        userPrincipal.setOidNet("1");
        userPrincipal.setOidDealerParent("1");

        doThrow(new CreatePVMException("Error test")).when(pvmMonthlyReportDetailRepository).updateReportDetail(any(), any(), any(),
                any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any());

        assertThrows(CreatePVMException.class ,()->pvmService.sendReportDetail(userPrincipal, reportDetailRequestDtoList, "1"));


    }

    @Test
    void whenProvidePVMToDealerThenReturnOk() throws SCErrorException {
        UserPrincipal userPrincipal = securityData.getUserPrincipal();
        userPrincipal.setOidNet("SC00010001");
        userPrincipal.setOidDealerParent("1");

        when(pvmMonthlyReportRepository.findById(anyInt())).thenReturn(Optional.of(PVMData.newPVMData()));

        when(dealersUtils.getDealerById(anyString(), anyString())).thenReturn(PVMData.getPVMGotoData().getMapDealers().get(0).getDealer());

        when(pvmMonthlyReportRepository.save(any())).thenReturn(PVMData.newPVMData());

        when(usrlogonUtil.getMailsForProfile(anyInt(), anyString(), anyString())).thenReturn("test@test.com");

        pvmService.providePVMToDealer(userPrincipal, "test", 1);

        verify(pvmMonthlyReportRepository, times(1)).findById(anyInt());
        verify(dealersUtils, times(1)).getDealerById(anyString(), anyString());
        verify(pvmMonthlyReportRepository, times(1)).save(any());
        verify(usrlogonUtil, times(1)).getMailsForProfile(anyInt(),anyString() ,anyString());

    }

    @Test
    void whenProvidePVMToDealerThenThrows() {
        UserPrincipal userPrincipal = securityData.getUserPrincipal();
        userPrincipal.setOidNet("SC00010001");
        userPrincipal.setOidDealerParent("1");

        when(pvmMonthlyReportRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(GetPVMException.class ,()->pvmService.providePVMToDealer(userPrincipal, "test", 1));


    }

    @Test
    void whenRequestToChangeThenReturnOk() throws SCErrorException {
        UserPrincipal userPrincipal = securityData.getUserPrincipal();
        userPrincipal.setOidNet("SC00010002");
        userPrincipal.setOidDealerParent("1");

        when(pvmMonthlyReportRepository.findById(anyInt())).thenReturn(Optional.of(PVMData.newPVMData()));

        when(dealersUtils.getDealerById(anyString(), anyString())).thenReturn(PVMData.getPVMGotoData().getMapDealers().get(0).getDealer());

        when(pvmMonthlyReportRepository.save(any())).thenReturn(PVMData.newPVMData());

        when(usrlogonUtil.getMailsForProfile(anyInt(), anyString(), anyString())).thenReturn("test@test.com");

        pvmService.requestToChange(userPrincipal, "test", "1");

        verify(pvmMonthlyReportRepository, times(1)).findById(anyInt());
        verify(dealersUtils, times(1)).getDealerById(anyString(), anyString());
        verify(pvmMonthlyReportRepository, times(1)).save(any());
        verify(usrlogonUtil, times(1)).getMailsForProfile(anyInt(),anyString() ,anyString());

    }

    @Test
    void whenRequestToChangeThenThrows() {
        UserPrincipal userPrincipal = securityData.getUserPrincipal();
        userPrincipal.setOidNet("SC00010001");
        userPrincipal.setOidDealerParent("1");

        when(pvmMonthlyReportRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(GetPVMException.class ,()->pvmService.requestToChange(userPrincipal, "test", "1"));


    }

    @Test
    void whenGetPVMExcelByMonth1MThenReturnInfo() throws SCErrorException {
        UserPrincipal userPrincipal = securityData.getUserPrincipal();
        userPrincipal.setOidNet("SC00010001");
        userPrincipal.setOidDealerParent("1");
        userPrincipal.setCaMember(false);

        PVMRequestDTO pvmRequestDTO = PVMRequestDTO.builder()
                .year(2023)
                .month(6)
                .oidNet("SC00010001")
                .build();

        HttpServletResponse response = new MockHttpServletResponse();

        when(pvm1MonthReport.execute(anyInt(), anyInt(), anyString(), anyBoolean())).thenReturn(new HSSFWorkbook());


        pvmService.getPVMExcelByMonth(pvmRequestDTO, "1MONTH",userPrincipal, response);

        verify(pvm1MonthReport, times(1)).execute(anyInt(), anyInt(), anyString(), anyBoolean());
    }


    @Test
    void whenGetPVMExcelByMonth1MLexusThenReturnInfo() throws SCErrorException {
        UserPrincipal userPrincipal = securityData.getUserPrincipal();
        userPrincipal.setOidNet("SC00010002");
        userPrincipal.setOidDealerParent("1");
        userPrincipal.setCaMember(false);

        PVMRequestDTO pvmRequestDTO = PVMRequestDTO.builder()
                .year(2023)
                .month(6)
                .oidNet("SC00010002")
                .build();

        HttpServletResponse response = new MockHttpServletResponse();

        when(pvm1MonthReport.execute(anyInt(), anyInt(), anyString(), anyBoolean())).thenReturn(new HSSFWorkbook());


        pvmService.getPVMExcelByMonth(pvmRequestDTO, "1MONTH",userPrincipal, response);

        verify(pvm1MonthReport, times(1)).execute(anyInt(), anyInt(), anyString(), anyBoolean());
    }

    @Test
    void whenGetPVMExcelByMonth3MThenReturnInfo() throws SCErrorException {
        UserPrincipal userPrincipal = securityData.getUserPrincipal();
        userPrincipal.setOidNet("SC00010001");
        userPrincipal.setOidDealerParent("1");
        userPrincipal.setCaMember(false);

        PVMRequestDTO pvmRequestDTO = PVMRequestDTO.builder()
                .year(2023)
                .month(6)
                .oidNet("SC00010001")
                .build();

        HttpServletResponse response = new MockHttpServletResponse();

        when(pvm3MonthReport.executeForToyota(anyInt(), anyInt(), anyBoolean())).thenReturn(new HSSFWorkbook());


        pvmService.getPVMExcelByMonth(pvmRequestDTO, "3MONTH",userPrincipal, response);

        verify(pvm3MonthReport, times(1)).executeForToyota(anyInt(), anyInt(), anyBoolean());
    }

    @Test
    void whenGetPVMExcelByMonth3MLexusThenReturnInfo() throws SCErrorException {
        UserPrincipal userPrincipal = securityData.getUserPrincipal();
        userPrincipal.setOidNet("SC00010002");
        userPrincipal.setOidDealerParent("1");
        userPrincipal.setCaMember(false);

        PVMRequestDTO pvmRequestDTO = PVMRequestDTO.builder()
                .year(2023)
                .month(6)
                .oidNet("SC00010002")
                .build();

        HttpServletResponse response = new MockHttpServletResponse();

        when(pvm3MonthReport.executeForLexus(anyInt(), anyInt(), anyBoolean())).thenReturn(new HSSFWorkbook());


        pvmService.getPVMExcelByMonth(pvmRequestDTO, "3MONTH",userPrincipal, response);

        verify(pvm3MonthReport, times(1)).executeForLexus(anyInt(), anyInt(), anyBoolean());
    }


    @Test
    void whenGetPVMExcelByMonth3MLexusThenThrows() throws SCErrorException {
        UserPrincipal userPrincipal = securityData.getUserPrincipal();
        userPrincipal.setOidNet("SC00010002");
        userPrincipal.setOidDealerParent("1");
        userPrincipal.setCaMember(false);

        PVMRequestDTO pvmRequestDTO = PVMRequestDTO.builder()
                .year(2023)
                .month(6)
                .oidNet("SC00010002")
                .build();

        HttpServletResponse response = new MockHttpServletResponse();

        when(pvm3MonthReport.executeForLexus(anyInt(), anyInt(), anyBoolean())).thenThrow(RuntimeException.class);


        assertThrows(RuntimeException.class, ()->pvmService.getPVMExcelByMonth(pvmRequestDTO, "3MONTH",userPrincipal, response));


    }

}
