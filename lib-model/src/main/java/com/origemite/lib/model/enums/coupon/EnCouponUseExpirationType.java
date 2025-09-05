package com.origemite.lib.model.enums.coupon;

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
public enum EnCouponUseExpirationType implements CodeValue {

    FIXED("CUET0FIXED", "쿠폰 사용 만료 일자 지정"),
    DAY("CUET000DAY", "쿠폰 생성 일자 기준 만료 일자 지정")
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

    public static EnCouponUseExpirationType of(String code) {
        return Arrays.stream(EnCouponUseExpirationType.values())
                .filter(v -> v.getCode().equals(code))
                .findAny()
                .orElseThrow(() ->
                        new BizErrorException(ResponseType.INVALID_ENUM_VALUE)
                                .addData("code", code)
                                .addData("values", Arrays.stream(values()).collect(Collectors.toMap(CodeValue::getCode, CodeValue::getValue)))
                );
    }

    public static EnCouponUseExpirationType ofNullable(String code, EnCouponUseExpirationType... allowStatus) {
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
