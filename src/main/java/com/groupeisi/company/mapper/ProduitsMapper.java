package com.groupeisi.company.mapper;

import com.groupeisi.company.dto.ProduitsDto;
import com.groupeisi.company.entities.Produits;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProduitsMapper {

    @Mapping(source = "user.id", target = "userId")
    ProduitsDto toDto(Produits produits);

    @Mapping(source = "userId", target = "user.id")
    Produits toEntity(ProduitsDto dto);

    List<ProduitsDto> toDtoList(List<Produits> produits);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "userId", target = "user.id")
    @Mapping(target = "ref", ignore = true) // ref est la PK, on ne la modifie pas
    void updateFromDto(ProduitsDto dto, @MappingTarget Produits produits);
}
