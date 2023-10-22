package ru.openblocks.management.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.openblocks.management.api.dto.taskfile.create.TaskFileCreateRequest;
import ru.openblocks.management.api.dto.taskfile.get.TaskFileGetResponse;
import ru.openblocks.management.exception.DatabaseEntityNotFoundException;
import ru.openblocks.management.exception.FileMimeTypeIsNotAllowedException;
import ru.openblocks.management.mapper.TaskFileMapper;
import ru.openblocks.management.model.file.FileStorageType;
import ru.openblocks.management.persistence.entity.TaskEntity;
import ru.openblocks.management.persistence.entity.TaskFileEntity;
import ru.openblocks.management.persistence.repository.TaskFileRepository;
import ru.openblocks.management.persistence.repository.TaskRepository;
import ru.openblocks.management.service.file.FileStorageService;
import ru.openblocks.management.service.file.FileUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TaskFileService {

    private final TaskFileRepository taskFileRepository;

    private final TaskRepository taskRepository;

    private final FileStorageService fileStorageService;

    private final UserDataService userDataService;

    private final TaskFileMapper taskFileMapper;

    private final Clock clock = Clock.systemDefaultZone();

    @Autowired
    public TaskFileService(FileStorageService fileStorageService,
                           UserDataService userDataService,
                           TaskFileRepository taskFileRepository,
                           TaskRepository taskRepository,
                           TaskFileMapper taskFileMapper) {
        this.fileStorageService = fileStorageService;
        this.userDataService = userDataService;
        this.taskFileRepository = taskFileRepository;
        this.taskRepository = taskRepository;
        this.taskFileMapper = taskFileMapper;
    }

    /**
     * Appends the file to a task by its given code.
     * Stores the file using some file storage implementation. By default, it will attempt to store
     * the file in file system.
     *
     * @param taskCode        code of task
     * @param fileInputStream content of file
     * @param request         request to create file
     * @return id of file
     */
    @Transactional
    public Long store(String taskCode, InputStream fileInputStream, TaskFileCreateRequest request) {

        log.info("Store file to task {} by request {}", taskCode, request);

        final TaskEntity task = taskRepository.findByCode(taskCode)
                .orElseThrow(() -> DatabaseEntityNotFoundException.ofTaskCode(taskCode));
        final FileStorageType fileStorageType = fileStorageService.getStorageType();
        final String fileName = request.fileName();

        // Detect file mime-type and store file in file storage
        final String mimeType;
        final String filePath;
        try {
            try (BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream)) {

                mimeType = FileUtils.getMimeType(bufferedInputStream, fileName);
                if (!fileStorageService.isAllowedMimeType(mimeType)) {
                    throw new FileMimeTypeIsNotAllowedException("Mime-type " + mimeType + " is not allowed");
                }

                // Revert back input stream to store all the content of file to file storage
                bufferedInputStream.reset();
                filePath = fileStorageService.store(bufferedInputStream);

            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        // Create task file entity, store all the required meta information also
        try {
            TaskFileEntity file = TaskFileEntity.builder()
                    .task(task)
                    .createdAt(Instant.now(clock))
                    .fileName(fileName)
                    .fileStorageType(fileStorageType)
                    .mimeType(mimeType)
                    .filePath(filePath)
                    .owner(userDataService.getCurrentUser())
                    .build();

            TaskFileEntity savedFile = taskFileRepository.save(file);
            return savedFile.getId();

        } catch (Exception ex) {
            // In case something went wrong we should delete stored file
            fileStorageService.delete(filePath);
            throw ex;
        }
    }

    /**
     * Deletes the file from task by its given code and file id.
     * Removes also the file from storage.
     *
     * @param taskCode code of task
     * @param fileId   id of file
     */
    @Transactional
    public void deleteFile(String taskCode, Long fileId) {

        log.info("Delete file {} from task {}", fileId, taskCode);

        final TaskFileEntity file = taskFileRepository.findById(fileId)
                .orElseThrow(() -> DatabaseEntityNotFoundException.ofTaskFileId(fileId));

        fileStorageService.delete(file.getFilePath());
        taskFileRepository.deleteById(fileId);
    }

    /**
     * Returns list of task files by a given code of task.
     *
     * @param taskCode code of task
     * @return task files
     */
    @Transactional(readOnly = true)
    public List<TaskFileGetResponse> getFilesByTaskCode(String taskCode) {

        log.info("Get task files by code {}", taskCode);

        if (!taskRepository.existsByCode(taskCode)) {
            throw DatabaseEntityNotFoundException.ofTaskCode(taskCode);
        }

        List<TaskFileEntity> taskFiles = taskFileRepository.findAllByTaskCodeOrderByCreatedAtDesc(taskCode);

        return taskFiles.stream()
                .map(taskFileMapper::toDto)
                .collect(Collectors.toList());
    }
}
