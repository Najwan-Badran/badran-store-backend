package com.badran.store.mapper;

import com.badran.store.dto.model.AddressDto;
import com.badran.store.dto.model.UserDto;
import com.badran.store.entity.Address;
import com.badran.store.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct mapper for user profile and address API responses.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * Converts a user entity into a profile DTO with role name flattened.
     */
    @Mapping(target = "roleName", source = "role.roleName")
    UserDto toDto(User user);

    /**
     * Converts a saved address entity into a response DTO.
     */
    AddressDto toDto(Address address);
}
