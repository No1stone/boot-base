package com.origemite.lib.common.web;

import com.origemite.lib.common.exception.BizErrorException;
import com.origemite.lib.common.exception.BizWarnException;
import com.origemite.lib.common.util.ThrowableUtils;
import feign.RetryableException;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ValidationException;
import com.origemite.lib.common.util.CommonResponseUtils;
import com.origemite.lib.common.util.ProfileUtils;
import com.origemite.lib.common.util.MessageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.*;
import java.util.stream.Collectors;

import static com.origemite.lib.common.util.MessageUtils.getExceptionMessage;


@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
@Hidden
public class CommonExceptionAdvice {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CommonResponse<?> handleException(Exception e, HttpServletRequest request, HttpServletResponse response) {
        log.error(ExceptionUtils.getStackTrace(e));

        BizErrorException bizErrorException = ThrowableUtils.findCauseByType(e, BizErrorException.class, 2);
        if (bizErrorException != null) {
            return handleBizErrorException(bizErrorException, request, response);
        }

        if (ProfileUtils.isLocal() || ProfileUtils.isDev()) {
            return CommonResponseUtils.response(ResponseType.UNKNOWN, e.getMessage());
        } else {
            return CommonResponseUtils.response(ResponseType.UNKNOWN);
        }
    }

    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public CommonResponse<?> handleNoResourceFoundException(HttpServletRequest request, NoResourceFoundException e) {
        log.warn(ExceptionUtils.getStackTrace(e));
        return CommonResponseUtils.response(ResponseType.NOT_FOUND_RESOURCE, request.getRequestURI());
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CommonResponse<?> handleIllegalStateException(HttpServletRequest request, IllegalStateException e) {
        log.warn(ExceptionUtils.getStackTrace(e));
        return CommonResponseUtils.response(ResponseType.UNKNOWN, e.getMessage());
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public CommonResponse<?> handleAuthenticationException(HttpServletRequest request, AuthenticationException e) {
        log.warn(ExceptionUtils.getStackTrace(e));
        return CommonResponseUtils.response(ResponseType.UNAUTHORIZED, getExceptionMessage(AuthenticationException.class));
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public CommonResponse<?> handleAccessDeniedException(HttpServletRequest request, AccessDeniedException e) {
        log.warn(ExceptionUtils.getStackTrace(e));
        return CommonResponseUtils.response(ResponseType.FORBIDDEN, getExceptionMessage(AccessDeniedException.class));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResponse<?> handleMethodArgumentNotValidException(HttpServletRequest request, MethodArgumentNotValidException e) {

        log.warn(ExceptionUtils.getStackTrace(e));

        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        if (!fieldErrors.isEmpty()) {

            FieldError fieldError = e.getBindingResult().getFieldErrors().get(fieldErrors.size() - 1);

            List<Object> auguments = new ArrayList<>();

            if (Optional.ofNullable(fieldError.getArguments()).isPresent()) {
                auguments = Arrays.stream(fieldError.getArguments()).skip(1).collect(Collectors.toList());
                Collections.reverse(auguments);
            }

            ValidationResponse validationResponse = ValidationResponse.builder()
                    .field(fieldError.getField())
                    .value(fieldError.getRejectedValue())
                    .error(MessageUtils.getMessage(fieldError.getCode(), auguments.toArray(Object[]::new)))
                    .args(Arrays.stream(fieldError.getArguments()).skip(1).collect(Collectors.toList()))
                    .build();

            return CommonResponseUtils.response(SystemServiceModule.DEFAULT, ResponseType.INVALID_PARAMETER, validationResponse);
        }

        return CommonResponseUtils.response(ResponseType.INVALID_PARAMETER);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResponse<?> handleMethodArgumentTypeMismatchException(HttpServletRequest request, MethodArgumentTypeMismatchException e) {
        log.warn(ExceptionUtils.getStackTrace(e));
        return CommonResponseUtils.response(ResponseType.UNKNOWN);
    }

    @ExceptionHandler(BizWarnException.class)
    @ResponseBody
    public CommonResponse<?> handleBizWarnException(HttpServletRequest request, BizWarnException e, HttpServletResponse response) {
        log.warn(ExceptionUtils.getStackTrace(e));
        response.setStatus(e.getHttpStatus().value());
        return CommonResponseUtils.response(e.getSystemServiceModule(), e.getResponseType(), e.getErrorMessage());
    }

    @ExceptionHandler(BizErrorException.class)
    public CommonResponse<?> handleBizErrorException(BizErrorException e, HttpServletRequest request, HttpServletResponse response) {

        log.error(ExceptionUtils.getStackTrace(e));

        response.setStatus(e.getHttpStatus().value());
        return CommonResponseUtils.response(e.getSystemServiceModule(), e.getResponseType(), e.getData());
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResponse<?> handleValidationException(HttpServletRequest request, ValidationException e) {
        log.warn(ExceptionUtils.getStackTrace(e));
        if (e.getCause() instanceof BizWarnException warningException) {
            return CommonResponseUtils.response(
                    warningException.getSystemServiceModule(),
                    warningException.getResponseType(),
                    warningException.getErrorMessage()
            );
        }
        return CommonResponseUtils.response(ResponseType.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CommonResponse<?> handleMaxUploadSizeExceededException(HttpServletRequest request, MaxUploadSizeExceededException e) {
        log.error(ExceptionUtils.getStackTrace(e));
        return CommonResponseUtils.response(ResponseType.UPLOAD_FILE_SIZE_EXCEEDED);
    }

    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CommonResponse<?> handleObjectOptimisticLockingFailureException(HttpServletRequest request, ObjectOptimisticLockingFailureException e) {
        log.error(ExceptionUtils.getStackTrace(e));
        return CommonResponseUtils.response(ResponseType.OBJECT_LOCKED);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public CommonResponse<?> handleNoHandlerFoundException(HttpServletRequest request, NoHandlerFoundException e) {
        log.error(ExceptionUtils.getStackTrace(e));
        return CommonResponseUtils.response(ResponseType.NOT_FOUND_RESOURCE);
    }

    @ExceptionHandler(RetryableException.class)
    public CommonResponse<?> handleRetryableException(HttpServletRequest request, RetryableException e) {
        log.error(ExceptionUtils.getStackTrace(e));
        return CommonResponseUtils.response(ResponseType.MICRO_SERVICE_CONNECTION_FAILED);
    }
}
