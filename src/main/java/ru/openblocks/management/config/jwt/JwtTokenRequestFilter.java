package ru.openblocks.management.config.jwt;

import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.openblocks.management.api.dto.auth.UserAuthenticationInfo;
import ru.openblocks.management.api.dto.common.AuthenticatedUser;
import ru.openblocks.management.api.dto.user.get.UserResponse;
import ru.openblocks.management.api.dto.userrole.get.UserRoleResponse;
import ru.openblocks.management.service.auth.JwtTokenService;
import ru.openblocks.management.service.UserDataService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenRequestFilter extends OncePerRequestFilter {

    private final JwtTokenService jwtTokenService;

    private UserDataService userService;

    @Autowired
    public JwtTokenRequestFilter(JwtTokenService jwtTokenService) {
        this.jwtTokenService = jwtTokenService;
    }

    @Lazy
    @Autowired
    public void setUserService(UserDataService userService) {
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        log.info("JwtTokenRequestFilter - start filtering, request: {}", request.getRequestURI());

        final String token = refreshTokenIfRequired(jwtTokenService.extractJwtToken(request), response);
        if (token != null) {
            log.debug("Token received: {}", token);
            if (jwtTokenService.verify(token)) {
                log.debug("Token is valid: {}", token);

                // Создаём и размещаем в контексте безопасности текущего пользователя
                final DecodedJWT jwtClaims = jwtTokenService.decode(token);
                final String userName = jwtClaims.getSubject();
                final UserAuthenticationInfo user = mapToAuthenticationInfo(userService.getByLogin(userName));
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(
                                AuthenticatedUser.builder()
                                        .userName(userName)
                                        .user(user)
                                        .token(token)
                                        .build(),
                                null,
                                setGrantedAuthorities(user)
                        );
                usernamePasswordAuthenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

                // Сохраняем в MDC текущий логин для использования в логах
                MDC.put("userId", userName);
            }
        } else {
            log.info("Request without token, nothing to filter");
        }

        log.info("JwtTokenRequestFilter - finish filtering, request: {}", request.getRequestURI());

        // Продолжаем цепочку фильтрации
        filterChain.doFilter(request, response);
    }

    private String refreshTokenIfRequired(final String token, final HttpServletResponse response) {
        if (StringUtils.hasText(token)) {
            try {
                final String refreshedToken = jwtTokenService.refresh(token);
                if (!refreshedToken.equals(token)) {
                    jwtTokenService.setJwtTokenCookie(refreshedToken, response);
                }
                return refreshedToken;
            } catch (Exception ex) {
                log.error("Cannot refresh token {}, reason {}", token, ex.getMessage());
                return null;
            }
        }
        return null;
    }

    private UserAuthenticationInfo mapToAuthenticationInfo(UserResponse user) {
        return UserAuthenticationInfo.builder()
                .id(user.id())
                .name(user.name())
                .login(user.login())
                .build();
    }

    private List<GrantedAuthority> setGrantedAuthorities(UserAuthenticationInfo user) {
        final Long userId = user.getId();
        final List<UserRoleResponse> userRoles = userService.getUserRoles(userId);
        return userRoles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.code()))
                .collect(Collectors.toList());
    }

}
