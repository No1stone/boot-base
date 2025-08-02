package io.origemite.lib.model.enums.agent;

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
public enum EnTranslationAgentType implements CodeValue {

    EXA135("MATAEXA135", "엑사원 (Exaone) v3.5"),
    HYPER_CLOVA("MATAHCSD3B", "HyperCLOVA X SEED 3B"),
    NCP_TEXT("MATA0NCPTT", "Naver Cloud Platform, 텍스트 번역 (Text Translation)"),
    GOOGLE_API("MATAGCTAPI", "Google Cloud, Translation API")
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

    public static EnTranslationAgentType of(String code) {
        return Arrays.stream(EnTranslationAgentType.values())
                .filter(v -> v.getCode().equals(code))
                .findAny()
                .orElseThrow(() ->
                        new BizErrorException(ResponseType.INVALID_ENUM_VALUE)
                                .addData("code", code)
                                .addData("values", Arrays.stream(values()).collect(Collectors.toMap(CodeValue::getCode, CodeValue::getValue)))
                );
    }

    public static EnTranslationAgentType ofNullable(String code, EnTranslationAgentType... allowStatus) {
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

