package com.gsc.mkformularios.model.toyota.entity;

import com.gsc.mkformularios.dto.MapTypesDTO;
import com.gsc.mkformularios.dto.PVMBudgetDTO;
import com.gsc.mkformularios.model.toyota.CompositePVMBudget;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@IdClass(CompositePVMBudget.class)
@SqlResultSetMapping(
        name = "GetBudgetYear",
        classes = {
                @ConstructorResult(
                        targetClass = PVMBudgetDTO.class,
                        columns = {
                                @ColumnResult(name = "OID_DEALER", type = String.class),
                                @ColumnResult(name = "YEAR", type = Integer.class),
                                @ColumnResult(name = "MONTH", type = Integer.class),
                                @ColumnResult(name = "ID_PVM_CARMODEL", type = Integer.class),
                                @ColumnResult(name = "PLATES", type = Integer.class),
                                @ColumnResult(name = "SUB_DEALER", type = Integer.class),
                        }
                )
        }
)
@Table(name = "PVM_BUDGET")
public class PVMBudget {

    @Id
    @Column(name = "OID_DEALER")
    private String oidDealer;
    @Id
    private Integer year;
    @Id
    private Integer month;
    @Id
    @Column(name = "ID_PVM_CARMODEL")
    private Integer idPvmCarModel;
    private Integer plates;
    @Id
    @Column(name = "SUB_DEALER")
    private Integer subDealer;

}
