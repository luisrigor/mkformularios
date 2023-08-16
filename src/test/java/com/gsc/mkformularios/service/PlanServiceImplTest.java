package com.gsc.mkformularios.service;

import com.gsc.mkformularios.config.datasource.toyota.DbContext;
import com.gsc.mkformularios.dto.PlanDTO;
import com.gsc.mkformularios.exceptions.FileUploadException;
import com.gsc.mkformularios.exceptions.GetPVMException;
import com.gsc.mkformularios.repository.toyota.PVMBudgetRepository;
import com.gsc.mkformularios.repository.toyota.PVMCarModelYearForecastRepository;
import com.gsc.mkformularios.repository.toyota.PVMCarmodelRepository;
import com.gsc.mkformularios.sample.data.provider.PVMData;
import com.gsc.mkformularios.sample.data.provider.SecurityData;
import com.gsc.mkformularios.security.UserPrincipal;
import com.gsc.mkformularios.service.impl.BudgetServiceImpl;
import com.gsc.mkformularios.service.impl.PlanServiceImpl;
import com.gsc.mkformularios.utils.DealersUtils;
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
import java.util.List;
import java.util.Vector;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
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

   /* @Test
    void whenUploadPlanThenReturnSaved() throws IOException {
        UserPrincipal userPrincipal = securityData.getUserPrincipal();
        userPrincipal.setOidNet("SC00010001");
        userPrincipal.setOidDealerParent("1");



        byte[] fileData = (
                "21,DYNA L,5,4,5,5,0,0,0,0,0,0,0,0 \n" +
                "97,JUAN,0,0,0,0,5,0,0,0,0,0,0,0 \n").getBytes();
        InputStream inputStream = new ByteArrayInputStream(fileData);
        MockMultipartFile file = new MockMultipartFile("file", "test.csv",
                "text/csv", inputStream);

        doNothing().when(pvmCarmodelYearForecastRepository).deleteCarModelByIdAndYear(anyInt(), anyInt());
        when(pvmCarmodelYearForecastRepository.save(any())).thenReturn(PVMData.getModelDTO().getForecast().get(0));

        List<String> response = planService.uploadPlan(file, "2023", userPrincipal);
        assertEquals("uploaded", response.get(0));

    }*/

    @Test
    void whenUploadPlanThenReturnEmpty() throws IOException {
        UserPrincipal userPrincipal = securityData.getUserPrincipal();
        userPrincipal.setOidNet("SC00010001");
        userPrincipal.setOidDealerParent("1");

        byte[] fileData = ("").getBytes();
        InputStream inputStream = new ByteArrayInputStream(fileData);
        MockMultipartFile file = new MockMultipartFile("file", "test.csv",
                "text/csv", inputStream);

        assertThrows(FileUploadException.class, ()->planService.uploadPlan(file, "2023", userPrincipal));
    }

    @Test
    void whenUploadPlanThenReturnError() throws IOException {
        UserPrincipal userPrincipal = securityData.getUserPrincipal();
        userPrincipal.setOidNet("SC00010001");
        userPrincipal.setOidDealerParent("1");

        byte[] fileData = (
                        "21;DYNA L;5;4;5;5;0;0;0;0;0;0;0;0 \n" +
                        "97;JUAN;0;0;0;0;5;0;0;0;0;0;0;0 \n").getBytes(StandardCharsets.ISO_8859_1);
        InputStream inputStream = new ByteArrayInputStream(fileData);
        MockMultipartFile file = new MockMultipartFile("file", "test.csv",
                "text/csv", inputStream);

        doNothing().when(pvmCarmodelYearForecastRepository).deleteCarModelByIdAndYear(anyInt(), anyInt());
        when(pvmCarmodelYearForecastRepository.save(any())).thenThrow(RuntimeException.class);

        List<String> response = planService.uploadPlan(file, "2023", userPrincipal);
        assertEquals("Error saving JUAN for month 1", response.get(0));
    }

    @Test
    void whenDownloadPlanThenReturnInfo() throws SCErrorException {
        UserPrincipal userPrincipal = securityData.getUserPrincipal();
        userPrincipal.setOidNet("SC00010001");
        userPrincipal.setOidDealerParent("1");
        HttpServletResponse response = new MockHttpServletResponse();


        when(pvmCarmodelRepository.getCar())
                .thenReturn(PVMData.getPlanDto().getCar());

        when(pvmCarmodelYearForecastRepository.findAllByYear(anyInt()))
                .thenReturn(PVMData.getPlanDto().getForecast());


        List<String[]> plan = planService.downloadPlan("2023", userPrincipal, response);
        assertEquals("N", plan.get(1)[1]);
        assertEquals("1", plan.get(1)[2]);
        assertEquals("0", plan.get(1)[3]);
        assertEquals("0", plan.get(1)[4]);
    }

    @Test
    void whenDownloadPlanThenThrows() {
        UserPrincipal userPrincipal = securityData.getUserPrincipal();
        userPrincipal.setOidNet("1");
        userPrincipal.setOidDealerParent("1");
        HttpServletResponse response = new MockHttpServletResponse();


        when(pvmCarmodelRepository.getCar())
                .thenThrow(GetPVMException.class);

        assertThrows(GetPVMException.class, ()-> planService.downloadPlan("2023", userPrincipal, response));



    }

}
