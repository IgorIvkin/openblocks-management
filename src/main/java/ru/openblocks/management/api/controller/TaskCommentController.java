package ru.openblocks.management.api.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.openblocks.management.api.dto.common.IdResponse;
import ru.openblocks.management.api.dto.taskcomment.create.TaskCommentCreateRequest;
import ru.openblocks.management.api.dto.taskcomment.get.TaskCommentResponse;
import ru.openblocks.management.api.dto.taskcomment.update.TaskCommentUpdateRequest;
import ru.openblocks.management.service.TaskCommentService;

import java.util.List;

@RestController
@Tag(name = "Комментарии к задачам")
@CrossOrigin
@RequestMapping("/api/v1/task-comments")
@RequiredArgsConstructor
public class TaskCommentController {

    private final TaskCommentService taskCommentService;

    @PostMapping("/{code}")
    public IdResponse<Long> create(@PathVariable String code,
                                   @RequestBody @Valid TaskCommentCreateRequest request) {
        final Long taskCommentId = taskCommentService.create(code, request);
        return IdResponse.of(taskCommentId);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Long id,
                       @RequestBody @Valid TaskCommentUpdateRequest request) {
        taskCommentService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        taskCommentService.delete(id);
    }

    @GetMapping("/comment/{id}")
    public TaskCommentResponse getById(@PathVariable Long id) {
        return taskCommentService.getById(id);
    }

    @GetMapping("/task/{code}")
    public List<TaskCommentResponse> getByCode(@PathVariable String code) {
        return taskCommentService.getByTaskCode(code);
    }
}
