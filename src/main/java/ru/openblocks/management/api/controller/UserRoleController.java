package ru.openblocks.management.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.openblocks.management.api.dto.common.IdResponse;
import ru.openblocks.management.api.dto.userrole.create.UserRoleCreateRequest;
import ru.openblocks.management.api.dto.userrole.get.UserRoleResponse;
import ru.openblocks.management.api.dto.userrole.update.UserRoleUpdateRequest;
import ru.openblocks.management.service.UserRoleService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/roles")
public class UserRoleController {

    private final UserRoleService userRoleService;

    @GetMapping("/{roleId}")
    public UserRoleResponse getById(@PathVariable Long roleId) {
        return userRoleService.getById(roleId);
    }

    @GetMapping("/code/{code}")
    public UserRoleResponse getByCode(@PathVariable String code) {
        return userRoleService.getByCode(code);
    }

    @PostMapping
    public IdResponse<Long> create(@RequestBody @Valid UserRoleCreateRequest request) {
        Long roleId = userRoleService.create(request);
        return IdResponse.of(roleId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{roleId}")
    public void update(@PathVariable Long roleId, @RequestBody @Valid UserRoleUpdateRequest request) {
        userRoleService.update(roleId, request);
    }

}
