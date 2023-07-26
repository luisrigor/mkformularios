package com.gsc.mkformularios.sample.data.provider;

import com.gsc.mkformularios.dto.PVMDetailDTO;
import com.gsc.mkformularios.dto.SalesPlates;
import com.gsc.mkformularios.model.toyota.entity.PVMCarmodel;
import com.gsc.mkformularios.model.toyota.entity.PVMMonthlyReport;

import java.util.ArrayList;
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
}
