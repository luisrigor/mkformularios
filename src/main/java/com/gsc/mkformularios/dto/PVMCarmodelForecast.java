<<<<<<<< HEAD:src/main/java/com/gsc/mkformularios/model/toyota/entity/PVMCarmodel.java
package com.gsc.mkformularios.model.toyota.entity;

========
package com.gsc.mkformularios.dto;
>>>>>>>> excel-services:src/main/java/com/gsc/mkformularios/dto/PVMCarmodelForecast.java

import com.gsc.mkformularios.dto.MapTypesDTO;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
<<<<<<<< HEAD:src/main/java/com/gsc/mkformularios/model/toyota/entity/PVMCarmodel.java
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
========
public class PVMCarmodelForecast {

>>>>>>>> excel-services:src/main/java/com/gsc/mkformularios/dto/PVMCarmodelForecast.java

    private Integer id;
    private String name;
    private String type;
    private String active;
    private String createdBy;
    private LocalDateTime dtCreated;
    private String changedBy;
    private LocalDateTime dtChanged;
    private LocalDate dtFrom;
    private LocalDate dtTo;
    private Integer exportOrder;
    private Integer forecast;
}
