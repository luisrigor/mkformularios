package com.gsc.mkformularios.model.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "PVM_MONTHLYREPORTDETAIL")
public class PVMMonthlyReportDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PVM_MONTHLYREPORT")
    private Integer id;

    @Column(name = "SALES_VALUE")
    private Integer salesValue;
}
