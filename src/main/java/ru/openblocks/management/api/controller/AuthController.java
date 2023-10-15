package ru.openblocks.management.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.openblocks.management.api.dto.auth.AuthRequest;
import ru.openblocks.management.api.dto.auth.AuthResponse;
import ru.openblocks.management.api.dto.auth.ValidateRequest;
import ru.openblocks.management.service.auth.UserAuthenticationService;

@RestController
@Tag(name = "Авторизация и проверка токена")
@CrossOrigin
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserAuthenticationService userAuthenticationService;

    @Operation(
            summary = "Выдаёт пользователю токен по логину и паролю",
            description = "Проверяет заданные логин и пароль пользователя. Если всё хорошо, выдаёт JWT-токен"
    )
    @PostMapping
    public AuthResponse auth(@RequestBody AuthRequest request, HttpServletResponse response) {
        return userAuthenticationService.authenticate(request, response);
    }

    @Operation(
            summary = "Освежает JWT-токен и возвращает новый токен, если нужно",
            description = "Проверяет заданный токен, если он подписан правильным ключом, но истёк, освежает токен и "
                    + "возвращает новый токен, если нужно"
    )
    @PostMapping("/refresh")
    public AuthResponse refresh(@RequestBody ValidateRequest request) {
        return userAuthenticationService.refresh(request);
    }
}
