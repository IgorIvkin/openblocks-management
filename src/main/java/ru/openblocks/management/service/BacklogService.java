package ru.openblocks.management.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.openblocks.management.api.dto.backlog.get.BacklogGetRequest;
import ru.openblocks.management.api.dto.backlog.get.BacklogGetResponse;
import ru.openblocks.management.exception.NoUserRightsException;
import ru.openblocks.management.mapper.BacklogMapper;
import ru.openblocks.management.persistence.projection.BacklogProjection;
import ru.openblocks.management.persistence.repository.BacklogRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BacklogService {

    private final UserDataService userDataService;

    private final ProjectAccessService projectAccessService;

    private final BacklogRepository backlogRepository;

    private final BacklogMapper backlogMapper;

    @Autowired
    public BacklogService(UserDataService userDataService,
                          ProjectAccessService projectAccessService,
                          BacklogRepository backlogRepository,
                          BacklogMapper backlogMapper) {
        this.userDataService = userDataService;
        this.projectAccessService = projectAccessService;
        this.backlogRepository = backlogRepository;
        this.backlogMapper = backlogMapper;
    }

    /**
     * Returns a backlog of tasks defined by request.
     * Currently following filters are supported:
     * - project code
     * - executor id
     * - subject (part of title)
     *
     * @param request request to get backlog tasks
     * @return tasks filtered by request
     */
    @Transactional(readOnly = true)
    public List<BacklogGetResponse> getBacklog(BacklogGetRequest request) {

        final Long userId = userDataService.getCurrentUserId();

        log.info("Get backlog for user {} by request {}", userId, request);

        checkProjectAccess(userId, request);
        List<BacklogProjection> backlogItems = backlogRepository.getBacklog(request);

        return backlogItems.stream()
                .map(backlogMapper::toDto)
                .collect(Collectors.toList());
    }

    private void checkProjectAccess(Long userId, BacklogGetRequest request) {
        final String projectCode = request.projectCode();
        if (projectCode != null) {
            if (!projectAccessService.hasAccess(userId, projectCode)) {
                throw NoUserRightsException.noAccessToProject(projectCode);
            }
        }
    }
}
