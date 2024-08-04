package br.com.rinhabackend.configuration;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import br.com.rinhabackend.exception.BadRequestException;
import br.com.rinhabackend.exception.NotFoundException;
import br.com.rinhabackend.exception.ObjectAlreadyExistsException;
import br.com.rinhabackend.exception.UnprocessableException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class CustomRestExceptionHandler {
    private static final String MSG_ERRO_INTERNO = "Erro interno do Servidor";
    private static final String MSG_ERRO_REQUISICAO_INVALIDA = "Requisição inválida";

    @SuppressWarnings("rawtypes")
    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(
            final MethodArgumentTypeMismatchException ex) {
        Class clazz = ex.getRequiredType();

        String error = ex.getName() + " should be of type ";
        if (clazz != null) {
            error += clazz.getName();
        } else {
            error += " {{ null }}";
        }

        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST,
                MSG_ERRO_REQUISICAO_INVALIDA, error);
        return new ResponseEntity<>(apiError, new HttpHeaders(),
                apiError.getStatus());
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintViolation(
            final ConstraintViolationException ex) {
        final List<String> errors = new ArrayList<>();
        for (final ConstraintViolation<?> violation : ex
                .getConstraintViolations()) {
            errors.add(violation.getMessage());
        }

        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST,
                MSG_ERRO_REQUISICAO_INVALIDA, errors);
        return new ResponseEntity<>(apiError, new HttpHeaders(),
                apiError.getStatus());
    }

    // 401

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<Object> handleAccessDenied(final Exception ex) {
        ApiError apiError = new ApiError(HttpStatus.UNAUTHORIZED,
                "Acesso Negado", ex.getLocalizedMessage());
        return new ResponseEntity<>(apiError, new HttpHeaders(),
                apiError.getStatus());
    }

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<Object> handleNotFound(final Exception ex) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND,
                ex.getLocalizedMessage());
        return new ResponseEntity<>(apiError, new HttpHeaders(),
                apiError.getStatus());
    }

    @ExceptionHandler({UnprocessableException.class})
    public ResponseEntity<Object> handleUnprocessable(final Exception ex) {
        ApiError apiError = new ApiError(HttpStatus.UNPROCESSABLE_ENTITY,
                ex.getLocalizedMessage());
        return new ResponseEntity<>(apiError, new HttpHeaders(),
                apiError.getStatus());
    }

    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity<Object> handleBadRequest(final Exception ex) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST,
                ex.getLocalizedMessage());
        return new ResponseEntity<>(apiError, new HttpHeaders(),
                apiError.getStatus());
    }

    @ExceptionHandler({ObjectAlreadyExistsException.class})
    public ResponseEntity<Object> handleObjectAlreadyExists(
            final Exception ex) {
        ApiError apiError = new ApiError(HttpStatus.CONFLICT,
                ex.getLocalizedMessage());
        return new ResponseEntity<>(apiError, new HttpHeaders(),
                apiError.getStatus());
    }

    // 500

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleAll(final Exception ex) {
        final List<String> errors = new ArrayList<>();
        tratarErro(errors, ex);
        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR,
                MSG_ERRO_INTERNO, errors);
        return new ResponseEntity<>(apiError, new HttpHeaders(),
                apiError.getStatus());
    }

    protected void tratarErro(List<String> erros, Throwable ex) {
        if (ex.getCause() != null) {
            tratarErro(erros, ex.getCause());
        }
        erros.add(ex.getLocalizedMessage());
    }
}
