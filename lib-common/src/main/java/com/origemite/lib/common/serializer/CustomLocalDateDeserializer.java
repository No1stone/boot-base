package com.origemite.lib.common.serializer;

import com.origemite.lib.common.exception.BizErrorException;
import com.origemite.lib.common.util.StringUtils;
import com.origemite.lib.common.web.ResponseType;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.ValueDeserializer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CustomLocalDateDeserializer extends ValueDeserializer<LocalDate> {

    @Override
    public LocalDate deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) {
        String text = jsonParser.getText();

        if (StringUtils.isEmpty(text)) return null;

        if (text.matches("\\d{4}-\\d{2}-\\d{2}")) {
            return LocalDate.parse(text, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        throw new BizErrorException(ResponseType.INVALID_DATE_FORMAT);
    }
}
