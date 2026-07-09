package com.badran.store.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * User address response used for delivery address selection.
 */
@Data
@Schema(description = "Saved customer address.")
public class AddressDto {
    @Schema(description = "Address identifier.", example = "7")
    private Long addressId;

    @Schema(description = "Customer label for the address.", example = "Home")
    private String label;

    @Schema(description = "City.", example = "Ramallah")
    private String city;

    @Schema(description = "Zone or neighborhood.", example = "Al-Tireh")
    private String zone;

    @Schema(description = "Street and building details.", example = "Main Street, Building 12")
    private String addressLine;

    @Schema(description = "Whether this is the default delivery address.", example = "true")
    private Boolean isDefault;
}
