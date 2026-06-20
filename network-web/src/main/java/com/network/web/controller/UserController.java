package com.network.web.controller;

import cn.hutool.core.bean.BeanUtil;
import com.network.common.base.BaseResponse;
import com.network.common.base.PageResult;
import com.network.dal.entity.UserEntity;
import com.network.service.UserService;
import com.network.web.dto.request.UserCreateRequest;
import com.network.web.dto.request.UserQueryRequest;
import com.network.web.dto.request.UserUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User Management", description = "User CRUD operations")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "Create user")
    @PostMapping
    public BaseResponse<UserEntity> create(@Valid @RequestBody UserCreateRequest request) {
        UserEntity entity = new UserEntity();
        BeanUtil.copyProperties(request, entity);
        return BaseResponse.success(userService.createUser(entity));
    }

    @Operation(summary = "Update user")
    @PutMapping
    public BaseResponse<UserEntity> update(@Valid @RequestBody UserUpdateRequest request) {
        UserEntity entity = new UserEntity();
        BeanUtil.copyProperties(request, entity);
        return BaseResponse.success(userService.updateUser(entity));
    }

    @Operation(summary = "Delete user")
    @DeleteMapping("/{id}")
    public BaseResponse<Void> delete(@Parameter(description = "User ID") @PathVariable Long id) {
        userService.deleteUser(id);
        return BaseResponse.success();
    }

    @Operation(summary = "Get user by ID")
    @GetMapping("/{id}")
    public BaseResponse<UserEntity> getById(@Parameter(description = "User ID") @PathVariable Long id) {
        return BaseResponse.success(userService.getUserById(id));
    }

    @Operation(summary = "Get user by username")
    @GetMapping("/username/{username}")
    public BaseResponse<UserEntity> getByUsername(@Parameter(description = "Username") @PathVariable String username) {
        return BaseResponse.success(userService.getUserByUsername(username));
    }

    @Operation(summary = "Paginated user list")
    @GetMapping("/page")
    public BaseResponse<PageResult<UserEntity>> page(@Valid UserQueryRequest query) {
        UserEntity entity = new UserEntity();
        BeanUtil.copyProperties(query, entity);
        return BaseResponse.success(userService.listUsers(entity, query.getPage(), query.getPageSize()));
    }

    @Operation(summary = "List all users")
    @GetMapping
    public BaseResponse<java.util.List<UserEntity>> list(@Valid UserQueryRequest query) {
        UserEntity entity = new UserEntity();
        BeanUtil.copyProperties(query, entity);
        return BaseResponse.success(userService.listAll(entity));
    }

}
