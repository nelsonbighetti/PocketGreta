package com.source.backend.Dto;

import lombok.Builder;

@Builder
public class AccountInfoDto {

    private final String email;
    private final String username;
    private final int bonuses;

}
