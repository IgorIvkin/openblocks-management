package ru.openblocks.management.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.openblocks.management.api.dto.userrole.create.UserRoleCreateRequest;
import ru.openblocks.management.api.dto.userrole.get.UserRoleResponse;
import ru.openblocks.management.api.dto.userrole.update.UserRoleUpdateRequest;
import ru.openblocks.management.exception.DatabaseEntityNotFoundException;
import ru.openblocks.management.mapper.UserRoleMapper;
import ru.openblocks.management.persistence.entity.UserRoleEntity;
import ru.openblocks.management.persistence.repository.UserRoleRepository;

@Slf4j
@Service
public class UserRoleService {

    private final UserRoleRepository userRoleRepository;

    private final UserRoleMapper userRoleMapper;

    @Autowired
    public UserRoleService(UserRoleMapper userRoleMapper,
                           UserRoleRepository userRoleRepository) {
        this.userRoleMapper = userRoleMapper;
        this.userRoleRepository = userRoleRepository;
    }

    /**
     * Creates new user role by request.
     *
     * @param request request to create new user role
     * @return ID of newly created role
     */
    @Transactional
    public Long create(UserRoleCreateRequest request) {

        log.info("Create user role by request {}", request);

        UserRoleEntity userRole = userRoleMapper.toEntity(request);
        UserRoleEntity createdRole = userRoleRepository.save(userRole);

        return createdRole.getId();
    }

    /**
     * Updates a user role by given ID and request.
     *
     * @param roleId  ID of user role to update
     * @param request request to update
     */
    @Transactional
    public void update(Long roleId, UserRoleUpdateRequest request) {

        log.info("Update user role with id {} by request {}", roleId, request);

        UserRoleEntity userRole = userRoleRepository.findById(roleId)
                .orElseThrow(() -> DatabaseEntityNotFoundException.ofRoleId(roleId));

        userRoleMapper.updateEntity(userRole, request);
    }

    /**
     * Returns user role by its ID.
     *
     * @param roleId ID of user role
     * @return user role defined by its ID
     */
    @Transactional(readOnly = true)
    public UserRoleResponse getById(Long roleId) {

        log.info("Get user role by id {}", roleId);

        UserRoleEntity userRole = userRoleRepository.findById(roleId)
                .orElseThrow(() -> DatabaseEntityNotFoundException.ofRoleId(roleId));

        return userRoleMapper.toDto(userRole);
    }

    /**
     * Returns user role by its code.
     *
     * @param code code of user role
     * @return user role defined by its code
     */
    @Transactional(readOnly = true)
    public UserRoleResponse getByCode(String code) {

        log.info("Get user role by code {}", code);

        UserRoleEntity userRole = userRoleRepository.findByCode(code)
                .orElseThrow(() -> DatabaseEntityNotFoundException.ofRoleCode(code));

        return userRoleMapper.toDto(userRole);
    }

}
