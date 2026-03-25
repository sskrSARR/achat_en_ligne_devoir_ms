package com.groupeisi.company.mapper;

import com.groupeisi.company.dto.UserAccountDto;
import com.groupeisi.company.entities.UserAccount;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserAccountMapper {

    UserAccountDto toDto(UserAccount userAccount);

    UserAccount toEntity(UserAccountDto dto);

    List<UserAccountDto> toDtoList(List<UserAccount> userAccounts);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(UserAccountDto dto, @MappingTarget UserAccount userAccount);
}
