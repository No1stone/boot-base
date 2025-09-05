package com.origemite.apiauth.config;

import feign.Logger;
import feign.RequestInterceptor;
import feign.Response;
import feign.Retryer;
import feign.codec.ErrorDecoder;
import com.origemite.lib.common.exception.BizErrorException;
import com.origemite.lib.common.util.AuthContextUtils;
import com.origemite.lib.common.util.StringUtils;
import com.origemite.lib.common.web.ResponseType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.FeignFormatterRegistrar;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;

import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class FeignClientConfig {

    @Value("${security.jwt.token}")
    private String jwtToken;

    @Bean
    public RequestInterceptor requestInterceptor() {

        String contextToken = AuthContextUtils.getJwtToken();

        return requestTemplate -> {
            requestTemplate.header("Authorization", "Bearer " + StringUtils.defaultIfEmpty(contextToken, jwtToken));
        };
    }

    @Bean
    Retryer.Default retryer() {
        return new Retryer.Default(100L, TimeUnit.SECONDS.toMillis(3L), 5);
    }

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public FeignFormatterRegistrar dateTimeFormatterRegistrar() {
        return registry -> {
            DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
            //registrar.setUseIsoFormat(true);
            registrar.setUseIsoFormat(false);
            registrar.setDateTimeFormatter(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            registrar.setDateFormatter(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        };
    }

    @Bean
    public ErrorDecoder getFeignErrorDecoder(){
        return new FeignErrorDecoder();
    }

    public static class FeignErrorDecoder implements ErrorDecoder {
        @Override
        public Exception decode(String methodKey, Response response) {
            log.error("FeignErrorDecoder: methodKey={}, reason={}", methodKey, response.reason());
            switch (response.status()){
                case 400 -> throw new BizErrorException(ResponseType.UNKNOWN);
                case 404 -> throw new BizErrorException(ResponseType.UNKNOWN);
                default -> throw new BizErrorException(ResponseType.UNKNOWN);
            }
        }
    }
/**
    @Bean
    public Decoder feignDecoder() {
        return new FeignDecoder(objectMapper);
    }

    @RequiredArgsConstructor
    public static class FeignDecoder implements Decoder {

        private final ObjectMapper objectMapper;

        @Override
        public Object decode(Response response, java.lang.reflect.Type type) throws IOException {
            String body = Util.toString(response.body().asReader(Util.UTF_8));

            JavaType javaType = objectMapper.getTypeFactory().constructType(type);
            CommonResponse<?> commonResponse = objectMapper.readValue(body, javaType);
            ResponseType responseType = ResponseType.of(commonResponse.getCode());

            if (responseType != ResponseType.SUCCESS) {
                throw new BizErrorException(responseType);
            }

            return commonResponse;
        }
    }
        */


//    @Bean
//    public Decoder feignDecoder() {
//        HttpMessageConverter jacksonConverter = new MappingJackson2HttpMessageConverter(customObjectMapper());
//
//        HttpMessageConverters httpMessageConverters = new HttpMessageConverters(jacksonConverter);
//        ObjectFactory<HttpMessageConverters> objectFactory = () -> httpMessageConverters;
//        return new ResponseEntityDecoder(new SpringDecoder(objectFactory));
//    }


}