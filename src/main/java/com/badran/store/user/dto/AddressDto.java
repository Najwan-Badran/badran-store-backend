package com.badran.store.user.dto;

import lombok.Data;

@Data
public class AddressDto {
    private Long addressId;
    private String label;
    private String city;
    private String zone;
    private String addressLine;
    private Boolean isDefault;
}
