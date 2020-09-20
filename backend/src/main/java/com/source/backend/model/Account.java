package com.source.backend.model;


import com.sun.istack.NotNull;
import lombok.Data;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.Instant;

@Data
@Entity
@Getter
public class Account {
    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private String username;
    @NotNull
    private String password;
    private String email;
    private boolean Enabled;
    private Integer bonuses;
    @NotNull
    private Instant registrationDate;

    private String permission;
}
