package com.origemite.apiauth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.origemite.lib.common.converter.StringToEmptyConverter;
import com.origemite.lib.common.serializer.CustomLocalDateDeserializer;
import com.origemite.lib.common.serializer.CustomLocalDateSerializer;
import com.origemite.lib.common.serializer.CustomLocalDateTimeDeserializer;
import com.origemite.lib.common.serializer.CustomLocalDateTimeSerializer;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.standard.TemporalAccessorParser;
import org.springframework.format.datetime.standard.TemporalAccessorPrinter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    @Bean
    public ObjectMapper objectMapper() {
        return Jackson2ObjectMapperBuilder
                .json()
                .deserializerByType(LocalDate.class, new CustomLocalDateDeserializer())
                .serializerByType(LocalDate.class, new CustomLocalDateSerializer())
                .deserializerByType(LocalDateTime.class, new CustomLocalDateTimeDeserializer())
                .serializerByType(LocalDateTime.class, new CustomLocalDateTimeSerializer()).build();
    }

    /*
     * ======================================
     * 메시지 컨버터 설정 POST
     * ======================================
     * */

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {

        for (HttpMessageConverter<?> converter : converters) {
            if (converter instanceof MappingJackson2HttpMessageConverter jacksonConverter) {
                jacksonConverter.setObjectMapper(objectMapper());
            }
        }
    }

    /*
     * ======================================
     * 메시지 컨버터 설정 GET
     * ======================================
     * */
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatterForFieldType(LocalDateTime.class,
                new TemporalAccessorPrinter(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                new TemporalAccessorParser(LocalDateTime.class, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        registry.addConverter(new StringToEmptyConverter());
    }

    /*
    * ======================================
    * 다국어 메시지 설정
    * ======================================
    * */

    @Bean
    public LocaleResolver localeResolver() {
        AcceptHeaderLocaleResolver localeResolver = new AcceptHeaderLocaleResolver();
        localeResolver.setDefaultLocale(Locale.KOREA);
        return localeResolver;
    }

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames("messages/message", "messages/validation", "messages/message_a4");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setCacheSeconds(0);
        messageSource.setUseCodeAsDefaultMessage(true);
        return messageSource;
    }

    @Bean
    public MessageSourceAccessor messageSourceAccessor(MessageSource messageSource) {
        return new MessageSourceAccessor(messageSource, Locale.getDefault());
    }

    /*
     * ======================================
     * 벨리데이터 설정
     * ======================================
     * */

    @Bean
    public LocalValidatorFactoryBean getValidator() {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource());
        return bean;
    }

    /*
     * ======================================
     * CORS 설정
     * ======================================
     * */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //TODO 추후 도메인 정의되면 Phase 별로 지정하여 설정
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("*")
                .allowedHeaders("*");
    }

}
