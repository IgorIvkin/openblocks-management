package ru.openblocks.management.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.openblocks.management.api.dto.auth.AuthRequest;
import ru.openblocks.management.api.dto.auth.UserAuthenticationInfo;
import ru.openblocks.management.api.dto.common.AuthenticatedUser;
import ru.openblocks.management.api.dto.user.create.UserCreateRequest;
import ru.openblocks.management.api.dto.user.get.UserResponse;
import ru.openblocks.management.api.dto.userrole.get.UserRoleResponse;
import ru.openblocks.management.exception.DatabaseEntityAlreadyExistsException;
import ru.openblocks.management.exception.DatabaseEntityNotFoundException;
import ru.openblocks.management.mapper.UserDataMapper;
import ru.openblocks.management.mapper.UserRoleMapper;
import ru.openblocks.management.persistence.entity.UserDataEntity;
import ru.openblocks.management.persistence.entity.UserPositionEntity;
import ru.openblocks.management.persistence.entity.UserRoleEntity;
import ru.openblocks.management.persistence.repository.UserDataRepository;
import ru.openblocks.management.persistence.repository.UserPositionRepository;
import ru.openblocks.management.persistence.repository.UserRoleRepository;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserDataService {

    private final UserDataMapper userDataMapper;

    private final UserRoleMapper userRoleMapper;

    private final UserDataRepository userDataRepository;

    private final UserRoleRepository userRoleRepository;

    private final UserPositionRepository userPositionRepository;

    private final PasswordEncoder passwordEncoder;

    private final Clock clock = Clock.systemDefaultZone();

    @Autowired
    public UserDataService(UserDataMapper userDataMapper,
                           UserRoleMapper userRoleMapper,
                           UserDataRepository userDataRepository,
                           UserRoleRepository userRoleRepository,
                           UserPositionRepository userPositionRepository,
                           PasswordEncoder passwordEncoder) {
        this.userDataMapper = userDataMapper;
        this.userRoleMapper = userRoleMapper;
        this.userDataRepository = userDataRepository;
        this.userRoleRepository = userRoleRepository;
        this.userPositionRepository = userPositionRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Creates new user by a given request.
     *
     * @param request request to create new user
     * @return ID of newly created user
     */
    @Transactional
    public Long create(UserCreateRequest request) {

        log.info("Create user by request {}", request);

        // Check if such login is not presented yet
        final String login = request.login();
        userDataRepository.findByLogin(login)
                .ifPresent(user -> {
                    throw DatabaseEntityAlreadyExistsException.ofUserLogin(login);
                });

        UserDataEntity user = userDataMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreatedAt(Instant.now(clock));

        UserDataEntity savedUser = userDataRepository.save(user);
        return savedUser.getId();
    }

    /**
     * Returns user by its given ID.
     *
     * @param userId ID of user
     * @return user data by his ID
     */
    @Transactional(readOnly = true)
    public UserResponse getById(Long userId) {

        log.info("Get user by id {}", userId);

        UserDataEntity user = userDataRepository.findById(userId)
                .orElseThrow(() -> DatabaseEntityNotFoundException.ofUserId(userId));

        return userDataMapper.toDto(user);
    }

    /**
     * Returns user by its given login.
     *
     * @param login login of user
     * @return user data by his login
     */
    @Transactional(readOnly = true)
    public UserResponse getByLogin(String login) {

        log.info("Get user by login {}", login);

        UserDataEntity user = userDataRepository.findByLogin(login)
                .orElseThrow(() -> DatabaseEntityNotFoundException.ofUserLogin(login));

        return userDataMapper.toDto(user);
    }

    /**
     * Returns users by part of name.
     *
     * @param name part of name
     * @return list of user data
     */
    public List<UserResponse> getByName(String name) {

        log.info("Get users by name {}", name);

        List<UserDataEntity> users = userDataRepository.findAllByNameStartsWithIgnoreCase(name);

        return users.stream()
                .map(userDataMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Returns a list of position of user by its given ID.
     *
     * @param userId ID of user
     * @return list of positions of user
     */
    public List<UserRoleResponse> getUserRoles(Long userId) {

        log.info("Get user roles by user id {}", userId);

        userDataRepository.findById(userId)
                .orElseThrow(() -> DatabaseEntityNotFoundException.ofUserId(userId));

        List<UserPositionEntity> userPositions = userPositionRepository.findAllByUserId(userId);

        return userPositions.stream()
                .map(position -> userRoleMapper.toDto(position.getRole()))
                .collect(Collectors.toList());
    }

    /**
     * Adds a role by its ID to a user given by its ID.
     * Do nothing in case such role is already presented.
     *
     * @param roleId ID of role to add to user
     * @param userId ID of user to add a role
     */
    @Transactional
    public void addRoleToUser(Long roleId, Long userId) {

        log.info("Add role {} to user {}", roleId, userId);

        UserDataEntity user = userDataRepository.findById(userId)
                .orElseThrow(() -> DatabaseEntityNotFoundException.ofUserId(userId));
        UserRoleEntity role = userRoleRepository.findById(roleId)
                .orElseThrow(() -> DatabaseEntityNotFoundException.ofRoleId(roleId));

        // Check that this role was not presented yet
        List<UserPositionEntity> existingRoles = user.getRoles();
        for (UserPositionEntity existingRole : existingRoles) {
            if (Objects.equals(existingRole.getRole().getId(), roleId)) {
                log.info("This role {} was already presented on user {}", roleId, userId);
                return;
            }
        }

        user.addRole(role);
    }

    /**
     * Checks if the right password is passed for a given user.
     * In case user is incorrect an error returns.
     *
     * @param request authentication request
     * @return true if the password is correct
     */
    @Transactional(readOnly = true)
    public boolean validateUser(AuthRequest request) {

        final String login = request.getUserName();
        log.info("Validate user by login {}", request.getUserName());

        UserDataEntity user = userDataRepository.findByLogin(login)
                .orElseThrow(() -> DatabaseEntityNotFoundException.ofUserLogin(login));

        return passwordEncoder.matches(request.getPassword(), user.getPassword());
    }

    /**
     * Returns a currently logged user.
     *
     * @return current user
     */
    public UserDataEntity getCurrentUser() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            final Object principal = authentication.getPrincipal();
            if (principal instanceof AuthenticatedUser authenticatedUser) {
                final Long currentUserId = Optional.ofNullable(authenticatedUser.getUser())
                        .map(UserAuthenticationInfo::getId)
                        .orElse(null);
                if (currentUserId != null) {
                    return userDataRepository.findById(currentUserId)
                            .orElse(null);
                }
            }
        }

        return null;
    }

    /**
     * Returns an ID of currently identified user from security context.
     *
     * @return ID of current user
     */
    public Long getCurrentUserId() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            final Object principal = authentication.getPrincipal();
            if (principal instanceof AuthenticatedUser authenticatedUser) {
                return Optional.ofNullable(authenticatedUser.getUser())
                        .map(UserAuthenticationInfo::getId)
                        .orElse(null);
            }
        }

        return null;
    }
}
