package io.origemite.lib.webflux.web;


import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ResponseType {
    SUCCESS(HttpStatus.OK, "CM0000", "정상 처리되었습니다."),

    /**
     * 공통
     */
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "CM0001", "잘못된 요청입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "CM0002", "인증이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "CM0003", "권한이 없습니다."),
    NOT_FOUND_RESOURCE(HttpStatus.NOT_FOUND, "CM0004", "요청한 리소스를 찾을 수 없습니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "CM0005", "허용되지 않은 메소드입니다."),
    BAD_GATEWAY(HttpStatus.BAD_GATEWAY, "CM0006", "서버와의 연결이 끊겼습니다."),
    SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "CM0007", "서비스를 사용할 수 없습니다."),
    INVALID_PARAMETER(HttpStatus.INTERNAL_SERVER_ERROR, "CM0008", "잘못된 파라미터 값입니다."),
    INVALID_DATE_FORMAT(HttpStatus.INTERNAL_SERVER_ERROR, "CM0009", "잘못된 날짜 형식이 포함되어 있습니다."),
    INVALID_ENUM_VALUE(HttpStatus.INTERNAL_SERVER_ERROR, "CM0010", "잘못된 열거형 값이 포함되어 있습니다."),
    UPLOAD_FILE_SIZE_EXCEEDED(HttpStatus.INTERNAL_SERVER_ERROR, "CM0011", "업로드 파일 크기가 초과되었습니다."),
    NOT_ALLOWED_ENUM_VALUE(HttpStatus.INTERNAL_SERVER_ERROR, "CM0012", "허용되지 않는 열거형 값이 포함되어 있습니다."),
    OBJECT_LOCKED(HttpStatus.INTERNAL_SERVER_ERROR, "CM0013", "객체가 잠겨있습니다."),
    MICRO_SERVICE_CONNECTION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "CM0014", "마이크로 서비스 연결에 실패하였습니다."),
    NOT_FOUND_SERVICE_ID(HttpStatus.INTERNAL_SERVER_ERROR, "CM0015", "서비스 아이디를 찾을 수 없습니다."),
    NOT_FOUND_CHANNEL_CODE(HttpStatus.INTERNAL_SERVER_ERROR, "CM0016", "채널코드를 찾을 수 없습니다."),
    BAD_HEADER_REQUEST(HttpStatus.BAD_REQUEST, "CM0017", "필수 해더값을 확인해 주세요"),
    /**
     * 관리자
     */
    AD_INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "AD0001", "유효하지 않은 엑세스 토큰입니다."),
    AD_INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "AD0002", "유효하지 않은 리프레시 토큰입니다."),
    AD_NOT_FOUND_ADMINISTRATOR(HttpStatus.OK, "AD0003", "관리자 정보를 찾을 수 없습니다."),
    AD_NOT_FOUND_ADMINISTRATOR_OTP_AUTHENTICATION_HISTORY(HttpStatus.OK, "AD0004", "관리자 OTP 인증 이력 정보를 찾을 수 없습니다."),
    AD_EXPIRED_ADMINISTRATOR_OTP_AUTHENTICATION_NUMBER(HttpStatus.OK, "AD0005", "OTP 인증 번호가 만료되었습니다."),
    AD_NOT_MATCH_ADMINISTRATOR_OTP_AUTHENTICATION_NUMBER(HttpStatus.OK, "AD0006", "OTP 인증 번호가 일치하지 않습니다."),
    AD_NOT_ACTIVE_ADMINISTRATOR(HttpStatus.OK, "AD0007", "활성화 되지 않은 관리자입니다."),


    UNDEFINED(HttpStatus.INTERNAL_SERVER_ERROR, "CM9998", "정의되지 않은 오류가 발생하였습니다."),
    UNKNOWN(HttpStatus.INTERNAL_SERVER_ERROR, "CM9999", "알 수 없는 오류가 발생하였습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String desc;


    ResponseType(HttpStatus httpStatus, String code, String desc) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.desc = desc;
    }


    // 메소드를 하나 만들어줘 메소드명은 of이고 AD0001, AD0001 이런 코드 값으로 enum을 찾도록
    // 만들어줘

    public static ResponseType of(String code) {
        for (ResponseType responseType : values()) {
            if (responseType.getCode().equals(code)) {
                return responseType;
            }
        }
        return UNKNOWN;
    }

}
