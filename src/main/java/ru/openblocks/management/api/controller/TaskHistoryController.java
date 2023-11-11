package ru.openblocks.management.api.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.openblocks.management.api.dto.taskhistory.get.TaskHistoryResponse;
import ru.openblocks.management.service.history.TaskHistoryService;

import java.util.List;

@RestController
@Tag(name = "История изменений")
@CrossOrigin
@RequestMapping("/api/v1/task-history")
@RequiredArgsConstructor
public class TaskHistoryController {

    private final TaskHistoryService taskHistoryService;

    @GetMapping("/{code}")
    public List<TaskHistoryResponse> getTaskHistory(@PathVariable String code) {
        return taskHistoryService.getTaskHistory(code);
    }
}
