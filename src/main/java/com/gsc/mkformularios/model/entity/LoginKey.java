package com.gsc.mkformularios.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "login_key")
public class LoginKey implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty
    @Size(max = 100)
    @Column(name = "key_value")
    private String keyValue;
    @NotNull
    private Boolean enabled;
    @NotEmpty
    @Size(max = 50)
    @Column(name = "created_by")
    private String createdBy;
    @NotNull
    @Column(name = "dt_created")
    private LocalDateTime created;


}