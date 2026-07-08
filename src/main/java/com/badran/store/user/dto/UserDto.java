package com.badran.store.user.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserDto {
    private Long userId;
    private String name;
    private String email;
    private String phone;
    private String roleName;
    private String preferredLanguage;
    private List<AddressDto> addresses;
}
