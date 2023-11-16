package ru.openblocks.management.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.openblocks.management.api.dto.project.get.ProjectResponse;
import ru.openblocks.management.api.dto.projectaccess.get.ProjectAccessResponse;
import ru.openblocks.management.api.dto.projectaccess.update.ProjectAccessUpdateRequest;
import ru.openblocks.management.service.ProjectAccessService;
import ru.openblocks.management.service.ProjectService;

import java.util.List;

@RestController
@Tag(name = "Проекты")
@CrossOrigin
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    private final ProjectAccessService projectAccessService;

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Обновляет доступ пользователя к проекту",
            description = "Обновляет доступ пользователя к проекту, создает доступ, если ранее его не было"
    )
    @PostMapping("/{code}/access")
    public void updateProjectAccess(@PathVariable String code,
                                    @RequestBody @Valid ProjectAccessUpdateRequest request) {
        projectAccessService.update(request.getUserId(), code, request.getProjectAdmin());
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Удаляет доступ пользователя к проекту",
            description = "Удаляет доступ пользователя к проекту"
    )
    @DeleteMapping("/{code}/access/{userId}")
    public void deleteProjectAccess(@PathVariable String code,
                                    @PathVariable Long userId) {
        projectAccessService.delete(userId, code);
    }

    @Operation(
            summary = "Возвращает все существующие проекты",
            description = "Возвращает все существующие проекты"
    )
    @GetMapping
    public List<ProjectResponse> getAllProjects() {
        return projectService.getAllProjects();
    }

    @Operation(
            summary = "Возвращает все доступы пользователей в рамках заданного проекта",
            description = "Возвращает все доступы пользователей в рамках заданного проекта"
    )
    @GetMapping("/{code}/all-accesses")
    public List<ProjectAccessResponse> getAllProjectAccesses(@PathVariable String code) {
        return projectAccessService.getByProject(code);
    }

    @Operation(
            summary = "Возвращает проект по его коду",
            description = "Возвращает основные данные по проекту по его коду"
    )
    @GetMapping("/{code}")
    public ProjectResponse getByCode(@PathVariable String code) {
        return projectService.getByCode(code);
    }

    @Operation(
            summary = "Проверяет, является ли текущий пользователь администратором проекта",
            description = "Проверяет, является ли текущий пользователь администратором проекта. Пользователь " +
                    "определяется по содержимому токена или сессии"
    )
    @GetMapping("/{code}/is-admin")
    public Boolean isCurrentUserProjectAdmin(@PathVariable String code) {
        return projectService.isCurrentUserProjectAdmin(code);
    }
}
