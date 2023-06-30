package com.gsc.mkformularios.model.toyota.entity;

import com.gsc.mkformularios.constants.ProjectFileStatus;
import com.gsc.mkformularios.constants.ProjectFileType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;


@Getter
@Setter
@Entity
public class ProjectFile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty
    @Size(max = 255)
    private String name;
    @NotEmpty
    @Size(max = 255)
    private String path;
    @NotEmpty
    @Size(max = 255)
    private String checksum;
    @NotEmpty
    @Size(max = 255)
    private String originalName;
    @NotNull
    @Enumerated(EnumType.STRING)
    private ProjectFileType type;
    @NotNull
    @Enumerated(EnumType.STRING)
    private ProjectFileStatus status;
    @NotNull
    @Column(name = "active")
    private Boolean active;
    @NotEmpty
    @Size(max = 50)
    private String createdBy;
    @NotNull
    @Column(name = "dt_created")
    private LocalDateTime created;
    @Size(max = 50)
    private String changedBy;
    @Column(name = "dt_changed")
    private LocalDateTime changed;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_project")
    private Project project;


}
