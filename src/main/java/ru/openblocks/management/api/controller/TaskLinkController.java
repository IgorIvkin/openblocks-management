package ru.openblocks.management.api.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.openblocks.management.api.dto.common.IdResponse;
import ru.openblocks.management.api.dto.tasklink.create.TaskLinkCreateRequest;
import ru.openblocks.management.api.dto.tasklink.get.TaskLinkResponse;
import ru.openblocks.management.service.TaskLinkService;

import java.util.List;

@RestController
@Tag(name = "Связи между задачами")
@CrossOrigin
@RequestMapping("/api/v1/task-links")
@RequiredArgsConstructor
public class TaskLinkController {

    private final TaskLinkService taskLinkService;

    @PostMapping
    public IdResponse<Long> create(@RequestBody @Valid TaskLinkCreateRequest request) {
        final Long taskLinkId = taskLinkService.create(request);
        return IdResponse.of(taskLinkId);
    }

    @GetMapping("/{taskCode}")
    public List<TaskLinkResponse> getByTaskCode(@PathVariable String taskCode) {
        return taskLinkService.getByTaskCode(taskCode);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        taskLinkService.deleteById(id);
    }
}
