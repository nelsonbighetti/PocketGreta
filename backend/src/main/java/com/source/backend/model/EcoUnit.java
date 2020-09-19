package com.source.backend.model;


import com.sun.istack.NotNull;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class EcoUnit {
    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Enumerated(EnumType.ORDINAL)
    private Type type;
    private String subtype = "";
    @NotNull
    private Double latitude;
    @NotNull
    private Double longitude;
    private String Details = "";
    private String Descriptions = "";

}
