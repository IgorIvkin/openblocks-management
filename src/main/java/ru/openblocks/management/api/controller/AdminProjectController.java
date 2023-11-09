package ru.openblocks.management.api.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.openblocks.management.api.dto.project.create.ProjectCreateRequest;
import ru.openblocks.management.service.ProjectService;

@RestController
@Tag(name = "Администрирование проектов")
@CrossOrigin
@RequestMapping("/api/v1/admin/projects")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMINISTRATOR')")
public class AdminProjectController {

    private final ProjectService projectService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void create(@RequestBody @Valid ProjectCreateRequest request) {
        projectService.create(request);
    }
}
