package ru.openblocks.management.config.jwt;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import ru.openblocks.management.config.http.CookieConfig;
import ru.openblocks.management.service.auth.JwtTokenService;

import java.util.stream.Stream;

/**
 * Вспомогательный класс, предназначенный для извлечения JWT-токена из разных источников запроса.
 */
@Component
public class JwtTokenResolver {

    /**
     * Извлекает JWT-токен из различных источников. Сначала пробуем извлечь из заголовка
     * Authorization: Bearer [[token]]. Если там пусто, то пробуем извлечь токен из кук запроса.
     *
     * @param request HTTP-запрос к сервлету
     * @return JWT-токен
     */
    public String resolve(final HttpServletRequest request) {
        String token = fromAuthorizationHeader(request.getHeader(HttpHeaders.AUTHORIZATION));
        if (token != null) {
            return token;
        } else {
            return fromCookies(request.getCookies());
        }
    }

    /**
     * Извлекает JWT-токен из заголовка Authorization: Bearer [[token]].
     *
     * @param authorizationHeader заголовок Authorization
     * @return JWT-токен
     */
    private String fromAuthorizationHeader(final String authorizationHeader) {
        if (authorizationHeader != null) {
            if (authorizationHeader.startsWith(JwtTokenService.BEARER)) {
                return authorizationHeader.substring(JwtTokenService.BEARER.length());
            }
        }
        return null;
    }

    /**
     * Извлекает JWT-токен из куки с именем "jwtToken".
     *
     * @param cookies список куки из HTTP-запроса
     * @return JWT-токен
     */
    private String fromCookies(final Cookie[] cookies) {
        if (cookies != null) {
            return Stream.of(cookies)
                    .filter(cookie -> CookieConfig.JWT_TOKEN_COOKIE_NAME.equals(cookie.getName()))
                    .map(Cookie::getValue)
                    .findFirst()
                    .orElse(null);
        } else {
            return null;
        }
    }
}
