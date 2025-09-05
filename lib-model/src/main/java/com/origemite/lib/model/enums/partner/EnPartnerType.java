package com.origemite.lib.model.enums.partner;

import com.origemite.lib.common.enums.CodeValue;
import com.origemite.lib.common.exception.BizErrorException;
import com.origemite.lib.common.processor.annotation.BizEnum;
import com.origemite.lib.common.web.ResponseType;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Arrays;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@BizEnum
public enum EnPartnerType implements CodeValue {

    LSP("PTNR000LSP", "레거시 서비스 공급"),
    PP("PTNR0000PP", "플랫폼 제공"),
    CP("PTNR0000CP", "콘텐츠 제공"),
    PS("PTNR0000PS", "제품/상품 공급"),
    SS("PTNR0000SS", "서비스 공급"),
    PO("PTNR0000PO", "플랫폼 (서비스) 운영"),
    CC("PTNR0000CC", "고객 센터"),
    PM("PTNR0000PM", "플랫폼 관리"),
    SM("PTNR0000SM", "서비스 관리"),
    OM("PTNR0000OM", "운영 관리"),
    ALIANC("PTNRALIANC", "ATM 설치 공간 관리"),
    PRCH("PTNR00PRCH", "매입 관리"),
    WM("PTNR0000WM", "물류 관리"),
    SALES("PTNR0SALES", "판매 관리"),
    BIDING("PTNRBIDING", "경매 관리"),
    AS("PTNR0000AS", "ATM 관리"),
    RFBS("PTNR00RFBS", "리퍼비시 관리"),
    CUST("PTNR00CUST", "고객 센터 관리"),
    COLLEC("PTNRCOLLEC", "수거 관리"),
    RENTAL("PTNRRENTAL", "렌탈 거래처"),
    ETC("PTNR000ETC", "기타 거래처");

    final String code;
    final String value;

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getValue() {
        return value;
    }

    public static EnPartnerType of(String code) {
        return Arrays.stream(EnPartnerType.values())
                .filter(v -> v.getCode().equals(code))
                .findAny()
                .orElseThrow(() ->
                        new BizErrorException(ResponseType.INVALID_ENUM_VALUE)
                                .addData("code", code)
                                .addData("values", Arrays.stream(values())
                                        .collect(Collectors.toMap(CodeValue::getCode, CodeValue::getValue)))
                );
    }

    public static EnPartnerType ofNullable(String code, EnPartnerType... allowStatus) {
        if (code == null) return null;
        if (allowStatus != null && allowStatus.length > 0) {
            if (Arrays.stream(allowStatus).noneMatch(v -> v.getCode().equals(code))) {
                throw new BizErrorException(ResponseType.NOT_ALLOWED_ENUM_VALUE)
                        .addData("code", code)
                        .addData("values", Arrays.stream(allowStatus)
                                .collect(Collectors.toMap(CodeValue::getCode, CodeValue::getValue)));
            }
        }
        return of(code);
    }
}
