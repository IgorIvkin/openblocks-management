package ru.openblocks.management.api.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.openblocks.management.api.dto.reference.get.*;
import ru.openblocks.management.service.ReferenceService;

import java.util.List;

@RestController
@Tag(name = "Справочники")
@CrossOrigin
@RequestMapping("/api/v1/references")
@RequiredArgsConstructor
public class ReferenceController {

    private final ReferenceService referenceService;

    @GetMapping("/statuses")
    public List<TaskStatusReferenceResponse> getStatuses() {
        return referenceService.getStatuses();
    }

    @GetMapping("/priorities")
    public List<TaskPriorityReferenceResponse> getPriorities() {
        return referenceService.getPriorities();
    }

    @GetMapping("/types")
    public List<TaskTypeReferenceResponse> getTypes() {
        return referenceService.getTypes();
    }

    @GetMapping("/task-link-types")
    public List<TaskLinkTypeReferenceResponse> getTaskLinkTypes() {
        return referenceService.getTaskLinkTypes();
    }

    @GetMapping("/task-history-change-objects")
    public List<TaskHistoryChangeObjectReferenceResponse> getTaskHistoryChangeObjects() {
        return referenceService.getTaskHistoryChangeObjects();
    }
}
