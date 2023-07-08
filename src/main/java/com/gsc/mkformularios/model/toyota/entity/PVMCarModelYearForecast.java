package com.gsc.mkformularios.model.toyota.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "PVM_CARMODEL_YEAR_FORECASTS")
public class PVMCarModelYearForecast {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "ID_CARMODEL")
    private Integer idCarModel;
    @Column(name = "YEAR")
    private Integer year;
    @Column(name = "MONTH")
    private Integer month;
    @Column(name = "FORECAST")
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
