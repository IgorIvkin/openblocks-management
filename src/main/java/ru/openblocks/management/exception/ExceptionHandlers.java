package ru.openblocks.management.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.openblocks.management.api.dto.common.ErrorResponse;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlers {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse databaseEntityNotFoundException(DatabaseEntityNotFoundException ex) {
        log.warn("404 Not Found - {}", getExceptionMessage(ex));
        return handleException(ex, HttpStatus.NOT_FOUND.value());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse databaseEntityAlreadyExistsException(DatabaseEntityAlreadyExistsException ex) {
        log.warn("409 Conflict - {}", getExceptionMessage(ex));
        return handleException(ex, HttpStatus.CONFLICT.value());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        log.warn("400 Bad Request - {}", getExceptionMessage(ex));
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), ex.getName());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.warn("400 Bad Request - {}", getExceptionMessage(ex));
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), ex.getObjectName());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse httpMessageNotReadableException(HttpMessageNotReadableException ex) {
        log.warn("400 Bad Request - {}", getExceptionMessage(ex));
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), null);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse fileMimeTypeIsNotAllowedException(FileMimeTypeIsNotAllowedException ex) {
        log.warn("400 Bad Request - {}", getExceptionMessage(ex));
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), null);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse wrongCredentialsException(WrongCredentialsException ex) {
        log.warn("401 Unauthorized - {}", getExceptionMessage(ex));
        return new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), ex.getMessage(), null);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse noUserRightsException(NoUserRightsException ex) {
        log.warn("401 Forbidden - {}", getExceptionMessage(ex));
        return new ErrorResponse(HttpStatus.FORBIDDEN.value(), ex.getMessage(), null);
    }

    private ErrorResponse handleException(Throwable ex, Integer code) {
        final String message = ex.getMessage();
        if (message != null) {
            return new ErrorResponse(code, message);
        }
        return new ErrorResponse(code, "An error with null message occurred: " + ex);
    }

    private String getExceptionMessage(Throwable ex) {
        if (ex.getMessage() != null) {
            return ex.getMessage();
        }
        return "";
    }
}
