package ru.openblocks.management.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.openblocks.management.api.dto.common.IdResponse;
import ru.openblocks.management.api.dto.task.create.TaskCreateRequest;
import ru.openblocks.management.api.dto.task.get.TaskCardResponse;
import ru.openblocks.management.api.dto.task.update.*;
import ru.openblocks.management.service.TaskService;

@RestController
@Tag(name = "Задачи")
@CrossOrigin
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping("/{projectCode}")
    @Operation(
            summary = "Создаёт новую задачу в заданном проекте",
            description = "Создает новую задачу в заданном проекте и возвращает ее код"
    )
    public IdResponse<String> create(@RequestBody @Valid TaskCreateRequest request,
                                     @PathVariable @NotBlank @Size(max = 255) String projectCode) {
        final String taskCode = taskService.create(projectCode, request);
        return IdResponse.of(taskCode);
    }

    @PutMapping("/{code}/status")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Обновляет статус у заданной задачи",
            description = "Обновляет статус у задачи, заданной ее кодом"
    )
    public void updateTaskStatus(@RequestBody @Valid TaskUpdateStatusRequest request,
                                 @PathVariable @NotBlank @Size(max = 255) String code) {
        taskService.updateTaskStatus(code, request);
    }

    @PutMapping("/{code}/subject")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Обновляет название у заданной задачи",
            description = "Обновляет название у задачи, заданной ее кодом"
    )
    public void updateTaskSubject(@RequestBody @Valid TaskUpdateSubjectRequest request,
                                  @PathVariable @NotBlank @Size(max = 255) String code) {
        taskService.updateTaskSubject(code, request);
    }

    @PutMapping("/{code}/explanation")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Обновляет описание у заданной задачи",
            description = "Обновляет описание у задачи, заданной ее кодом"
    )
    public void updateTaskExplanation(@RequestBody @Valid TaskUpdateExplanationRequest request,
                                      @PathVariable @NotBlank @Size(max = 255) String code) {
        taskService.updateTaskExplanation(code, request);
    }

    @PutMapping("/{code}/due-date")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Обновляет срок завершения у заданной задачи",
            description = "Обновляет срок завершения у задачи, заданной ее кодом"
    )
    public void updateTaskDueDate(@RequestBody @Valid TaskUpdateDueDateRequest request,
                                  @PathVariable @NotBlank @Size(max = 255) String code) {
        taskService.updateTaskDueDate(code, request);
    }

    @PutMapping("/{code}/estimation")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Обновляет оценку у заданной задачи",
            description = "Обновляет оценку у задачи, заданной ее кодом"
    )
    public void updateTaskEstimation(@RequestBody @Valid TaskUpdateEstimationRequest request,
                                     @PathVariable @NotBlank @Size(max = 255) String code) {
        taskService.updateTaskEstimation(code, request);
    }

    @PutMapping("/{code}/executor")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Обновляет исполнителя у заданной задачи",
            description = "Обновляет исполнителя у задачи, заданной ее кодом"
    )
    public void updateTaskExecutor(@RequestBody @Valid TaskUpdateExecutorRequest request,
                                   @PathVariable @NotBlank @Size(max = 255) String code) {
        taskService.updateTaskExecutor(code, request);
    }

    @PutMapping("/{code}/owner")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Обновляет владельца у заданной задачи",
            description = "Обновляет владельца у задачи, заданной ее кодом"
    )
    public void updateTaskExecutor(@RequestBody @Valid TaskUpdateOwnerRequest request,
                                   @PathVariable @NotBlank @Size(max = 255) String code) {
        taskService.updateTaskOwner(code, request);
    }

    @GetMapping("/{code}")
    @Operation(
            summary = "Возвращает карточку задачи по её коду",
            description = "Возвращает основную карточку задачи по ее коду"
    )
    public TaskCardResponse getByCode(@PathVariable @NotBlank @Size(max = 255) String code) {
        return taskService.getByCode(code);
    }
}
