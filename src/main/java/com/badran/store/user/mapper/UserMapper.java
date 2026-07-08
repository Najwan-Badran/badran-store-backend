package com.badran.store.user.mapper;

import com.badran.store.user.dto.AddressDto;
import com.badran.store.user.dto.UserDto;
import com.badran.store.user.entity.Address;
import com.badran.store.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "roleName", source = "role.roleName")
    UserDto toDto(User user);

    AddressDto toDto(Address address);
}
