package com.gsc.mkformularios.dto;


import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesPlates {

    private Integer idPvmMonthlyreport;
    private Integer year;
    private Integer month;
    private String oidDealer;
    private Integer brandId;
    private LocalDate dtSelect;
    private LocalDate dtFrom;
    private LocalDate dtTo;
    private Integer salesValuep2;
    private Integer salesValuep1;
    private Integer salesValue;
    private Integer salesValuep3;
    private Integer salesValue2;
    private Integer salesValue3;
    private Integer platesValuep2;
    private Integer platesValuep1;
    private Integer platesValue;
    private Integer platesValuep3;
    private Integer platesValue2;
    private Integer platesValue3;
    private Integer contracts;
    private Integer budget;
    private Integer vdvcp2;
    private Integer vdvcp1;
    private Integer vdvc;
    private Integer vdvcp3;
    private Integer vdvc2;
    private Integer vdvc3;

}
