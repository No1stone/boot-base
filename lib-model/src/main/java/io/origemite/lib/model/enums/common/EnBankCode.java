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
public enum EnBankCode implements CodeValue {

    KDB("BANK000002", "KDB산업은행"),
    IBK("BANK000003", "IBK기업은행"),
    KB("BANK000004", "KB국민은행"),
    SH_SUHYEOP("BANK000007", "Sh수협은행"),
    NH("BANK000011", "NH농협은행(농협중앙회)"),
    NH_LOCAL("BANK000012", "NH농협은행(농ㆍ축협)"),
    WOORI("BANK000020", "우리은행"),
    SC("BANK000023", "SC제일은행"),
    CITI("BANK000027", "한국씨티은행"),
    DGB("BANK000031", "DGB대구은행"),
    BS("BANK000032", "BNK부산은행"),
    GWANGJU("BANK000034", "광주은행"),
    JEJU("BANK000035", "제주은행"),
    JEONBUK("BANK000037", "전북은행"),
    GYEONGNAM("BANK000039", "BNK경남은행"),
    MG("BANK000045", "MG새마을금고"),
    CU("BANK000048", "신협"),
    SB("BANK000050", "저축은행중앙회"),
    HSBC("BANK000054", "HSBC은행"),
    DEUTSCHE("BANK000055", "도이치은행"),
    JPMORGAN("BANK000057", "제이피모간체이스은행"),
    BOA("BANK000060", "BOA은행"),
    BNP("BANK000061", "비엔피파리바은행"),
    SANLIM("BANK000064", "산림조합중앙회"),
    POST("BANK000071", "우체국"),
    KEB_HANA("BANK000081", "KEB하나은행"),
    SHINHAN("BANK000088", "신한은행"),
    KBANK("BANK000089", "케이뱅크"),
    KAKAO("BANK000090", "카카오뱅크"),
    TOSSBANK("BANK000092", "토스뱅크"),
    YUANTA("BANK000209", "유안타증권"),
    KB_SECURITIES("BANK000218", "KB증권"),
    MIRAE_ASSET("BANK000230", "미래에셋대우"),
    DAEWOO("BANK000238", "대우증권"),
    SAMSUNG("BANK000240", "삼성증권"),
    KOREA_INVEST("BANK000243", "한국투자증권"),
    NH_INVEST("BANK000247", "NH투자증권"),
    KYOBE("BANK000261", "교보증권"),
    HI_INVEST("BANK000262", "하이투자증권"),
    HMC("BANK000263", "HMC투자증권"),
    KIWOOM("BANK000264", "키움증권"),
    EBEST("BANK000265", "이베스트투자증권"),
    SK("BANK000266", "SK증권"),
    DAISHIN("BANK000267", "대신증권"),
    MERITZ1("BANK000268", "메리츠종금증권(아이엠투자증권)"),
    HANWHA("BANK000269", "한화투자증권"),
    HANA_FINANCE("BANK000270", "하나금융투자"),
    TOSS_INVEST("BANK000271", "토스증권"),
    SHINHAN_INVEST("BANK000278", "신한금융투자"),
    DB("BANK000279", "DB금융투자"),
    EUGENE("BANK000280", "유진투자증권"),
    MERITZ2("BANK000287", "메리츠종금증권(메리츠종합금융증권)"),
    KAKAOPAY("BANK000288", "카카오페이증권"),
    BUGUK("BANK000290", "부국증권"),
    SHINYOUNG("BANK000291", "신영증권"),
    CAPE("BANK000292", "케이프투자증권");

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

    public static EnBankCode of(String code) {
        return Arrays.stream(EnBankCode.values())
                .filter(v -> v.getCode().equals(code))
                .findAny()
                .orElseThrow(() ->
                        new BizErrorException(ResponseType.INVALID_ENUM_VALUE)
                                .addData("code", code)
                                .addData("values", Arrays.stream(values())
                                        .collect(Collectors.toMap(CodeValue::getCode, CodeValue::getValue)))
                );
    }

    public static EnBankCode ofNullable(String code, EnBankCode... allowStatus) {
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
