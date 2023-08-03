package com.gsc.mkformularios.model.toyota.entity;


import com.gsc.mkformularios.dto.SalesPlates;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SqlResultSetMapping(
        name = "SalesPlatesMapping",
        classes = {
                @ConstructorResult(
                        targetClass = SalesPlates.class,
                        columns = {
                                @ColumnResult(name = "ID_PVM_MONTHLYREPORT", type = Integer.class),
                                @ColumnResult(name = "YEAR", type = Integer.class),
                                @ColumnResult(name = "MONTH", type = Integer.class),
                                @ColumnResult(name = "OID_DEALER", type = String.class),
                                @ColumnResult(name = "BRAND_ID", type = Integer.class),
                                @ColumnResult(name = "DT_SELECT", type = LocalDate.class),
                                @ColumnResult(name = "DT_FROM", type = LocalDate.class),
                                @ColumnResult(name = "DT_TO", type = LocalDate.class),
                                @ColumnResult(name = "SALES_VALUEP2", type = Integer.class),
                                @ColumnResult(name = "SALES_VALUEP1", type = Integer.class),
                                @ColumnResult(name = "SALES_VALUE", type = Integer.class),
                                @ColumnResult(name = "SALES_VALUEP3", type = Integer.class),
                                @ColumnResult(name = "SALES_VALUE2", type = Integer.class),
                                @ColumnResult(name = "SALES_VALUE3", type = Integer.class),
                                @ColumnResult(name = "PLATES_VALUEP2", type = Integer.class),
                                @ColumnResult(name = "PLATES_VALUEP1", type = Integer.class),
                                @ColumnResult(name = "PLATES_VALUE", type = Integer.class),
                                @ColumnResult(name = "PLATES_VALUEP3", type = Integer.class),
                                @ColumnResult(name = "PLATES_VALUE2", type = Integer.class),
                                @ColumnResult(name = "PLATES_VALUE3", type = Integer.class),
                                @ColumnResult(name = "CONTRACTS", type = Integer.class),
                                @ColumnResult(name = "BUDGET", type = Integer.class),
                                @ColumnResult(name = "VDVCP2", type = Integer.class),
                                @ColumnResult(name = "VDVCP1", type = Integer.class),
                                @ColumnResult(name = "VDVC", type = Integer.class),
                                @ColumnResult(name = "VDVCP3", type = Integer.class),
                                @ColumnResult(name = "VDVC2", type = Integer.class),
                                @ColumnResult(name = "VDVC3", type = Integer.class)
                        }
                )
        }
)
@Entity
@Table(name = "PVM_MONTHLYREPORT")
public class PVMMonthlyReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer year;
    private Integer month;
    @Column(name = "dt_availability")
    private LocalDateTime dtAvailability;
    @Column(name = "oid_dealer")
    private String oidDealer;
    @Column(name = "created_by")
    private String createdBy;
    @Column(name = "dt_created")
    private LocalDateTime dtCreated;
    private Integer available;
    @Column(name = "REASON")
    private String reason;
    @Column(name = "sub_dealer")
    private Integer subDealer;

}
