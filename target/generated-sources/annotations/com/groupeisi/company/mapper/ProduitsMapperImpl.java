package com.groupeisi.company.mapper;

import com.groupeisi.company.dto.ProduitsDto;
import com.groupeisi.company.entities.Produits;
import com.groupeisi.company.entities.UserAccount;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-24T23:03:05+0000",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.1 (Oracle Corporation)"
)
@Component
public class ProduitsMapperImpl implements ProduitsMapper {

    @Override
    public ProduitsDto toDto(Produits produits) {
        if ( produits == null ) {
            return null;
        }

        ProduitsDto.ProduitsDtoBuilder produitsDto = ProduitsDto.builder();

        produitsDto.userId( produitsUserId( produits ) );
        produitsDto.ref( produits.getRef() );
        produitsDto.name( produits.getName() );
        produitsDto.stock( produits.getStock() );

        return produitsDto.build();
    }

    @Override
    public Produits toEntity(ProduitsDto dto) {
        if ( dto == null ) {
            return null;
        }

        Produits.ProduitsBuilder produits = Produits.builder();

        produits.user( produitsDtoToUserAccount( dto ) );
        produits.ref( dto.getRef() );
        produits.name( dto.getName() );
        produits.stock( dto.getStock() );

        return produits.build();
    }

    @Override
    public List<ProduitsDto> toDtoList(List<Produits> produits) {
        if ( produits == null ) {
            return null;
        }

        List<ProduitsDto> list = new ArrayList<ProduitsDto>( produits.size() );
        for ( Produits produits1 : produits ) {
            list.add( toDto( produits1 ) );
        }

        return list;
    }

    @Override
    public void updateFromDto(ProduitsDto dto, Produits produits) {
        if ( dto == null ) {
            return;
        }

        if ( produits.getUser() == null ) {
            produits.setUser( UserAccount.builder().build() );
        }
        produitsDtoToUserAccount1( dto, produits.getUser() );
        if ( dto.getName() != null ) {
            produits.setName( dto.getName() );
        }
        produits.setStock( dto.getStock() );
    }

    private Long produitsUserId(Produits produits) {
        if ( produits == null ) {
            return null;
        }
        UserAccount user = produits.getUser();
        if ( user == null ) {
            return null;
        }
        Long id = user.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    protected UserAccount produitsDtoToUserAccount(ProduitsDto produitsDto) {
        if ( produitsDto == null ) {
            return null;
        }

        UserAccount.UserAccountBuilder userAccount = UserAccount.builder();

        userAccount.id( produitsDto.getUserId() );

        return userAccount.build();
    }

    protected void produitsDtoToUserAccount1(ProduitsDto produitsDto, UserAccount mappingTarget) {
        if ( produitsDto == null ) {
            return;
        }

        if ( produitsDto.getUserId() != null ) {
            mappingTarget.setId( produitsDto.getUserId() );
        }
    }
}
