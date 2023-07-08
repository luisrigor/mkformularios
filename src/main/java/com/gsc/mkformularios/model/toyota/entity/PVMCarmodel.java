package com.gsc.mkformularios.model.toyota.entity;


import com.gsc.mkformularios.dto.MapTypesDTO;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@SqlResultSetMapping(
        name = "GetCarTypesMapping",
        classes = {
                @ConstructorResult(
                        targetClass = MapTypesDTO.class,
                        columns = {
                                @ColumnResult(name = "ID", type = Integer.class),
                                @ColumnResult(name = "TYPE", type = String.class)
                        }
                )
        }
)
@Table(name = "PVM_CARMODEL")
public class PVMCarmodel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String type;
    private String active;
    @Column(name = "created_by")
    private String createdBy;
    @Column(name = "dt_created")
    private LocalDateTime dtCreated;
    @Column(name = "changed_by")
    private String changedBy;
    @Column(name = "dt_changed")
    private LocalDateTime dtChanged;
    @Column(name = "dt_from")
    private LocalDate dtFrom;
    @Column(name = "dt_to")
    private LocalDate dtTo;
    @Column(name = "export_order")
    private Integer exportOrder;
}
