package io.origemite.lib.model.enums.common;

import io.diddda.lib.common.enums.CodeValue;
import io.diddda.lib.common.exception.BizErrorException;
import io.diddda.lib.common.processor.annotation.BizEnum;
import io.diddda.lib.common.web.ResponseType;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Arrays;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@BizEnum
public enum EnOSTypeCode implements CodeValue {

    Android("OS00000AOS", "Android"),
    iOS("OS00000IOS", "iOS"),
    Mac("OS00000MAC", "Mac"),
    BlackBerry("OS00BBERRY", "BlackBerry"),
    Windows("OS00WINDOW", "Windows"),
    ETC("OS00999999", "기타"),
    ;











    final String code;
    final String value;

    @Override
    public String getCode() { return code; }

    @Override
    public String getValue() { return value; }

    public static EnOSTypeCode of(String code) {
        return Arrays.stream(EnOSTypeCode.values())
                .filter(v -> v.getCode().equals(code))
                .findAny()
                .orElseThrow(() ->
                        new BizErrorException(ResponseType.INVALID_ENUM_VALUE)
                                .addData("code", code)
                                .addData("values", Arrays.stream(values()).collect(Collectors.toMap(CodeValue::getCode, CodeValue::getValue)))
                );
    }

    public static EnOSTypeCode ofNullable(String code, EnOSTypeCode... allowStatus) {
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
