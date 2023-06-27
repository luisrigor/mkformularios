package com.gsc.mkformularios.model.toyota.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Configuration implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty
    @Size(max = 50)
    private String name;
    @NotEmpty
    @Size(max = 50)
    private String value;
    @NotEmpty
    @Size(max = 100)
    private String description;
    @NotEmpty
    @Size(max = 50)
    @Column(name = "created_by")
    private String createdBy;
    @NotNull
    @Column(name = "dt_created")
    private LocalDateTime created;
    @Size(max = 50)
    @Column(name = "changed_by")
    private String changedBy;
    @Column(name = "dt_changed")
    private LocalDateTime changed;

}
