package io.origemite.lib.model.enums.common;

import com.fasterxml.jackson.annotation.JsonValue;
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
public enum EnStatus implements CodeValue {

    ACTIVATED("A", "활성"),
    DEACTIVATED("D", "비활성"),
    ;

    final String code;
    final String value;

    @Override
    @JsonValue
    public String getCode() {
        return code;
    }

    @Override
    public String getValue() {
        return value;
    }

    public static EnStatus of(String code) {
        return Arrays.stream(EnStatus.values())
                .filter(v -> v.getCode().equals(code))
                .findAny()
                .orElseThrow(() ->
                        new BizErrorException(ResponseType.INVALID_ENUM_VALUE)
                                .addData("code", code)
                                .addData("values", Arrays.stream(values()).collect(Collectors.toMap(CodeValue::getCode, CodeValue::getValue)))
                );
    }

    public static EnStatus ofNullable(String code) {
        if( code == null ) return null;
        return of(code);
    }

}