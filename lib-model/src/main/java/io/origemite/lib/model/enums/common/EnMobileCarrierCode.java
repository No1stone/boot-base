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
public enum EnMobileCarrierCode implements CodeValue {

    KT("MCC00000KT", "KT"),
    KTM("MCC0000KTM", "KTM"),
    LGM("MCC0000LGM", "LGM"),
    LGT("MCC0000LGT", "LGT"),
    SKM("MCC0000SKM", "SKM"),
    SKT("MCC0000SKT", "SKT"),
    OVER("MCC000OVER", "해외판"),
    WIFI("MCC000WIFI", "WiFi"),
    SELF("MCC000SELF", "자급제"),
    BUDGET("MCC0BUDGET", "알뜰폰"),
    ETC("MCC0999999", "기타")
    ;

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

    public static EnMobileCarrierCode of(String code) {
        return Arrays.stream(EnMobileCarrierCode.values())
                .filter(v -> v.getCode().equals(code))
                .findAny()
                .orElseThrow(() ->
                        new BizErrorException(ResponseType.INVALID_ENUM_VALUE)
                                .addData("code", code)
                                .addData("values", Arrays.stream(values()).collect(Collectors.toMap(CodeValue::getCode, CodeValue::getValue)))
                );
    }

    public static EnMobileCarrierCode ofNullable(String code, EnMobileCarrierCode... allowStatus) {
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
