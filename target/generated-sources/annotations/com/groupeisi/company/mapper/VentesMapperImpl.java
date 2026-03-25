package com.groupeisi.company.mapper;

import com.groupeisi.company.dto.VentesDto;
import com.groupeisi.company.entities.Produits;
import com.groupeisi.company.entities.UserAccount;
import com.groupeisi.company.entities.Ventes;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-24T23:03:06+0000",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.1 (Oracle Corporation)"
)
@Component
public class VentesMapperImpl implements VentesMapper {

    @Override
    public VentesDto toDto(Ventes ventes) {
        if ( ventes == null ) {
            return null;
        }

        VentesDto.VentesDtoBuilder ventesDto = VentesDto.builder();

        ventesDto.productRef( ventesProductRef( ventes ) );
        ventesDto.userId( ventesUserId( ventes ) );
        ventesDto.id( ventes.getId() );
        ventesDto.dateP( ventes.getDateP() );
        ventesDto.quantity( ventes.getQuantity() );

        return ventesDto.build();
    }

    @Override
    public Ventes toEntity(VentesDto dto) {
        if ( dto == null ) {
            return null;
        }

        Ventes.VentesBuilder ventes = Ventes.builder();

        ventes.product( ventesDtoToProduits( dto ) );
        ventes.user( ventesDtoToUserAccount( dto ) );
        ventes.id( dto.getId() );
        ventes.dateP( dto.getDateP() );
        ventes.quantity( dto.getQuantity() );

        return ventes.build();
    }

    @Override
    public List<VentesDto> toDtoList(List<Ventes> ventes) {
        if ( ventes == null ) {
            return null;
        }

        List<VentesDto> list = new ArrayList<VentesDto>( ventes.size() );
        for ( Ventes ventes1 : ventes ) {
            list.add( toDto( ventes1 ) );
        }

        return list;
    }

    @Override
    public void updateFromDto(VentesDto dto, Ventes ventes) {
        if ( dto == null ) {
            return;
        }

        if ( ventes.getProduct() == null ) {
            ventes.setProduct( Produits.builder().build() );
        }
        ventesDtoToProduits1( dto, ventes.getProduct() );
        if ( ventes.getUser() == null ) {
            ventes.setUser( UserAccount.builder().build() );
        }
        ventesDtoToUserAccount1( dto, ventes.getUser() );
        if ( dto.getId() != null ) {
            ventes.setId( dto.getId() );
        }
        if ( dto.getDateP() != null ) {
            ventes.setDateP( dto.getDateP() );
        }
        ventes.setQuantity( dto.getQuantity() );
    }

    private String ventesProductRef(Ventes ventes) {
        if ( ventes == null ) {
            return null;
        }
        Produits product = ventes.getProduct();
        if ( product == null ) {
            return null;
        }
        String ref = product.getRef();
        if ( ref == null ) {
            return null;
        }
        return ref;
    }

    private Long ventesUserId(Ventes ventes) {
        if ( ventes == null ) {
            return null;
        }
        UserAccount user = ventes.getUser();
        if ( user == null ) {
            return null;
        }
        Long id = user.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    protected Produits ventesDtoToProduits(VentesDto ventesDto) {
        if ( ventesDto == null ) {
            return null;
        }

        Produits.ProduitsBuilder produits = Produits.builder();

        produits.ref( ventesDto.getProductRef() );

        return produits.build();
    }

    protected UserAccount ventesDtoToUserAccount(VentesDto ventesDto) {
        if ( ventesDto == null ) {
            return null;
        }

        UserAccount.UserAccountBuilder userAccount = UserAccount.builder();

        userAccount.id( ventesDto.getUserId() );

        return userAccount.build();
    }

    protected void ventesDtoToProduits1(VentesDto ventesDto, Produits mappingTarget) {
        if ( ventesDto == null ) {
            return;
        }

        if ( ventesDto.getProductRef() != null ) {
            mappingTarget.setRef( ventesDto.getProductRef() );
        }
    }

    protected void ventesDtoToUserAccount1(VentesDto ventesDto, UserAccount mappingTarget) {
        if ( ventesDto == null ) {
            return;
        }

        if ( ventesDto.getUserId() != null ) {
            mappingTarget.setId( ventesDto.getUserId() );
        }
    }
}
