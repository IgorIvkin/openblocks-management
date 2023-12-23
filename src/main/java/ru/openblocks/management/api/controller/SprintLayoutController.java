package ru.openblocks.management.api.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.openblocks.management.api.dto.sprintlayout.get.SprintLayoutResponse;
import ru.openblocks.management.api.dto.sprintlayout.update.SprintLayoutUpdateRequest;
import ru.openblocks.management.service.SprintLayoutService;

@RestController
@Tag(name = "Порядок задач в спринте")
@CrossOrigin
@RequestMapping("/api/v1/sprint-layout")
@RequiredArgsConstructor
public class SprintLayoutController {

    private final SprintLayoutService sprintLayoutService;

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{projectCode}")
    public void updateLayout(@PathVariable String projectCode,
                             @RequestBody @Valid SprintLayoutUpdateRequest request) {
        sprintLayoutService.updateLayout(projectCode, request);
    }

    @GetMapping("/{sprintId}")
    public SprintLayoutResponse getLayout(@PathVariable Long sprintId) {
        return sprintLayoutService.getLayout(sprintId);
    }
}
