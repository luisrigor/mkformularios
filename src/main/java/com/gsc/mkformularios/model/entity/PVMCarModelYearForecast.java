package com.gsc.mkformularios.model.entity;

import lombok.*;

import javax.persistence.*;

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

}
