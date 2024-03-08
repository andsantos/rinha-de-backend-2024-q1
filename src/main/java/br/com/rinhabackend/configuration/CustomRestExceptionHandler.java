package br.com.rinhabackend.configuration;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.com.rinhabackend.exception.BadRequestException;
import br.com.rinhabackend.exception.NotFoundException;
import br.com.rinhabackend.exception.ObjectAlreadyExistsException;
import br.com.rinhabackend.exception.UnprocessableException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {
    private static final String MSG_ERRO_INTERNO = "Erro interno do Servidor";
    private static final String MSG_ERRO_REQUISICAO_INVALIDA = "Requisição inválida";

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        logger.info(ex.getClass().getName());
        final String error = "Formato inválido";
        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, MSG_ERRO_REQUISICAO_INVALIDA, error);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        logger.info(ex.getClass().getName());

        final List<String> errors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }
        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, MSG_ERRO_REQUISICAO_INVALIDA, errors);
        return handleExceptionInternal(ex, apiError, headers, apiError.getStatus(), request);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
            HttpStatusCode status, WebRequest request) {
        logger.info(ex.getClass().getName());

        final String error = ex.getValue() + " value for " + ex.getPropertyName() + " should be of type "
                + ex.getRequiredType();

        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, MSG_ERRO_REQUISICAO_INVALIDA, error);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(MissingServletRequestPartException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        logger.info(ex.getClass().getName());

        final String error = ex.getRequestPartName() + " part is missing";
        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, MSG_ERRO_REQUISICAO_INVALIDA, error);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        logger.info(ex.getClass().getName());

        final String error = ex.getParameterName() + " parameter is missing";
        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, MSG_ERRO_REQUISICAO_INVALIDA, error);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @SuppressWarnings("rawtypes")
    @ExceptionHandler({ MethodArgumentTypeMismatchException.class })
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(final MethodArgumentTypeMismatchException ex,
            final WebRequest request) {
        logger.info(ex.getClass().getName());
        Class clazz = ex.getRequiredType();

        String error = ex.getName() + " should be of type ";
        if (clazz != null) {
            error += clazz.getName();
        } else {
            error += " {{ null }}";
        }

        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, MSG_ERRO_REQUISICAO_INVALIDA, error);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({ ConstraintViolationException.class })
    public ResponseEntity<Object> handleConstraintViolation(final ConstraintViolationException ex,
            final WebRequest request) {
        logger.info(ex.getClass().getName());

        final List<String> errors = new ArrayList<>();
        for (final ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.add(violation.getMessage());
        }

        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, MSG_ERRO_REQUISICAO_INVALIDA, errors);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    // 401

    @ExceptionHandler({ AccessDeniedException.class })
    public ResponseEntity<Object> handleAccessDenied(final Exception ex, final WebRequest request) {
        logger.error("handleBadCredentials", ex);
        ApiError apiError = new ApiError(HttpStatus.UNAUTHORIZED, "Acesso Negado", ex.getLocalizedMessage());
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    // 404

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
            HttpStatusCode status, WebRequest request) {
        logger.info(ex.getClass().getName());

        final String error = "No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL();

        final ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getLocalizedMessage(), error);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    // 405

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        logger.info(ex.getClass().getName());

        final StringBuilder builder = new StringBuilder();
        builder.append(ex.getMethod());
        builder.append(" method is not supported for this request. Supported methods are ");
        Set<HttpMethod> set = ex.getSupportedHttpMethods();

        if (set != null) {
            set.forEach(t -> builder.append(t + " "));
        }

        final ApiError apiError = new ApiError(HttpStatus.METHOD_NOT_ALLOWED, ex.getLocalizedMessage(),
                builder.toString());
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    // 415

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        logger.info(ex.getClass().getName());

        final StringBuilder builder = new StringBuilder();
        builder.append(ex.getContentType());
        builder.append(" media type is not supported. Supported media types are ");

        ex.getSupportedMediaTypes().forEach(t -> builder.append(t + " "));

        final ApiError apiError = new ApiError(HttpStatus.UNSUPPORTED_MEDIA_TYPE, ex.getLocalizedMessage(),
                builder.substring(0, builder.length() - 2));
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({ NotFoundException.class })
    public ResponseEntity<Object> handleNotFound(final Exception ex, final WebRequest request) {
        logger.error("handleNotFound", ex);
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getLocalizedMessage());
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({ UnprocessableException.class })
    public ResponseEntity<Object> handleUnprocessable(final Exception ex, final WebRequest request) {
        logger.error("handleNotFound", ex);
        ApiError apiError = new ApiError(HttpStatus.UNPROCESSABLE_ENTITY, ex.getLocalizedMessage());
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({ BadRequestException.class })
    public ResponseEntity<Object> handleBadRequest(final Exception ex, final WebRequest request) {
        logger.error("handleBadRequest", ex);
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage());
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({ ObjectAlreadyExistsException.class })
    public ResponseEntity<Object> handleObjectAlreadyExists(final Exception ex, final WebRequest request) {
        logger.error("handleObjectAlreadyExists", ex);
        ApiError apiError = new ApiError(HttpStatus.CONFLICT, ex.getLocalizedMessage());
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    // 500

    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Object> handleAll(final Exception ex, final WebRequest request) {
        logger.error("error", ex);
        final List<String> errors = new ArrayList<>();
        tratarErro(errors, ex);
        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, MSG_ERRO_INTERNO, errors);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    protected void tratarErro(List<String> erros, Throwable ex) {
        if (ex.getCause() != null) {
            tratarErro(erros, ex.getCause());
        }
        erros.add(ex.getLocalizedMessage());
    }
}
