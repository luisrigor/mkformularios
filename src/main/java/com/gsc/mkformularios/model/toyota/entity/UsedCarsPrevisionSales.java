package com.gsc.mkformularios.model.toyota.entity;

import com.gsc.mkformularios.model.toyota.CompositeReportDetail;
import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@IdClass(CompositeReportDetail.class)
@Table(name = "TVC_USED_CARS_PREVISION_SALES")
public class UsedCarsPrevisionSales {

    public static final String STATUS_OPEN			= "Aberto";
    public static final String STATUS_CLOSE			= "Fechado";
    public static final String PREVISION_TYPE_ANUAL	= "Anual";
    public static final String PREVISION_TYPE_MENSAL= "Mensal";
    @Id
    @Column(name = "ID")
    protected Integer ivId;
    @Column(name = "OID_DEALER")
    protected String ivOidDealer;
    @Column(name = "YEAR")
    protected Integer ivYear;
    @Column(name = "MONTH")
    protected Integer ivMonth;
    @Column(name = "PREVISION_TYPE")
    protected String ivPrevisionType;
    @Column(name = "STATUS")
    protected String ivStatus;
    protected Integer ivPrevisionTvc;
    protected Integer ivPrevisionSn;
    protected String ivCreatedBy;
    protected Timestamp ivDtCreated;
    protected String ivChangedBy;
    protected Timestamp ivDtChanged;
}
