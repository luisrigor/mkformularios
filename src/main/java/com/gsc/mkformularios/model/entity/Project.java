package com.gsc.mkformularios.model.entity;

import com.gsc.mkformularios.constants.ProjectStatusType;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;


@Getter
@Setter
@Entity
public class Project implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty
    @Size(max = 50)
    private String name;
    @NotNull
    @Enumerated(EnumType.STRING)
    private ProjectStatusType status;
    @Size(max = 50)
    private String brand;
    @Size(max = 50)
    private String gamma;
    @Size(max = 50)
    private String genWvta;
    @Column(name = "dt_wvta")
    private LocalDateTime wvtaReceived;
    @Column(name = "dt_decode_suffix")
    private LocalDateTime decodeSuffixReceived;
    private Integer suffixVersion;
    @Column(name = "dt_imt_submitted")
    private LocalDateTime imtSubmitted;
    @Column(name = "dt_fam")
    private LocalDateTime famReceived;
    private String category;
    private String completion;
    @NotNull
    private Boolean csvGeneration;
    private String approvalNumber;
    private LocalDate wvtaApprovalDate;
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
    @JoinColumn(name = "id_client")
    private Client client;

    @OneToMany(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "id_project")
    private Set<ProjectFile> projectFiles;



}
