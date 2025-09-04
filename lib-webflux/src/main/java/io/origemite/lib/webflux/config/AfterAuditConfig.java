package io.origemite.lib.webflux.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.origemite.lib.webflux.web.CommonResponse;
import io.origemite.lib.webflux.web.ResponseType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.jackson.autoconfigure.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.codec.json.JacksonJsonDecoder;
import org.springframework.http.codec.json.JacksonJsonEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import org.springframework.data.domain.PageImpl;
import reactor.util.context.Context;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@Slf4j
public class AfterAuditConfig implements WebFilter {


    //스프링 4로 업데이트하면서 jakarta 로 전부 바뀌면서 오브잭트 맵퍼 의존성이 변경됨

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        long start = System.currentTimeMillis();
        String language = exchange.getRequest().getHeaders().getFirst("X-LANGUAGE");
        String platform = exchange.getRequest().getHeaders().getFirst("X-PLATFORM");
        String uuid = exchange.getRequest().getHeaders().getFirst("X-UUID");
        String clientIP = exchange.getRequest().getHeaders().getFirst("X-CLIENT_IP");
        String appVersion = exchange.getRequest().getHeaders().getFirst("X-APP_VERSION");
        String traceId = exchange.getRequest().getHeaders().getFirst("X-B3-TRACE-ID");
        String userAgent = exchange.getRequest().getHeaders().getFirst("X-USER_AGENT");
        String userId = exchange.getRequest().getHeaders().getFirst("X-USER_ID");
        String serviceId = exchange.getRequest().getHeaders().getFirst("X-SERVICE_ID");
        String authorization = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        String path = exchange.getRequest().getURI().getPath();
        if (path.startsWith("/v3/api-docs") || path.startsWith("/swagger-ui")) {
            return chain.filter(exchange); // 헤더 검사 없이 통과
        }

        if (language == null
                || platform == null
                || uuid == null
                || traceId == null
        ) {
            CommonResponse commonResponse = CommonResponse.builder()
                    .code(ResponseType.BAD_HEADER_REQUEST.getCode())
                    .message(ResponseType.BAD_HEADER_REQUEST.getDesc())
                    .build();

            ObjectMapper objectMapper = new ObjectMapper();
            String json = null;
            try {
                json = objectMapper.writeValueAsString(commonResponse);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            DataBuffer buffer = exchange.getResponse()
                    .bufferFactory().wrap(json.getBytes(StandardCharsets.UTF_8));
            exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
            return exchange.getResponse().writeWith(Mono.just(buffer));

        }

        return chain.filter(exchange)
                .contextWrite(ctx -> {
                    Context updated = ctx.put("X-LANGUAGE", language != null ? language : "ko");
                    if (platform != null) updated = updated.put("X-PLATFORM", platform);
                    if (uuid != null) updated = updated.put("X-UUID", uuid);
                    if (clientIP != null) updated = updated.put("X-CLIENT-IP", clientIP);
                    if (appVersion != null) updated = updated.put("X-APP-VERSION", appVersion);
                    if (traceId != null) updated = updated.put("X-TRACE-ID", traceId);
                    if (userAgent != null) updated = updated.put("X-USER-AGENT", userAgent);
                    if (userId != null) updated = updated.put("X-USER-ID", userId);
                    if (serviceId != null) updated = updated.put("X-SERVICE-ID", serviceId);
                    if (authorization != null) updated = updated.put(HttpHeaders.AUTHORIZATION, authorization);
                    return updated;
                })
                .doOnTerminate(() -> {
                    long duration = System.currentTimeMillis() - start;
                    log.info("후처리 =====");
                    log.info("Request URI: " + exchange.getRequest().getURI());
                    log.info("Request Method: " + exchange.getRequest().getMethod());
                    log.info("Response Status: " + exchange.getResponse().getStatusCode());
                    log.info("Execution Time: " + duration + "ms");
                });
    }

    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(configurer -> {
                    configurer.defaultCodecs().jackson2JsonDecoder(new JacksonJsonDecoder());
                    configurer.defaultCodecs().jackson2JsonEncoder(new JacksonJsonEncoder());
                })
                .build();

        return builder
                .exchangeStrategies(strategies)
                .build();
    }


//    @Bean
//    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
//        return JsonMapper.builder()
//                .defaultDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
//                .serializers(new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
//                .deserializers(new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
//                .mixIn(PageImpl.class, PageImplMixIn.class)
//                ;
//    }

    private static final String DATETIME = "yyyy-MM-dd HH:mm:ss";
    @Bean
    public ObjectMapper objectMapper() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern(DATETIME);
        JavaTimeModule jtm = new JavaTimeModule();
        jtm.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(fmt));
        jtm.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(fmt));

        ObjectMapper mapper = JsonMapper.builder()
                .addModule(jtm)
                .defaultDateFormat(new SimpleDateFormat(DATETIME))
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .configure(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE, false)
                .build();

        // mix-in 은 build 후 등록
        mapper.addMixIn(PageImpl.class, PageImplMixIn.class);
        return mapper;
    }

    public abstract class PageImplMixIn<T> {
        @JsonCreator
        public PageImplMixIn(@JsonProperty("content") List<T> content,
                             @JsonProperty("number") int number,
                             @JsonProperty("size") int size,
                             @JsonProperty("totalElements") long totalElements,
                             @JsonProperty("pageable") Object pageable,
                             @JsonProperty("last") boolean last,
                             @JsonProperty("totalPages") int totalPages,
                             @JsonProperty("sort") Object sort,
                             @JsonProperty("first") boolean first,
                             @JsonProperty("numberOfElements") int numberOfElements) {
        }
    }
}
