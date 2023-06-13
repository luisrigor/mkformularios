package com.gsc.mkformularios.service;


import com.gsc.mkformularios.config.datasource.DbContext;
import com.gsc.mkformularios.constants.AppProfile;
import com.gsc.mkformularios.dto.PVMDetailDTO;
import com.gsc.mkformularios.model.entity.PVMMonthlyReport;
import com.gsc.mkformularios.repository.PVMCarmodelRepository;
import com.gsc.mkformularios.repository.PVMMonthlyReportRepository;
import com.gsc.mkformularios.sample.data.provider.PVMData;
import com.gsc.mkformularios.sample.data.provider.SecurityData;
import com.gsc.mkformularios.security.UserPrincipal;
import com.gsc.mkformularios.service.impl.PVMServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ActiveProfiles(SecurityData.ACTIVE_PROFILE)
public class PVMServiceImplTest {

    @Mock
    private DbContext dbContext;
    @Mock
    private PVMCarmodelRepository pvmCarmodelRepository;
    @Mock
    private PVMMonthlyReportRepository pvmMonthlyReportRepository;

    @InjectMocks
    private PVMServiceImpl pvmService;

    private SecurityData securityData;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        securityData = new SecurityData();
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
        roles.add(AppProfile.APPROVAL_MANAGER);
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


}
