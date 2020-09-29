package com.source.backend.Dto;

import com.source.backend.model.Type;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EcoUnitDto {
    private Long id = 0L;
    private Type type;
    private String subtype = "";
    @NotNull
    private Double latitude;
    @NotNull
    private Double longitude;
    private String Details = "";
    private String Descriptions = "";
}
