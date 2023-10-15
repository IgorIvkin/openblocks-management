package ru.openblocks.management.service.auth;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.openblocks.management.api.dto.auth.AuthRequest;
import ru.openblocks.management.api.dto.auth.AuthResponse;
import ru.openblocks.management.api.dto.auth.ValidateRequest;
import ru.openblocks.management.exception.TokenRefreshException;
import ru.openblocks.management.exception.WrongCredentialsException;
import ru.openblocks.management.service.UserDataService;

@Slf4j
@Service
public class UserAuthenticationService {

    private final UserDataService userDataService;

    private final JwtTokenService jwtTokenService;

    @Autowired
    public UserAuthenticationService(UserDataService userDataService,
                                     JwtTokenService jwtTokenService) {
        this.userDataService = userDataService;
        this.jwtTokenService = jwtTokenService;
    }

    /**
     * Checks token validity and password correctness. Issues JWT-token if both are ok.
     * Also declares a cookie with issued JWT-token.
     *
     * @param request login and password of user
     * @return JWT-token
     */
    public AuthResponse authenticate(AuthRequest request, HttpServletResponse response) {

        if (userDataService.validateUser(request)) {
            log.info("Issue the new token for user: {}", request.getUserName());
            final String token = jwtTokenService.issue(request.getUserName());
            jwtTokenService.setJwtTokenCookie(token, response);
            return new AuthResponse(token);
        }

        throw new WrongCredentialsException("Wrong credentials provided");
    }

    /**
     * Checks validity of token, returns true in case if token is correct and not expired.
     *
     * @param request JWT-token to check
     * @return true in case token is ok
     */
    public boolean validate(ValidateRequest request) {
        final String token = request.getToken();
        log.info("Validation request for token: {}", token);
        return jwtTokenService.verify(token);
    }

    /**
     * Refreshes a given token in case if it's correct but expired.
     * In case a token is incorrect it will throw an error.
     *
     * @param request JWT-token to refresh
     * @return refreshed token
     */
    public AuthResponse refresh(ValidateRequest request) {

        final String token = request.getToken();

        log.info("Refresh request for token: {}", token);

        try {
            final String refreshedToken = jwtTokenService.refresh(token);
            return new AuthResponse(refreshedToken);
        } catch (Exception ex) {
            log.error("Cannot refresh token {}, reason: {}", token, ex.getMessage());
            throw new TokenRefreshException("Cannot refresh token");
        }
    }

    /**
     * Logouts a current user, clears user session and sets expired for JWT-token cookie.
     *
     * @param response HTTP-response
     */
    public void logout(HttpServletResponse response) {
        SecurityContextHolder.getContext().setAuthentication(null);
        jwtTokenService.expireJwtTokenCookie(response);
    }
}
