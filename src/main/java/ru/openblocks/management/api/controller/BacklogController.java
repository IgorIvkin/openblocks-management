package ru.openblocks.management.api.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.openblocks.management.api.dto.backlog.get.BacklogGetRequest;
import ru.openblocks.management.api.dto.backlog.get.BacklogGetResponse;
import ru.openblocks.management.service.BacklogService;

import java.util.List;

@RestController
@Tag(name = "Бэклог")
@CrossOrigin
@RequestMapping("/api/v1/backlog")
@RequiredArgsConstructor
public class BacklogController {

    private final BacklogService backlogService;

    @PostMapping
    private List<BacklogGetResponse> getBacklog(@RequestBody @Valid BacklogGetRequest request) {
        return backlogService.getBacklog(request);
    }
}
