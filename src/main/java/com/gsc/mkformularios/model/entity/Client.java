package com.gsc.mkformularios.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;


@Getter
@Setter
@Entity
@Table(name = "CLIENT")
public class Client implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty
    @Size(max = 50)
    private String name;
    @NotNull
    @Column(name = "id_application")
    private Long applicationId;
    @NotEmpty
    @Size(max = 5)
    @Column(name = "source_application")
    private String applicationSource;
    @NotEmpty
    @Size(max = 50)
    @Column(name = "created_by")
    private String createdBy;
    @NotNull
    @Column(name = "dt_created")
    private LocalDateTime created;

    @OneToMany(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "id_client")
    private Set<Project> projects;


}
