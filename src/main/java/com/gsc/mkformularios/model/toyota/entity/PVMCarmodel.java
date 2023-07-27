package com.gsc.mkformularios.model.toyota.entity;


import com.gsc.mkformularios.dto.MapTypesDTO;
import com.gsc.mkformularios.dto.PVMCarmodelForecast;

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
@SqlResultSetMapping(
        name = "GetCarModelsForecastMapping",
        classes = {
                @ConstructorResult(
                        targetClass = PVMCarmodelForecast.class,
                        columns = {
                                @ColumnResult(name = "ID", type = Integer.class),
                                @ColumnResult(name = "NAME", type = String.class),
                                @ColumnResult(name = "TYPE", type = String.class),
                                @ColumnResult(name = "ACTIVE", type = String.class),
                                @ColumnResult(name = "CREATED_BY", type = String.class),
                                @ColumnResult(name = "DT_CREATED", type = LocalDateTime.class),
                                @ColumnResult(name = "CHANGED_BY", type = String.class),
                                @ColumnResult(name = "DT_CHANGED", type = LocalDateTime.class),
                                @ColumnResult(name = "DT_FROM", type = LocalDate.class),
                                @ColumnResult(name = "DT_TO", type = LocalDate.class),
                                @ColumnResult(name = "EXPORT_ORDER", type = Integer.class),
                                @ColumnResult(name = "FORECAST", type = Integer.class)
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
