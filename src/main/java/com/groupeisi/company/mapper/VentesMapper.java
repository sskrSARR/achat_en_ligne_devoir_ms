package com.groupeisi.company.mapper;

import com.groupeisi.company.dto.VentesDto;
import com.groupeisi.company.entities.Ventes;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface VentesMapper {

    @Mapping(source = "product.ref", target = "productRef")
    @Mapping(source = "user.id",     target = "userId")
    VentesDto toDto(Ventes ventes);

    @Mapping(source = "productRef", target = "product.ref")
    @Mapping(source = "userId",     target = "user.id")
    Ventes toEntity(VentesDto dto);

    List<VentesDto> toDtoList(List<Ventes> ventes);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "productRef", target = "product.ref")
    @Mapping(source = "userId",     target = "user.id")
    void updateFromDto(VentesDto dto, @MappingTarget Ventes ventes);
}
