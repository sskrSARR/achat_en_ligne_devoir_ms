package com.groupeisi.company.mapper;

import com.groupeisi.company.dto.AchatsDto;
import com.groupeisi.company.entities.Achats;
import com.groupeisi.company.entities.Produits;
import com.groupeisi.company.entities.UserAccount;
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
public class AchatsMapperImpl implements AchatsMapper {

    @Override
    public AchatsDto toDto(Achats achats) {
        if ( achats == null ) {
            return null;
        }

        AchatsDto.AchatsDtoBuilder achatsDto = AchatsDto.builder();

        achatsDto.productRef( achatsProductRef( achats ) );
        achatsDto.userId( achatsUserId( achats ) );
        achatsDto.id( achats.getId() );
        achatsDto.dateP( achats.getDateP() );
        achatsDto.quantity( achats.getQuantity() );

        return achatsDto.build();
    }

    @Override
    public Achats toEntity(AchatsDto dto) {
        if ( dto == null ) {
            return null;
        }

        Achats.AchatsBuilder achats = Achats.builder();

        achats.product( achatsDtoToProduits( dto ) );
        achats.user( achatsDtoToUserAccount( dto ) );
        achats.id( dto.getId() );
        achats.dateP( dto.getDateP() );
        achats.quantity( dto.getQuantity() );

        return achats.build();
    }

    @Override
    public List<AchatsDto> toDtoList(List<Achats> achats) {
        if ( achats == null ) {
            return null;
        }

        List<AchatsDto> list = new ArrayList<AchatsDto>( achats.size() );
        for ( Achats achats1 : achats ) {
            list.add( toDto( achats1 ) );
        }

        return list;
    }

    @Override
    public void updateFromDto(AchatsDto dto, Achats achats) {
        if ( dto == null ) {
            return;
        }

        if ( achats.getProduct() == null ) {
            achats.setProduct( Produits.builder().build() );
        }
        achatsDtoToProduits1( dto, achats.getProduct() );
        if ( achats.getUser() == null ) {
            achats.setUser( UserAccount.builder().build() );
        }
        achatsDtoToUserAccount1( dto, achats.getUser() );
        if ( dto.getId() != null ) {
            achats.setId( dto.getId() );
        }
        if ( dto.getDateP() != null ) {
            achats.setDateP( dto.getDateP() );
        }
        achats.setQuantity( dto.getQuantity() );
    }

    private String achatsProductRef(Achats achats) {
        if ( achats == null ) {
            return null;
        }
        Produits product = achats.getProduct();
        if ( product == null ) {
            return null;
        }
        String ref = product.getRef();
        if ( ref == null ) {
            return null;
        }
        return ref;
    }

    private Long achatsUserId(Achats achats) {
        if ( achats == null ) {
            return null;
        }
        UserAccount user = achats.getUser();
        if ( user == null ) {
            return null;
        }
        Long id = user.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    protected Produits achatsDtoToProduits(AchatsDto achatsDto) {
        if ( achatsDto == null ) {
            return null;
        }

        Produits.ProduitsBuilder produits = Produits.builder();

        produits.ref( achatsDto.getProductRef() );

        return produits.build();
    }

    protected UserAccount achatsDtoToUserAccount(AchatsDto achatsDto) {
        if ( achatsDto == null ) {
            return null;
        }

        UserAccount.UserAccountBuilder userAccount = UserAccount.builder();

        userAccount.id( achatsDto.getUserId() );

        return userAccount.build();
    }

    protected void achatsDtoToProduits1(AchatsDto achatsDto, Produits mappingTarget) {
        if ( achatsDto == null ) {
            return;
        }

        if ( achatsDto.getProductRef() != null ) {
            mappingTarget.setRef( achatsDto.getProductRef() );
        }
    }

    protected void achatsDtoToUserAccount1(AchatsDto achatsDto, UserAccount mappingTarget) {
        if ( achatsDto == null ) {
            return;
        }

        if ( achatsDto.getUserId() != null ) {
            mappingTarget.setId( achatsDto.getUserId() );
        }
    }
}
