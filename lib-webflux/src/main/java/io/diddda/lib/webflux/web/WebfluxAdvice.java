package io.diddda.lib.webflux.web;

import io.diddda.lib.webflux.exception.BizErrorException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@RestControllerAdvice
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
@Slf4j
public class WebfluxAdvice {

    @ExceptionHandler(BizErrorException.class)
    public Mono<ResponseEntity<CommonResponse>> handleCustomException(BizErrorException e) {
        return Mono.just(
                ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(CommonResponseUtils.response(ResponseType.BAD_REQUEST)));
    }

}
