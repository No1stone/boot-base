package com.origemite.lib.model.enums.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Objects;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum BankOfKoreaLocale {

    KOREA("0000000","한국","원",EnCurrencyCode.KRW),
    USA("0000001","미국","달러",EnCurrencyCode.USD),
    JAPAN("0000002","일본","엔",EnCurrencyCode.JPY),
    CHINA("0000053","중국","위안",EnCurrencyCode.CNY)

    ;

    private String code;
    private String countryName;
    private String currencyName;
    private EnCurrencyCode currencyCode;


    public static BankOfKoreaLocale find(BankOfKoreaLocale target) {
        if (target == null) return null;

        return Arrays.stream(values())
                .filter(locale ->
                        Objects.equals(locale.getCode(), target.getCode()) &&
                                Objects.equals(locale.getCountryName(), target.getCountryName()) &&
                                Objects.equals(locale.getCurrencyName(), target.getCurrencyName()))
                .findFirst()
                .orElse(null);
    }

}
