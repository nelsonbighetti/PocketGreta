package com.source.backend.mapper;

import com.source.backend.Dto.HistoryDto;
import com.source.backend.model.AccountAndCode;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class AccountToAccountAndCodeDtoMapper {

    @Mapping(target = "date", source = "accountAndCode.date")
    @Mapping(target = "bonuses", source = "accountAndCode.bonuses")
    public abstract HistoryDto mapToDto(AccountAndCode accountAndCode);
}
