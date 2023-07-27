package com.gsc.mkformularios.dto;

import com.gsc.mkformularios.dto.MapTypesDTO;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PVMCarmodelForecast {


    private Integer id;
    private String name;
    private String type;
    private String active;
    private String createdBy;
    private LocalDateTime dtCreated;
    private String changedBy;
    private LocalDateTime dtChanged;
    private LocalDate dtFrom;
    private LocalDate dtTo;
    private Integer exportOrder;
    private Integer forecast;
}
