package com.origemite.lib.model.enums.common;

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
public enum EnCurrencyCode implements CodeValue {

    // TODO: 항목 채우기
//    KRW("CURR000KRW", "한국원"),
//    USD("CURR000USD", "미국달러"),
//    PHP("CURR000PHP", "필리핀페소"),
//    CNY("CURR000CNY", "중국위안"),
//    JPY("CURR000JPY", "일본엔"),
    KRW("KRW", "한국원"),
    USD("USD", "미국달러"),
    PHP("PHP", "필리핀페소"),
    CNY("CNY", "중국위안"),
    JPY("JPY", "일본엔"),
    ;

    final String code;
    final String value;

    @Override
    public String getCode() { return code; }

    @Override
    public String getValue() { return value; }

    public static EnCurrencyCode of(String code) {
        return Arrays.stream(EnCurrencyCode.values())
                .filter(v -> v.getCode().equals(code))
                .findAny()
                .orElseThrow(() ->
                        new BizErrorException(ResponseType.INVALID_ENUM_VALUE)
                                .addData("code", code)
                                .addData("values", Arrays.stream(values()).collect(Collectors.toMap(CodeValue::getCode, CodeValue::getValue)))
                );
    }

    public static EnCurrencyCode ofNullable(String code, EnCurrencyCode... allowStatus) {
        if (code == null) return null;
        if (allowStatus != null && allowStatus.length > 0) {
            if (Arrays.stream(allowStatus).noneMatch(v -> v.getCode().equals(code))) {
                throw new BizErrorException(ResponseType.NOT_ALLOWED_ENUM_VALUE)
                        .addData("code", code)
                        .addData("values", Arrays.stream(allowStatus).collect(Collectors.toMap(CodeValue::getCode, CodeValue::getValue)));
            }
        }
        return of(code);
    }
}
