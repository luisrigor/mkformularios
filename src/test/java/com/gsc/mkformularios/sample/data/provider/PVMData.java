package com.gsc.mkformularios.sample.data.provider;

import com.gsc.mkformularios.dto.*;
import com.gsc.mkformularios.model.toynet.entity.LexusRetailer;
import com.gsc.mkformularios.model.toyota.entity.PVMBudget;
import com.gsc.mkformularios.model.toyota.entity.PVMCarModelYearForecast;
import com.gsc.mkformularios.model.toyota.entity.PVMCarmodel;
import com.gsc.mkformularios.model.toyota.entity.PVMMonthlyReport;
import com.rg.dealer.Dealer;
import com.gsc.mkformularios.dto.PVMDetailDTO;
import com.gsc.mkformularios.dto.SalesPlates;
import com.gsc.mkformularios.model.toyota.entity.PVMCarmodel;
import com.gsc.mkformularios.model.toyota.entity.PVMMonthlyReport;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PVMData {

    public static PVMDetailDTO getPVMDetailData(){
        List<PVMCarmodel> car = new ArrayList<>();
        PVMCarmodel pvmCarmodel1 = PVMCarmodel.builder()
                .id(93)
                .name("teste PR COL")
                .type("PASSAGEIROS")
                .active("S")
                .build();

        PVMCarmodel pvmCarmodel2 = PVMCarmodel.builder()
                .id(91)
                .name("Modelo Testes Form01")
                .type("PASSAGEIROS")
                .active("S")
                .build();

        car.add(pvmCarmodel1);
        car.add(pvmCarmodel2);

        PVMMonthlyReport monthlyReport = PVMMonthlyReport.builder()
                .id(436)
                .year(2023)
                .month(6)
                .oidDealer("SC00020001")
                .build();

        List<SalesPlates> salesPlates = new ArrayList<>();

        SalesPlates salesPlates1 = SalesPlates.builder()
                .idPvmMonthlyreport(436)
                .year(2023)
                .month(6)
                .oidDealer("SC00020001")
                .brandId(21)
                .build();

        SalesPlates salesPlates2 = SalesPlates.builder()
                .idPvmMonthlyreport(436)
                .year(2023)
                .month(6)
                .oidDealer("SC00020001")
                .brandId(78)
                .build();

        salesPlates.add(salesPlates1);
        salesPlates.add(salesPlates2);

        return PVMDetailDTO.builder()
                .car(car)
                .monthlyReport(monthlyReport)
                .salesAndPlates(salesPlates)
                .build();
    }

    public static PVMGetDTO getPVMGotoData() {

        List<PVMMonthlyReport> pvmMonthlyReports = new ArrayList<>();

        PVMMonthlyReport build = PVMMonthlyReport.builder()
                .id(1)
                .year(1)
                .month(1)
                .oidDealer("SC00020001")
                .createdBy("205160085||tcap1@tpo")
                .available(1)
                .build();

        pvmMonthlyReports.add(build);

        List<DealerDTO> mapDealers = new ArrayList<>();
        Dealer dealer = new Dealer();
        dealer.setDistrict("D");
        dealer.setMunicipality("M");
        dealer.setCp3(1);
        dealer.setEmail("a@a");
        dealer.setResp("R");
        dealer.setCp4(2);
        DealerDTO dealerDTO = new DealerDTO("1", dealer);

        mapDealers.add(dealerDTO);



        List<DealerDTO> notSendPVM = new ArrayList<>();





        return PVMGetDTO.builder()
                .pvmMonthlyReports(pvmMonthlyReports)
                .mapDealers(mapDealers)
                .notSendPVM(notSendPVM)
                .build();


    }

    public static List<LexusRetailer> getLexusRetail() {
        List<LexusRetailer> lexusRetailers = new ArrayList<>();
        LexusRetailer lexusRetailer1 = LexusRetailer.builder()
                .aftersalesCode("1")
                .objectId("1")
                .cp4(1)
                .cp3(1)
                .cpExt("1")
                .changedBy("user")
                .comercialName("1")
                .build();

        lexusRetailers.add(lexusRetailer1);
        return lexusRetailers;

    }

    public static PVMMonthlyReport newPVMData() {

        return PVMMonthlyReport.builder()
                .id(1)
                .reason("R")
                .available(1)
                .createdBy("user")
                .oidDealer("SC000")
                .subDealer(1)
                .year(2022)
                .month(1)
                .build();
    }

    public static PVMRequestDTO getPVMRequestDTO() {
        return PVMRequestDTO.builder()
                .year(2023)
                .month(6)
                .build();
    }

    public static GoToModelDTO getModelDTO() {
        PVMCarmodel pvmCarmodel = PVMCarmodel.builder()
                .type("A")
                .active("A")
                .name("N")
                .changedBy("user")
                .build();

        PVMCarModelYearForecast forecast = PVMCarModelYearForecast.builder()
                .forecast(1)
                .idCarModel(1)
                .id(1)
                .month(1)
                .build();

        PVMCarmodel car = PVMCarmodel.builder()
                .type("A")
                .active("A")
                .name("N")
                .changedBy("user")
                .build();

        return  GoToModelDTO.builder()
                .carModel(pvmCarmodel)
                .forecast(Arrays.asList(forecast))
                .car(Arrays.asList(car))
                .build();
    }

    public static ModelDTO getSModelDTO() {
        return ModelDTO.builder()
                .model("m")
                .type("t")
                .from(new Timestamp(System.currentTimeMillis()).toLocalDateTime().toLocalDate())
                .to(new Timestamp(System.currentTimeMillis()).toLocalDateTime().toLocalDate())
                .order(1)
                .build();
    }

    public static BudgetDTO getBudgetDto() {
        List<PVMBudgetDTO> budgets = new ArrayList<>();

        PVMBudgetDTO pvmBudget1 = PVMBudgetDTO.builder()
                .idPvmCarModel(1)
                .year(1)
                .month(1)
                .oidDealer("1")
                .plates(1)
                .build();

        budgets.add(pvmBudget1);

        List<String[]> dealer = new ArrayList<>();

        dealer.add(new String[]{"SC00020003", "61", "61","0"});



        return BudgetDTO.builder()
                .budgetDealers(Arrays.asList(new BudgetDealerDTO(dealer.get(0), budgets)))
                .year(2023)
                .build();
    }

    public static List<PVMBudgetDTO> getBudgetDealerDto() {
        List<PVMBudgetDTO> budgets = new ArrayList<>();

        PVMBudgetDTO pvmBudget1 = PVMBudgetDTO.builder()
                .idPvmCarModel(1)
                .year(1)
                .month(1)
                .oidDealer("1")
                .plates(1)
                .build();

        PVMBudgetDTO pvmBudget2 = PVMBudgetDTO.builder()
                .idPvmCarModel(2)
                .year(2)
                .month(2)
                .oidDealer("2")
                .plates(2)
                .build();

        PVMBudgetDTO pvmBudget3 = PVMBudgetDTO.builder()
                .idPvmCarModel(1)
                .year(2)
                .month(2)
                .oidDealer("SC00020001")
                .subDealer(1)
                .plates(2)
                .build();

        budgets.add(pvmBudget1);
        budgets.add(pvmBudget2);
        budgets.add(pvmBudget3);



        return budgets;
    }

    public static List<MapTypesDTO> getMapTypes() {
        List<MapTypesDTO> mapTypesDTOS = new ArrayList<>();

        MapTypesDTO mapTypesDTO1 = MapTypesDTO.builder()
                .id(1)
                .type("PASS")
                .build();

        mapTypesDTOS.add(mapTypesDTO1);

        return mapTypesDTOS;
    }

    public static PlanDTO getPlanDto() {
        List<PVMCarmodel> car = new ArrayList<>();
        List<PVMCarModelYearForecast>  forecast = new ArrayList<>();

        PVMCarmodel carmodel = PVMCarmodel.builder()
                .type("A")
                .active("A")
                .name("N")
                .changedBy("user")
                .id(1)
                .build();

        car.add(carmodel);

        PVMCarModelYearForecast forecastmodel = PVMCarModelYearForecast.builder()
                .forecast(1)
                .idCarModel(1)
                .id(1)
                .month(1)
                .build();

        forecast.add(forecastmodel);



        return PlanDTO.builder()
                .car(car)
                .forecast(forecast)
                .build();
    }
}
