package com.badran.store.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * User profile response returned by authentication and profile endpoints.
 */
@Data
@Schema(description = "Customer or administrator profile.")
public class UserDto {
    @Schema(description = "User identifier.", example = "2")
    private Long userId;

    @Schema(description = "User display name.", example = "Ahmad Khalil")
    private String name;

    @Schema(description = "User email address.", example = "ahmad.khalil@example.com")
    private String email;

    @Schema(description = "User phone number.", example = "+970599123456")
    private String phone;

    @Schema(description = "Assigned role name.", example = "customer")
    private String roleName;

    @Schema(description = "Preferred language code.", example = "en")
    private String preferredLanguage;

    @Schema(description = "Saved user delivery addresses.")
    private List<AddressDto> addresses;
}
