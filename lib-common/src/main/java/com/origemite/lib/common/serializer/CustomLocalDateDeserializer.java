package com.origemite.lib.common.serializer;

import com.origemite.lib.common.exception.DidddaErrorException;
import com.origemite.lib.common.web.CommonResponseType;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.origemite.lib.common.util.StringUtils;
import com.origemite.lib.common.web.SystemServiceModule;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CustomLocalDateDeserializer extends JsonDeserializer<LocalDate> {

    @Override
    public LocalDate deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String text = jsonParser.getText();

        if (StringUtils.isEmpty(text)) return null;

        if (text.matches("\\d{4}-\\d{2}-\\d{2}")) {
            return LocalDate.parse(text, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } else {
            throw new DidddaErrorException(SystemServiceModule.DEFAULT, CommonResponseType.INVALID_DATE_FORMAT);
        }
    }
}