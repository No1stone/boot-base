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
public enum EnPaymentCardCompanyCode implements CodeValue {

    OLD_SHINHAN("PCCC000026", "구신한카드"),
    JUTAEK("PCCC000029", "주택카드(구동남)"),
    GWANGJU("PCCC000034", "광주카드"),
    JEJU("PCCC000035", "제주카드"),
    JEONBUK("PCCC000037", "전북카드"),
    JOHUNG("PCCC000038", "조흥카드(구강원)"),
    CHUNGBUK("PCCC000040", "충북카드"),
    CITI("PCCC000053", "씨티카드"),
    BC("PCCC000061", "비씨카드"),
    KB("PCCC000062", "국민카드"),
    KEHWA("PCCC000063", "외한카드"),
    SAMSUNG("PCCC000065", "삼성카드"),
    SHINHAN("PCCC000066", "신한카드"),
    HYUNDAI("PCCC000067", "현대카드"),
    LOTTE("PCCC000068", "롯데카드"),
    AMEX_FOREIGN("PCCC000069", "해외발행 아멕스"),
    NH("PCCC000071", "NH카드"),
    SUHYUP("PCCC000073", "수협카드"),
    WOORI("PCCC000075", "우리카드"),
    HANA_BC("PCCC000082", "하나BC카드"),
    HANA_SK("PCCC000083", "하나SK카드");

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

    public static EnPaymentCardCompanyCode of(String code) {
        return Arrays.stream(EnPaymentCardCompanyCode.values())
                .filter(v -> v.getCode().equals(code))
                .findAny()
                .orElseThrow(() ->
                        new BizErrorException(ResponseType.INVALID_ENUM_VALUE)
                                .addData("code", code)
                                .addData("values", Arrays.stream(values())
                                        .collect(Collectors.toMap(CodeValue::getCode, CodeValue::getValue)))
                );
    }
}