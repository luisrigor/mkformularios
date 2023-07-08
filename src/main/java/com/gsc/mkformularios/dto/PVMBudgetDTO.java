package com.gsc.mkformularios.dto;

import lombok.*;



@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PVMBudgetDTO {


    private String oidDealer;
    private Integer year;
    private Integer month;
    private Integer idPvmCarModel;
    private Integer plates;
    private Integer subDealer;
}
