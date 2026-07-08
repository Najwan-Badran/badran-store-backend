package com.badran.store.user.mapper;

import com.badran.store.user.dto.AddressDto;
import com.badran.store.user.dto.UserDto;
import com.badran.store.user.entity.Address;
import com.badran.store.user.entity.Role;
import com.badran.store.user.entity.User;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-08T15:46:00+0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.10 (Homebrew)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDto toDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserDto userDto = new UserDto();

        userDto.setRoleName( userRoleRoleName( user ) );
        userDto.setUserId( user.getUserId() );
        userDto.setName( user.getName() );
        userDto.setEmail( user.getEmail() );
        userDto.setPhone( user.getPhone() );
        userDto.setPreferredLanguage( user.getPreferredLanguage() );
        userDto.setAddresses( addressListToAddressDtoList( user.getAddresses() ) );

        return userDto;
    }

    @Override
    public AddressDto toDto(Address address) {
        if ( address == null ) {
            return null;
        }

        AddressDto addressDto = new AddressDto();

        addressDto.setAddressId( address.getAddressId() );
        addressDto.setLabel( address.getLabel() );
        addressDto.setCity( address.getCity() );
        addressDto.setZone( address.getZone() );
        addressDto.setAddressLine( address.getAddressLine() );
        addressDto.setIsDefault( address.getIsDefault() );

        return addressDto;
    }

    private String userRoleRoleName(User user) {
        Role role = user.getRole();
        if ( role == null ) {
            return null;
        }
        return role.getRoleName();
    }

    protected List<AddressDto> addressListToAddressDtoList(List<Address> list) {
        if ( list == null ) {
            return null;
        }

        List<AddressDto> list1 = new ArrayList<AddressDto>( list.size() );
        for ( Address address : list ) {
            list1.add( toDto( address ) );
        }

        return list1;
    }
}
