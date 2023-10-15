package ru.openblocks.management.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.openblocks.management.api.dto.project.get.ProjectResponse;
import ru.openblocks.management.service.ProjectService;

import java.util.List;

@RestController
@Tag(name = "Проекты")
@CrossOrigin
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @Operation(
            summary = "Возвращает проект по его коду",
            description = "Возвращает основные данные по проекту по его коду"
    )
    @GetMapping("/{code}")
    public ProjectResponse getByCode(@PathVariable String code) {
        return projectService.getByCode(code);
    }

    @Operation(
            summary = "Возвращает все существующие проекты",
            description = "Возвращает все существующие проекты"
    )
    @GetMapping
    public List<ProjectResponse> getAllProjects() {
        return projectService.getAllProjects();
    }
}
