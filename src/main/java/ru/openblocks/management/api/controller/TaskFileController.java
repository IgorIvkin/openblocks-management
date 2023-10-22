package ru.openblocks.management.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.openblocks.management.api.dto.common.IdResponse;
import ru.openblocks.management.api.dto.taskfile.create.TaskFileCreateRequest;
import ru.openblocks.management.api.dto.taskfile.get.TaskFileGetResponse;
import ru.openblocks.management.service.TaskFileService;

import java.io.IOException;
import java.util.List;

@RestController
@Tag(name = "Файлы в задачах")
@CrossOrigin
@RequestMapping("/api/v1/task-files")
@RequiredArgsConstructor
public class TaskFileController {

    private final TaskFileService taskFileService;

    @Operation(
            summary = "Добавляет новый файл в задачу",
            description = "Добавляет новый файл в существующую задачу"
    )
    @PostMapping("/{taskCode}")
    public IdResponse<Long> store(@PathVariable(name = "taskCode") String taskCode,
                                  @RequestParam("file") MultipartFile file) {
        try {

            final Long fileId = taskFileService.store(
                    taskCode,
                    file.getInputStream(),
                    new TaskFileCreateRequest(file.getOriginalFilename())
            );
            return IdResponse.of(fileId);

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Operation(
            summary = "Удаляет файл из задачи",
            description = "Удаляет файл из задачи по заданным коду задачи и идентификатору файла"
    )
    @DeleteMapping("/{taskCode}/{fileId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String taskCode,
                       @PathVariable Long fileId) {
        taskFileService.deleteFile(taskCode, fileId);
    }

    @Operation(
            summary = "Возвращает файлы по задаче",
            description = "Возвращает все файлы, связанные с задачей"
    )
    @GetMapping("/{taskCode}")
    public List<TaskFileGetResponse> getByTaskCode(@PathVariable String taskCode) {
        return taskFileService.getFilesByTaskCode(taskCode);
    }

}
