package com.groupeisi.company.mapper;

import com.groupeisi.company.dto.UserAccountDto;
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
public class UserAccountMapperImpl implements UserAccountMapper {

    @Override
    public UserAccountDto toDto(UserAccount userAccount) {
        if ( userAccount == null ) {
            return null;
        }

        UserAccountDto.UserAccountDtoBuilder userAccountDto = UserAccountDto.builder();

        userAccountDto.id( userAccount.getId() );
        userAccountDto.email( userAccount.getEmail() );
        userAccountDto.password( userAccount.getPassword() );

        return userAccountDto.build();
    }

    @Override
    public UserAccount toEntity(UserAccountDto dto) {
        if ( dto == null ) {
            return null;
        }

        UserAccount.UserAccountBuilder userAccount = UserAccount.builder();

        userAccount.id( dto.getId() );
        userAccount.email( dto.getEmail() );
        userAccount.password( dto.getPassword() );

        return userAccount.build();
    }

    @Override
    public List<UserAccountDto> toDtoList(List<UserAccount> userAccounts) {
        if ( userAccounts == null ) {
            return null;
        }

        List<UserAccountDto> list = new ArrayList<UserAccountDto>( userAccounts.size() );
        for ( UserAccount userAccount : userAccounts ) {
            list.add( toDto( userAccount ) );
        }

        return list;
    }

    @Override
    public void updateFromDto(UserAccountDto dto, UserAccount userAccount) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getId() != null ) {
            userAccount.setId( dto.getId() );
        }
        if ( dto.getEmail() != null ) {
            userAccount.setEmail( dto.getEmail() );
        }
        if ( dto.getPassword() != null ) {
            userAccount.setPassword( dto.getPassword() );
        }
    }
}
