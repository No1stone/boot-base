package io.diddda.lib.common.base;

import io.diddda.lib.common.enums.CodeValue;
import io.diddda.lib.common.util.StringUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class StringToEnumConverterFactory implements ConverterFactory<String, Enum<? extends CodeValue>> {

	@Override
	public <T extends Enum<? extends CodeValue>> Converter<String, T> getConverter(Class<T> targetType) {
		if (CodeValue.class.isAssignableFrom(targetType)) {
			return new StringToEnumConverter<>(targetType);
		} else {
			return null;
		}
	}

	private static final class StringToEnumConverter<T extends Enum<? extends CodeValue>> implements Converter<String, T> {

		private final Map<String, T> map;

		public StringToEnumConverter(Class<T> targetEnum) {
			T[] enumConstants = targetEnum.getEnumConstants();
			map = Arrays.stream(enumConstants)
					.collect(Collectors.toMap(enumConstant -> ((CodeValue) enumConstant).getCode(), Function.identity()));
		}

		@Override
		public T convert(String code) {

			if (!StringUtils.isEmpty(code)) {
				return null;
			}

			T enumValue = map.get(code);

			if (enumValue == null) {
				throw new IllegalArgumentException("IllegalArgumentException");
			}

			return enumValue;
		}
	}
}