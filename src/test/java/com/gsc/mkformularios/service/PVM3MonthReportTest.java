package com.gsc.mkformularios.service;

import com.google.gson.reflect.TypeToken;
import com.gsc.mkformularios.config.datasource.toyota.DbContext;
import com.gsc.mkformularios.dto.PVMCarmodelForecast;
import com.gsc.mkformularios.dto.RetailDealerDTO;
import com.gsc.mkformularios.model.toyota.entity.PVMCarmodel;
import com.gsc.mkformularios.repository.toynet.LexusRetailerRepository;
import com.gsc.mkformularios.repository.toynet.ToyotaRetailerRepository;
import com.gsc.mkformularios.repository.toyota.PVMCarmodelRepository;
import com.gsc.mkformularios.repository.toyota.PVMMonthlyReportRepository;
import com.gsc.mkformularios.sample.data.provider.ReadJsonTest;
import com.gsc.mkformularios.sample.data.provider.SecurityData;
import com.gsc.mkformularios.service.impl.pvm.PVM1MonthReport;
import com.gsc.mkformularios.service.impl.pvm.PVM3MonthReport;
import com.sc.commons.exceptions.SCErrorException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ActiveProfiles(SecurityData.ACTIVE_PROFILE)
public class PVM3MonthReportTest {

    @Mock
    private DbContext dbContext;
    @Mock
    private LexusRetailerRepository lexusRetailerRepository;
    @Mock
    private ToyotaRetailerRepository toyotaRetailerRepository;
    @Mock
    private PVMCarmodelRepository pvmCarmodelRepository;
    @Mock
    private PVMMonthlyReportRepository pvmMonthlyReportRepository;

    @InjectMocks
    private PVM3MonthReport pvm3MonthReport;

    private ReadJsonTest readJ;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        readJ = new ReadJsonTest();
    }

    @Test
    void whenCreateExcelWbForToyotaThenReturnExcelInfo() throws IOException, URISyntaxException {
        URI uriForecast = this.getClass().getResource("/exportexcel/vecForecasts.json").toURI();
        URI uriDealers = this.getClass().getResource("/exportexcel/vecRetailerDealers.json").toURI();
        URI uriCar = this.getClass().getResource("/exportexcel/vecPVMCarModel.json").toURI();
        URI uriRs1_1 = this.getClass().getResource("/exportexcel/rs1_1.json").toURI();
        List<PVMCarmodelForecast> vecForecasts = readJ.readJson(uriForecast.getPath(), new TypeToken<List<PVMCarmodelForecast>>() {}.getType());
        List<RetailDealerDTO> vecRetailerDealers = readJ.readJson(uriDealers.getPath(), new TypeToken<List<RetailDealerDTO>>() {}.getType());
        List<String[]> vecPVMCarModel = readJ.readJson(uriCar.getPath(), new TypeToken<List<String[]>>() {}.getType());
        List<Map<String, Object>> Rs1_1 = readJ.readJson(uriRs1_1.getPath(), new TypeToken<List<Map<String, Object>>>() {}.getType());

        List<PVMCarmodel> pvmCarmodels = new ArrayList<>();
        for (String[] current: vecPVMCarModel) {
            PVMCarmodel currentCar = PVMCarmodel.builder()
                    .type(current[0])
                    .id(Integer.valueOf(current[2]))
                    .name(current[1])
                    .build();

            pvmCarmodels.add(currentCar);
        }


        when(toyotaRetailerRepository.findAllOrderByDesig()).thenReturn(vecRetailerDealers);
        when(pvmCarmodelRepository.getPVMCarModels(any(), any())).thenReturn(pvmCarmodels);
        when(pvmMonthlyReportRepository.getPVMCarModelsForecasts(any(), any(), any())).thenReturn(vecForecasts);
        when(pvmMonthlyReportRepository.getPVMMonthReportPlates(any())).thenReturn(Rs1_1);


        HSSFWorkbook workBook = pvm3MonthReport.executeForToyota(2023, 6, false);


    }

    @Test
    void whenCreateExcelWbForLexusThenReturnExcelInfo() throws IOException, URISyntaxException {
        URI uriForecast = this.getClass().getResource("/exportexcel/vecForecasts.json").toURI();
        URI uriDealers = this.getClass().getResource("/exportexcel/vecRetailerDealers.json").toURI();
        URI uriCar = this.getClass().getResource("/exportexcel/vecPVMCarModel.json").toURI();
        URI uriRs1_1 = this.getClass().getResource("/exportexcel/rs1_1.json").toURI();
        List<PVMCarmodelForecast> vecForecasts = readJ.readJson(uriForecast.getPath(), new TypeToken<List<PVMCarmodelForecast>>() {}.getType());
        List<RetailDealerDTO> vecRetailerDealers = readJ.readJson(uriDealers.getPath(), new TypeToken<List<RetailDealerDTO>>() {}.getType());
        List<String[]> vecPVMCarModel = readJ.readJson(uriCar.getPath(), new TypeToken<List<String[]>>() {}.getType());
        List<Map<String, Object>> Rs1_1 = readJ.readJson(uriRs1_1.getPath(), new TypeToken<List<Map<String, Object>>>() {}.getType());

        List<PVMCarmodel> pvmCarmodels = new ArrayList<>();
        for (String[] current: vecPVMCarModel) {
            PVMCarmodel currentCar = PVMCarmodel.builder()
                    .type(current[0])
                    .id(Integer.valueOf(current[2]))
                    .name(current[1])
                    .build();

            pvmCarmodels.add(currentCar);
        }


        when(toyotaRetailerRepository.findAllOrderByDesig()).thenReturn(vecRetailerDealers);
        when(pvmCarmodelRepository.getPVMCarModels(any(), any())).thenReturn(pvmCarmodels);
        when(pvmMonthlyReportRepository.getPVMCarModelsForecasts(any(), any(), any())).thenReturn(vecForecasts);
        when(pvmMonthlyReportRepository.getPVMMonthReportPlates(any())).thenReturn(Rs1_1);


        HSSFWorkbook workBook = pvm3MonthReport.executeForLexus(2023, 6, false);


    }


}
