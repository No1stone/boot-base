package io.origemite.lib.common.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

public class StringToEmptyConverter implements Converter<String, String> {

    @Override
    public String convert(String source) {
        if (source.isEmpty()) {
            return null;
        }
        return source;
    }
}