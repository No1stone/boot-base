package com.origemite.lib.common.web;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.EnumSet;
import java.util.Set;

/**
 * 시스템 공통 응답 코드.
 * HTTP로 내려주기로 한 상태는 아래에 status 기준 항목으로 반드시 둔다.
 * 같은 HTTP에 대한 세부 업무 공통 코드는 그 아래에 이어서 둔다.
 */
@Getter
public enum ResponseType implements ResponseTypeInterface {

    // --- HTTP status 기준 (지원 세트) ---

    // 2xx
    SUCCESS(HttpStatus.OK, "CM0000", "정상 처리되었습니다."),                         // 200
    CREATED(HttpStatus.CREATED, "CM0201", "생성되었습니다."),                         // 201
    ACCEPTED(HttpStatus.ACCEPTED, "CM0202", "요청이 접수되었습니다."),                 // 202
    NO_CONTENT(HttpStatus.NO_CONTENT, "CM0204", "응답 본문이 없습니다."),               // 204

    // 4xx
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "CM0001", "잘못된 요청입니다."),                 // 400
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "CM0002", "인증이 필요합니다."),             // 401
    FORBIDDEN(HttpStatus.FORBIDDEN, "CM0003", "권한이 없습니다."),                     // 403
    NOT_FOUND_RESOURCE(HttpStatus.NOT_FOUND, "CM0004", "요청한 리소스를 찾을 수 없습니다."), // 404
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "CM0005", "허용되지 않은 메소드입니다."), // 405
    NOT_ACCEPTABLE(HttpStatus.NOT_ACCEPTABLE, "CM0406", "허용되지 않는 응답 형식입니다."), // 406
    CONFLICT(HttpStatus.CONFLICT, "CM0409", "요청이 현재 상태와 충돌합니다."),           // 409
    GONE(HttpStatus.GONE, "CM0410", "요청한 리소스가 더 이상 없습니다."),                 // 410
    PAYLOAD_TOO_LARGE(HttpStatus.PAYLOAD_TOO_LARGE, "CM0413", "요청 본문 크기가 너무 큽니다."), // 413
    UNSUPPORTED_MEDIA_TYPE(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "CM0415", "지원하지 않는 미디어 타입입니다."), // 415
    UNPROCESSABLE_ENTITY(HttpStatus.UNPROCESSABLE_ENTITY, "CM0422", "요청은 이해했지만 처리할 수 없습니다."), // 422
    TOO_MANY_REQUESTS(HttpStatus.TOO_MANY_REQUESTS, "CM0429", "요청이 너무 많습니다."), // 429

    // 5xx
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "CM0500", "서버 오류가 발생하였습니다."), // 500
    NOT_IMPLEMENTED(HttpStatus.NOT_IMPLEMENTED, "CM0501", "구현되지 않은 기능입니다."), // 501
    BAD_GATEWAY(HttpStatus.BAD_GATEWAY, "CM0006", "서버와의 연결이 끊겼습니다."),         // 502
    SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "CM0007", "서비스를 사용할 수 없습니다."), // 503
    GATEWAY_TIMEOUT(HttpStatus.GATEWAY_TIMEOUT, "CM0504", "게이트웨이 응답 시간이 초과되었습니다."), // 504

    // --- 동일 HTTP에 대한 공통 세부 코드 ---
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "CM0008", "잘못된 파라미터 값입니다."),     // 400
    INVALID_DATE_FORMAT(HttpStatus.BAD_REQUEST, "CM0009", "잘못된 날짜 형식이 포함되어 있습니다."), // 400
    INVALID_ENUM_VALUE(HttpStatus.BAD_REQUEST, "CM0010", "잘못된 열거형 값이 포함되어 있습니다."), // 400
    UPLOAD_FILE_SIZE_EXCEEDED(HttpStatus.PAYLOAD_TOO_LARGE, "CM0011", "업로드 파일 크기가 초과되었습니다."), // 413
    NOT_ALLOWED_ENUM_VALUE(HttpStatus.BAD_REQUEST, "CM0012", "허용되지 않는 열거형 값이 포함되어 있습니다."), // 400
    OBJECT_LOCKED(HttpStatus.CONFLICT, "CM0013", "객체가 잠겨있습니다."),                 // 409
    MICRO_SERVICE_CONNECTION_FAILED(HttpStatus.BAD_GATEWAY, "CM0014", "마이크로 서비스 연결에 실패하였습니다."), // 502
    NOT_FOUND_SERVICE_ID(HttpStatus.BAD_REQUEST, "CM0015", "서비스 아이디를 찾을 수 없습니다."), // 400
    NOT_FOUND_CHANNEL_CODE(HttpStatus.BAD_REQUEST, "CM0016", "채널코드를 찾을 수 없습니다."), // 400

    UNDEFINED(HttpStatus.INTERNAL_SERVER_ERROR, "CM9998", "정의되지 않은 오류가 발생하였습니다."), // 500
    UNKNOWN(HttpStatus.INTERNAL_SERVER_ERROR, "CM9999", "알 수 없는 오류가 발생하였습니다."); // 500

    /**
     * HTTP status당 대표 항목. {@link #ofHttpStatus(HttpStatus)} 조회에 사용한다.
     */
    private static final Set<ResponseType> STATUS_BASELINE = EnumSet.of(
            SUCCESS,
            CREATED,
            ACCEPTED,
            NO_CONTENT,
            BAD_REQUEST,
            UNAUTHORIZED,
            FORBIDDEN,
            NOT_FOUND_RESOURCE,
            METHOD_NOT_ALLOWED,
            NOT_ACCEPTABLE,
            CONFLICT,
            GONE,
            PAYLOAD_TOO_LARGE,
            UNSUPPORTED_MEDIA_TYPE,
            UNPROCESSABLE_ENTITY,
            TOO_MANY_REQUESTS,
            INTERNAL_SERVER_ERROR,
            NOT_IMPLEMENTED,
            BAD_GATEWAY,
            SERVICE_UNAVAILABLE,
            GATEWAY_TIMEOUT
    );

    private final HttpStatus httpStatus;
    private final String code;
    private final String desc;

    ResponseType(HttpStatus httpStatus, String code, String desc) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.desc = desc;
    }

    public static ResponseType of(String code) {
        for (ResponseType responseType : values()) {
            if (responseType.getCode().equals(code)) {
                return responseType;
            }
        }
        return UNKNOWN;
    }

    public static ResponseType ofHttpStatus(HttpStatus httpStatus) {
        for (ResponseType responseType : STATUS_BASELINE) {
            if (responseType.getHttpStatus() == httpStatus) {
                return responseType;
            }
        }
        return UNKNOWN;
    }

}
