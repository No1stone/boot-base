package io.diddda.lib.webflux.common.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import io.diddda.lib.webflux.exception.BizErrorException;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CustomLocalDateDeserializer extends JsonDeserializer<LocalDate> {

    @Override
    public LocalDate deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String text = jsonParser.getText();

        if (org.springframework.util.StringUtils.isEmpty(text)) return null;

        if (text.matches("\\d{4}-\\d{2}-\\d{2}")) {
            return LocalDate.parse(text, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } else {
            throw new BizErrorException("200","error");
        }
    }
}