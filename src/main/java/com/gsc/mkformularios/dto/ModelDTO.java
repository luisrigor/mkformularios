package com.gsc.mkformularios.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ModelDTO {
    private String model;
    private String type;
    private LocalDate from;
    private LocalDate to;
    private int order;
}
