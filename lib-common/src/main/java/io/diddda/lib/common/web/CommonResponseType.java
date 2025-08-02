package io.diddda.lib.common.web;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum CommonResponseType implements ResponseTypeInterface {

    DEFAULT(HttpStatus.OK, "0000", "기본값"),

    //After
    SUCCESS(HttpStatus.OK, "C000", "정상 처리"),
    EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "C999", "언노운 에러"),

    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "A001", "액세스 토큰이 유효하지 않음"),
    INVALID_ACCESS(HttpStatus.UNAUTHORIZED, "A002", "잘못된 접근"),

    BAD_REQUEST(HttpStatus.BAD_REQUEST, "V001", "잘못된 파라미터"),

    DATA_NOT_FOUND(HttpStatus.OK, "D001", "데이터 없음"),
    ALREADY_EXIST(HttpStatus.OK, "D002", "이미 존재하는 데이터"),

    INVALID_EXCEL_DATA_FORMAT(HttpStatus.OK, "E001", "데이터가 형식 유효하지 않음"),
    EXCEED_MAXIMUM_COUNT(HttpStatus.OK, "E002", "최대 개수 초과"),

    BROKEN_PIPE(HttpStatus.OK, "I001", "클라이언트 쪽 요청 종료"),
    CONNECTION_TIMEOUT(HttpStatus.OK, "I002", "연결 지연"),
    READ_TIMEOUT(HttpStatus.OK, "I003", "요청 지연"),


    // Before
    SUCCESS_BEFORE(HttpStatus.OK, "CM0000", "정상 처리되었습니다."),

    /**
     * 공통
     */
    BAD_REQUEST_BEFORE(HttpStatus.BAD_REQUEST, "CM0001", "잘못된 요청입니다."),
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

    /**
     * ATM
     */
    AT_NOT_FOUND_ATM_COLLECTION(HttpStatus.OK, "AT0001", "ATM 수거 정보를 찾을 수 없습니다."),
    AT_NOT_FOUND_ATM_MAINTENANCE_STATUS(HttpStatus.OK, "AT0002", "ATM 점검 상태 정보를 찾을 수 없습니다."),
    AT_NOT_FOUND_ATM_MAINTENANCE(HttpStatus.OK, "AT0003", "ATM 점검 정보를 찾을 수 없습니다."),
    AT_NOT_FOUND_ATM_MAINTENANCE_REQUEST_CATEGORY(HttpStatus.OK, "AT0004", "ATM 점검 요청 분류 정보를 찾을 수 없습니다."),
    AT_NOT_FOUND_ATM_COMPONENT(HttpStatus.OK, "AT0005", "ATM 구성요소 정보를 찾을 수 없습니다."),
    AT_NOT_FOUND_ATM(HttpStatus.OK, "AT0006", "ATM 정보를 찾을 수 없습니다."),
    AT_NOT_FOUND_ATM_STATUS(HttpStatus.OK, "AT0007", "ATM 상태 정보를 찾을 수 없습니다."),
    AT_NOT_FOUND_ATM_TYPE_COMPONENT(HttpStatus.OK, "AT0008", "ATM 유형 구성요소 정보를 찾을 수 없습니다."),
    AT_ALREAY_EXISTS_ATM(HttpStatus.OK, "AT0009", "이미 등록된 ATM 정보입니다."),
    AT_NOT_FOUND_ATM_MAINTEANCE_GUIDE(HttpStatus.OK, "AT0010", "ATM 점검 가이드 정보를 찾을 수 없습니다."),
    AT_NOT_FOUND_ATM_COPONENT_STOCK(HttpStatus.OK, "AT0011", "ATM 구성요소 재고 정보를 찾을 수 없습니다."),
    AT_NOT_FOUND_ATM_ACCEPTANCE_DEVICE(HttpStatus.OK, "AT0012", "ATM 수납 디바이스 정보를 찾을 수 없습니다."),
    AT_ALREAY_EXISTS_ATM_NAME_UK(HttpStatus.OK, "AT0013", "이미 등록된 ATM 키입니다."),

    /**
     * 구매
     */
    PU_NOT_FOUND_PURCHASE(HttpStatus.OK, "PU0001", "구매 정보를 찾을 수 없습니다."),
    PU_NOT_FOUND_DEVICE_MODEL_GRADE_PRICE(HttpStatus.OK, "PU0002", "디바이스 모델 등급 가격 정보를 찾을 수 없습니다."),

    /**
     * 파트너
     */
    PA_NOT_FOUND_PARTNER(HttpStatus.OK, "PA0001", "파트너 정보를 찾을 수 없습니다."),
    PA_NOT_FOUND_PARTNER_MEMBER(HttpStatus.OK, "PA0002", "파트너 회원 정보를 찾을 수 없습니다."),
    PA_NOT_FOUND_BRANCH(HttpStatus.OK, "PA0003", "지점 정보를 찾을 수 없습니다."),
    PA_DUPLICATED_MOBILE_PHONE_NUMBER(HttpStatus.OK, "PA0004", "이미 등록된 휴대폰 번호입니다."),
    PA_NOT_FOUND_PARTNER_MEMBER_BANK_ACCOUNT(HttpStatus.OK, "PA0005", "파트너 회원 은행 계좌 정보를 찾을 수 없습니다."),
    PA_ALREADY_REGIST_PARTNER_MEMBER(HttpStatus.OK, "PA0006", "이미 가입한 파트너 회원입니다."),


    /**
     * 공통
     */
    CO_NOT_FOUND_FILE(HttpStatus.OK, "CO0001", "파일을 찾을 수 없습니다."),
    CO_NOT_FOUND_COMMON_CODE(HttpStatus.OK, "CO0002", "공통 코드 정보를 찾을 수 없습니다."),
    CO_NOT_FOUND_COMMON_CODE_TYPE(HttpStatus.OK, "CO0003", "공통 코드 유형 정보를 찾을 수 없습니다."),
    CO_NOT_FOUND_API(HttpStatus.OK, "CO0004", "API 정보를 찾을 수 없습니다."),
    CO_NOT_FOUND_CONTENT_DISPLAY(HttpStatus.OK, "CO0005", "컨텐츠 노출 정보를 찾을 수 없습니다."),
    CO_NOT_FOUND_DEVICE_MANUFACTURER(HttpStatus.OK, "CO0006", "디바이스 제조사 정보를 찾을 수 없습니다."),
    CO_NOT_FOUND_DEVICE_MANUFACTURER_TYPE(HttpStatus.OK, "CO0007", "디바이스 제조사 유형 정보를 찾을 수 없습니다."),
    CO_NOT_FOUND_DEVICE_MODEL(HttpStatus.OK, "CO0008", "디바이스 모델 정보를 찾을 수 없습니다."),
    CO_NOT_FOUND_DEVICE_PETNAME(HttpStatus.OK, "CO0009", "디바이스 펫네임 정보를 찾을 수 없습니다."),
    CO_NOT_FOUND_DEVICE_SERISE(HttpStatus.OK, "CO0010", "디바이스 시리즈 정보를 찾을 수 없습니다."),
    CO_NOT_FOUND_MENU(HttpStatus.OK, "CO0011", "메뉴 정보를 찾을 수 없습니다."),
    CO_NOT_FOUND_MENU_BUTTON(HttpStatus.OK, "CO0012", "메뉴 버튼 정보를 찾을 수 없습니다."),
    CO_NOT_FOUND_SURVEY(HttpStatus.OK, "CO0013", "설문 정보를 찾을 수 없습니다."),
    CO_NOT_FOUND_SURVEY_PARTICIPANT(HttpStatus.OK, "CO0014", "설문 참여자 정보를 찾을 수 없습니다."),
    CO_NOT_FOUND_TERMS(HttpStatus.OK, "CO0015", "약관 정보를 찾을 수 없습니다."),
    CO_NOT_FOUND_TERMS_CONTENT(HttpStatus.OK, "CO0016", "약관 내용을 찾을 수 없습니다."),
    CO_NOT_FOUND_EVENT(HttpStatus.OK, "CO0017", "이벤트 정보를 찾을 수 없습니다."),
    CO_NOT_FOUND_EVENT_FILE(HttpStatus.OK, "CO0018", "이벤트 파일 정보를 찾을 수 없습니다."),
    CO_NOT_FOUND_SIGUNGU(HttpStatus.OK, "CO0019", "법정동 정보를 찾을 수 없습니다."),
    CO_ALREADY_EXISTS_COMMON_CODE(HttpStatus.OK, "CO0020", "이미 등록된 공통 코드 정보입니다."),
    CO_ALREADY_EXIST_COMMON_CODE_TYPE(HttpStatus.OK, "CO0021", "이미 등록된 공통 코드 유형 정보입니다."),
    CO_NOT_FOUND_COUPON_POLICY(HttpStatus.OK, "CO0022", "쿠폰 정책 정보를 찾을 수 없습니다."),
    // 암호화키를 찾을 수 없습니다
    CO_NOT_FOUND_CIPHER_KEY(HttpStatus.OK, "CO0023", "암호화키를 찾을 수 없습니다."),

    /**
     * 회원
     */
    ME_NOT_FOUND_PASS_IDENTIFICATION(HttpStatus.OK, "ME0001", "본인확인 정보를 찾을 수 없습니다."),
    ME_ALREAY_EXISTS_PASS_IDENTIFICATION(HttpStatus.OK, "ME0002", "이미 가입한 고객입니다.."),
    ME_NOT_FOUND_MEMBER(HttpStatus.OK, "ME0003", "회원 정보를 찾을 수 없습니다."),
    ME_INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "ME0004", "유효하지 않은 엑세스 토큰입니다."),
    ME_INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "ME0005", "유효하지 않은 리프레시 토큰입니다."),

    /**
     * 디바이스
     */
    DE_NOT_FOUND_DEVICE(HttpStatus.OK, "DE0001", "디바이스 정보를 찾을 수 없습니다."),
    DE_NOT_FOUND_DEVICE_DELETION(HttpStatus.OK, "DE0002", "디바이스 삭제 정보를 찾을 수 없습니다."),
    DE_ALREADY_SALE_DEVICE(HttpStatus.OK, "DE0003", "이미 판매된 디바이스입니다."),
    DE_ALREADY_ACCEPTANCE_DEVICE(HttpStatus.OK, "DE0004", "이미 수납된 디바이스입니다."),
    DE_NOT_FOUND_DEVICE_MODEL(HttpStatus.OK, "DE0005", "디바이스 모델 정보를 찾을 수 없습니다."),
    DE_ALREADY_EXIST_DEVICE_MODEL(HttpStatus.OK, "DE0006", "이미 등록된 디바이스 모델 정보입니다."),
    DE_ALREADY_INSPECTED_DEVICE(HttpStatus.OK, "DE0007", "이미 검사된 디바이스입니다."),
    DE_INACTIVE_DEVICE_MODEL(HttpStatus.OK, "DE0008", "비활성화된 디바이스 모델입니다."),

    /**
     * 검사
     */
    IN_NOT_FOUND_INSPECTION(HttpStatus.OK, "IN0001", "검사 정보를 찾을 수 없습니다."),
    IN_NOT_FOUND_INSPECTION_PROFILE(HttpStatus.OK, "IN0002", "검사 프로필 정보를 찾을 수 없습니다."),
    IN_NOT_FOUND_INSPECTION_SECTION(HttpStatus.OK, "IN0003", "검사 부위 정보를 찾을 수 없습니다."),
    IN_NOT_FOUND_INSPECTION_DEFECT(HttpStatus.OK, "IN0004", "검사 결함 정보를 찾을 수 없습니다."),
    IN_NOT_FOUND_INSPECTION_FILE(HttpStatus.OK, "IN0005", "검사 파일 정보를 찾을 수 없습니다."),
    IN_INSPECTION_NOT_IN_RESULT_CHECKING(HttpStatus.OK, "IN0006", "검사 결과 확인 중이 아닙니다"),
    IN_INSPECTION_NOT_IN_RESULT_WAITING(HttpStatus.OK, "IN0007", "검사 결과 확인 대기 중이 아닙니다"),

    /**
     * 창고
     */
    WA_NOT_FOUND_INVENTORY(HttpStatus.OK, "WA0001", "재고 정보를 찾을 수 없습니다."),
    WA_NOT_FOUND_BOX(HttpStatus.OK, "WA0002", "박스 정보를 찾을 수 없습니다."),

    /**
     * 통계
     */
    SA_NOT_FOUND_ATM_STATISTICS(HttpStatus.OK, "SA0001", "ATM 통계 정보를 찾을 수 없습니다."),
    SA_ALREADY_EXISTS_ATM_STATISTICS(HttpStatus.OK, "SA0002", "이미 등록된 ATM 통계 정보입니다."),

    /**
     * 레거시
     */
    LG_ERROR_SMART_VISION(HttpStatus.OK, "LG0001", "스마트비전 오류가 발생하였습니다."),


    UNDEFINED(HttpStatus.INTERNAL_SERVER_ERROR, "CM9998", "정의되지 않은 오류가 발생하였습니다."),
    UNKNOWN(HttpStatus.INTERNAL_SERVER_ERROR, "CM9999", "알 수 없는 오류가 발생하였습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String desc;


    CommonResponseType(HttpStatus httpStatus, String code, String desc) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.desc = desc;
    }

}
