package com.gsc.mkformularios.model.toyota.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "PVM_BUDGET")
public class PVMBudget {

    @Id
    @Column(name = "OID_DEALER")
    private String oidDealer;
    private Integer year;
    private Integer month;
    @Column(name = "ID_PVM_CARMODEL")
    private Integer idPvmCarModel;
    private Integer plates;
    @Column(name = "SUB_DEALER")
    private Integer subDealer;

}
