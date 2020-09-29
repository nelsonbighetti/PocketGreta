package com.source.backend.model;

import lombok.Data;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
@Getter
public class Code {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String qrcode;

    private int bonuses;

}
