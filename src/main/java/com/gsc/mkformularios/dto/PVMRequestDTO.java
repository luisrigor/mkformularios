package com.gsc.mkformularios.dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PVMRequestDTO {

    private int idPVM;
    private int year;
    private int month;
    private String cancelReasons;
}
