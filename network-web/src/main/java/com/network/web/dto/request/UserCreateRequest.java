package com.network.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "User create request")
public class UserCreateRequest {

    @NotBlank(message = "Username cannot be empty")
    @Size(min = 3, max = 50, message = "Username must be 3-50 characters")
    @Schema(description = "Username", example = "admin")
    private String username;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 6, max = 100, message = "Password must be 6-100 characters")
    @Schema(description = "Password", example = "123456")
    private String password;

    @Schema(description = "Nickname", example = "Administrator")
    private String nickname;

    @Email(message = "Invalid email format")
    @Schema(description = "Email", example = "admin@example.com")
    private String email;

    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "Invalid phone number")
    @Schema(description = "Phone", example = "13800138000")
    private String phone;

    @Schema(description = "Status: 0-disabled, 1-enabled", example = "1")
    private Integer status;

    @Schema(description = "Avatar URL")
    private String avatar;

}
