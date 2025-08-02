package io.origemite.lib.common.serializer;

import io.origemite.lib.common.exception.DidddaErrorException;
import io.origemite.lib.common.web.CommonResponseType;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import io.origemite.lib.common.util.StringUtils;
import io.origemite.lib.common.web.SystemServiceModule;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CustomLocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

    @Override
    public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String text = jsonParser.getText();

        if (StringUtils.isEmpty(text)) return null;

        if (text.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}")) {
            return LocalDateTime.parse(text, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        } else {
            throw new DidddaErrorException(SystemServiceModule.DEFAULT, CommonResponseType.INVALID_DATE_FORMAT);
        }

    }
}