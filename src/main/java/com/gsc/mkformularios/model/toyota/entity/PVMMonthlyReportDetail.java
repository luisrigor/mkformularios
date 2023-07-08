package com.gsc.mkformularios.model.toyota.entity;

import com.gsc.mkformularios.model.toyota.CompositeReportDetail;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@IdClass(CompositeReportDetail.class)
@Table(name = "PVM_MONTHLYREPORTDETAIL")
public class PVMMonthlyReportDetail {

    @Id
    @Column(name = "ID_PVM_MONTHLYREPORT")
    private Integer monthlyReportId;
    @Id
    @Column(name = "ID_PVM_CARMODEL")
    private Integer carModelId;
    @Column(name = "SALES_VALUE")
    private Integer salesValue;
    @Column(name = "PLATES_VALUE")
    private Integer platesValue;
    @Column(name = "CREATED_BY")
    private String createdBy;
    @Column(name = "DT_CREATED")
    private LocalDateTime dtCreated;
    @Column(name = "CHANGED_BY")
    private String changedBy;
    @Column(name = "DT_CHANGED")
    private LocalDateTime dtChanged;
    @Column(name = "SALES_VALUE2")
    private Integer salesValue2;
    @Column(name = "SALES_VALUE3")
    private Integer salesValue3;
    @Column(name = "PLATES_VALUE2")
    private Integer platesValue2;
    @Column(name = "PLATES_VALUE3")
    private Integer platesValue3;
    @Column(name = "CONTRACTS")
    private Integer contracts;
    @Column(name = "VDVC2")
    private Integer vdvc2;
    @Column(name = "VDVC3")
    private Integer vdvc3;
    @Column(name = "VDVC")
    private Integer vdvc;


}
