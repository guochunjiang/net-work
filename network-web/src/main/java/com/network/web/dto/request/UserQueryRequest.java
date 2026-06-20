package com.network.web.dto.request;

import com.network.common.dto.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "User query request")
public class UserQueryRequest extends PageParam {

    @Schema(description = "Username (fuzzy match)", example = "admin")
    private String username;

    @Schema(description = "Nickname (fuzzy match)", example = "Admin")
    private String nickname;

    @Schema(description = "Email", example = "admin@example.com")
    private String email;

    @Schema(description = "Phone", example = "13800138000")
    private String phone;

    @Schema(description = "Status: 0-disabled, 1-enabled", example = "1")
    private Integer status;

}
