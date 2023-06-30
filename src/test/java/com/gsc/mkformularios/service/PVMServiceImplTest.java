package com.gsc.mkformularios.service;


import com.gsc.mkformularios.config.datasource.toyota.DbContext;
import com.gsc.mkformularios.constants.AppProfile;
import com.gsc.mkformularios.constants.api.PVMEnpoints;
import com.gsc.mkformularios.dto.PVMDetailDTO;
import com.gsc.mkformularios.dto.PVMGetDTO;
import com.gsc.mkformularios.dto.PVMRequestDTO;
import com.gsc.mkformularios.exceptions.CreatePVMException;
import com.gsc.mkformularios.exceptions.GetPVMException;
import com.gsc.mkformularios.model.toyota.entity.PVMMonthlyReport;
import com.gsc.mkformularios.repository.toynet.LexusRetailerRepository;
import com.gsc.mkformularios.repository.toynet.ToyotaRetailerRepository;
import com.gsc.mkformularios.repository.toyota.PVMCarmodelRepository;
import com.gsc.mkformularios.repository.toyota.PVMMonthlyReportDetailRepository;
import com.gsc.mkformularios.repository.toyota.PVMMonthlyReportRepository;
import com.gsc.mkformularios.sample.data.provider.PVMData;
import com.gsc.mkformularios.sample.data.provider.SecurityData;
import com.gsc.mkformularios.security.UserPrincipal;
import com.gsc.mkformularios.service.impl.PVMServiceImpl;
import com.gsc.mkformularios.utils.DealersUtils;
import com.sc.commons.exceptions.SCErrorException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
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



        List<String> notSendOid = Arrays.asList("1", "100C", "2");

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



        List<String> notSendOid = Arrays.asList("1", "100C", "2");

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
        Boolean success = pvmService.newPVM(userPrincipal, 1);

        assertTrue(success);
    }

    @Test
    void whenNewPVMThenReturnFalse() {

        when(pvmMonthlyReportRepository.newPVM(anyInt(), anyInt(), anyString(), anyInt()))
                .thenReturn(Optional.of(PVMData.newPVMData()));

        doNothing().when(pvmMonthlyReportDetailRepository).mergePVMMonthlyReportDetail(anyInt());

        UserPrincipal userPrincipal = securityData.getUserPrincipal();
        userPrincipal.setOidNet("1");
        userPrincipal.setOidDealerParent("1");
        Boolean success = pvmService.newPVM(userPrincipal, 1);

        assertFalse(success);
    }





}
