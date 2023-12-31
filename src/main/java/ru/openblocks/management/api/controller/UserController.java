package ru.openblocks.management.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.openblocks.management.api.dto.common.IdResponse;
import ru.openblocks.management.api.dto.user.create.UserCreateRequest;
import ru.openblocks.management.api.dto.user.get.UserResponse;
import ru.openblocks.management.api.dto.user.update.UserUpdateNameRequest;
import ru.openblocks.management.api.dto.user.update.UserUpdatePasswordRequest;
import ru.openblocks.management.api.dto.userrole.get.UserRoleResponse;
import ru.openblocks.management.service.UserDataService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserDataService userDataService;

    @PostMapping
    public IdResponse<Long> create(@RequestBody @Valid UserCreateRequest request) {
        Long userId = userDataService.create(request);
        return IdResponse.of(userId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{userId}/password")
    public void updatePassword(@RequestBody @Valid UserUpdatePasswordRequest request,
                               @PathVariable Long userId) {
        userDataService.updatePassword(userId, request);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{userId}/name")
    public void updateName(@RequestBody @Valid UserUpdateNameRequest request,
                           @PathVariable Long userId) {
        userDataService.updateName(userId, request);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{userId}/roles/{roleId}")
    public void addRoleToUser(@PathVariable Long userId,
                              @PathVariable Long roleId) {
        userDataService.addRoleToUser(roleId, userId);
    }

    @GetMapping("/current")
    public UserResponse getCurrentUser() {
        final Long currentUserId = userDataService.getCurrentUserId();
        return userDataService.getById(currentUserId);
    }

    @GetMapping("/current-roles")
    public List<UserRoleResponse> getCurrentUserRoles() {
        final Long currentUserId = userDataService.getCurrentUserId();
        return userDataService.getUserRoles(currentUserId);
    }

    @GetMapping("/{userId}")
    public UserResponse getById(@PathVariable Long userId) {
        return userDataService.getById(userId);
    }

    @GetMapping("/login/{login}")
    public UserResponse getByLogin(@PathVariable String login) {
        return userDataService.getByLogin(login);
    }

    @GetMapping("/name")
    public List<UserResponse> getByName(@RequestParam String name) {
        return userDataService.getByName(name);
    }

    @GetMapping("/{userId}/roles")
    public List<UserRoleResponse> getUserRolesById(@PathVariable Long userId) {
        return userDataService.getUserRoles(userId);
    }

}
