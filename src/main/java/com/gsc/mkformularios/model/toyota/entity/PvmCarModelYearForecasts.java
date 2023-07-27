package com.gsc.mkformularios.model.toyota.entity;


import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "PVM_CARMODEL_YEAR_FORECASTS", schema = "DB2INST1")
public class PvmCarModelYearForecasts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "ID_CARMODEL")
    private Integer carModelId;

    @Column
    private Integer year;

    @Column
    private Integer month;

    @Column
    private Integer forecast;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "DT_CREATED")
    private LocalDateTime dtCreated;

    @Column(name = "CHANGED_BY")
    private String changedBy;

    @Column(name = "DT_CHANGED")
    private LocalDateTime dtChanged;
}
