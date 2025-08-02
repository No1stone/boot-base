package io.origemite.lib.model.enums.partner;

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
public enum EnPartnerMemberType implements CodeValue {

    PLATFORM_MANAGER("PMT00000PM", "플랫폼 관리자"),
    SYSTEM_MANAGER("PMT00000SM", "시스템 관리자"),
    SYSTEM_AGENT("PMT00000SA", "시스템 담당자"),
    CONTENT_MANAGER("PMT00000CM", "콘텐츠 관리자"),
    CONTENT_AGENT("PMT00000CA", "콘텐츠 담당자"),
    PRODUCT_SUPPLY_MANAGER("PMT0000PSM", "제품/상품 공급 관리자"),
    PRODUCT_SUPPLY_AGENT("PMT0000PSA", "제품/상품 공급 담당자"),
    SERVICE_SUPPLY_MANAGER("PMT0000SSM", "서비스 공급 관리자"),
    SERVICE_SUPPLY_AGENT("PMT0000SSA", "서비스 공급 담당자"),
    PLATFORM_OPERATION_MANAGER("PMT0000POM", "플랫폼 운영 관리자"),
    PLATFORM_OPERATION_AGENT("PMT0000POA", "플랫폼 운영 담당자"),
    CUSTOMER_CARE_MANAGER("PMT0000CCM", "고객 상담 관리자"),
    CUSTOMER_CARE_AGENT("PMT0000CCA", "고객 상담 담당자"),
    APPLICATION("PMT0000APP", "어플리케이션"),
    LEGACY_APPLICATION("PMT0LGCAPP", "레거시 어플리케이션");

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

    public static EnPartnerMemberType of(String code) {
        return Arrays.stream(EnPartnerMemberType.values())
                .filter(v -> v.getCode().equals(code))
                .findAny()
                .orElseThrow(() ->
                        new BizErrorException(ResponseType.INVALID_ENUM_VALUE)
                                .addData("code", code)
                                .addData("values", Arrays.stream(values())
                                        .collect(Collectors.toMap(CodeValue::getCode, CodeValue::getValue)))
                );
    }

    public static EnPartnerMemberType ofNullable(String code, EnPartnerMemberType... allowStatus) {
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
