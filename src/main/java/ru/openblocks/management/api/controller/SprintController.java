package ru.openblocks.management.api.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.openblocks.management.api.dto.common.IdResponse;
import ru.openblocks.management.api.dto.sprint.create.SprintCreateRequest;
import ru.openblocks.management.api.dto.sprint.get.SprintGetResponse;
import ru.openblocks.management.service.SprintService;

import java.util.List;

@RestController
@Tag(name = "Спринты")
@CrossOrigin
@RequestMapping("/api/v1/sprints")
@RequiredArgsConstructor
public class SprintController {

    private final SprintService sprintService;

    @PostMapping
    public IdResponse<Long> create(@RequestBody @Valid SprintCreateRequest request) {
        final Long sprintId = sprintService.create(request);
        return IdResponse.of(sprintId);
    }

    @GetMapping("/{projectCode}/all")
    public List<SprintGetResponse> getByProject(@PathVariable String projectCode) {
        return sprintService.getByProject(projectCode);
    }

    @GetMapping("/{projectCode}/unfinished")
    public List<SprintGetResponse> getByProjectUnfinished(@PathVariable String projectCode) {
        return sprintService.getByProjectUnfinished(projectCode);
    }

    @GetMapping("/{sprintId}/close")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void closeSprint(@PathVariable Long sprintId) {
        sprintService.closeSprint(sprintId);
    }
}
