package io.origemite.lib.webflux.config;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpHeaders;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;

@UtilityClass
public class ReactiveRequestContext {

    public static Mono<String> getAccessToken() {
        return Mono.deferContextual(ctx -> Mono.justOrEmpty(ctx.getOrEmpty("Authorization")));
    }

    public static Mono<String> getLanguage() {
        return Mono.deferContextual(ctx -> Mono.justOrEmpty(ctx.getOrEmpty("X-LANGUAGE")));
    }

    public static Mono<String> getPlatform() {
        return Mono.deferContextual(ctx -> Mono.justOrEmpty(ctx.getOrEmpty("X-PLATFORM")));
    }

    public static Mono<String> getUuid() {
        return Mono.deferContextual(ctx -> Mono.justOrEmpty(ctx.getOrEmpty("X-UUID")));
    }

    public static Mono<String> getClientIp() {
        return Mono.deferContextual(ctx -> Mono.justOrEmpty(ctx.getOrEmpty("X-CLIENT-IP")));
    }

    public static Mono<String> getAppVersion() {
        return Mono.deferContextual(ctx -> Mono.justOrEmpty(ctx.getOrEmpty("X-APP-VERSION")));
    }

    public static Mono<String> getTraceId() {
        return Mono.deferContextual(ctx -> Mono.justOrEmpty(ctx.getOrEmpty("X-TRACE-ID")));
    }

    public static Mono<String> getUserAgent() {
        return Mono.deferContextual(ctx -> Mono.justOrEmpty(ctx.getOrEmpty("X-USER-AGENT")));
    }

    public static Mono<String> getUserId() {
        return Mono.deferContextual(ctx -> Mono.justOrEmpty(ctx.getOrEmpty("X-USER-ID")));
    }

    public static Mono<String> getServiceId() {
        return Mono.deferContextual(ctx -> Mono.justOrEmpty(ctx.getOrEmpty("X-SERVICE-ID")));
    }

    public static Mono<String> getRequestId() {
        return Mono.deferContextual(ctx -> Mono.justOrEmpty(ctx.getOrEmpty("X-REQUEST-ID")));
    }

    public static Mono<RequestContextVO> getContext() {
        return Mono.deferContextual(ctx -> {
            RequestContextVO vo = RequestContextVO.builder()
                    .accessToken(ctx.getOrDefault("Authorization", null))
                    .language(ctx.getOrDefault("X-LANGUAGE", null))
                    .platform(ctx.getOrDefault("X-PLATFORM", null))
                    .uuid(ctx.getOrDefault("X-UUID", null))
                    .clientIp(ctx.getOrDefault("X-CLIENT-IP", null))
                    .appVersion(ctx.getOrDefault("X-APP-VERSION", null))
                    .traceId(ctx.getOrDefault("X-TRACE-ID", null))
                    .userAgent(ctx.getOrDefault("X-USER-AGENT", null))
                    .userId(ctx.getOrDefault("X-USER-ID", null))
                    .serviceId(ctx.getOrDefault("X-SERVICE-ID", null))
                    .requestId(ctx.getOrDefault("X-REQUEST-ID", null))
                    .build();
            return Mono.just(vo);
        });
    }

    public static Mono<Consumer<HttpHeaders>> buildHeaders() {
        return ReactiveRequestContext.getContext()
                .map(context -> headers -> {
                    if (context.getAccessToken() != null)
                        headers.set("Authorization", context.getAccessToken());
                    if (context.getLanguage() != null)
                        headers.set("X-LANGUAGE", context.getLanguage());
                    if (context.getPlatform() != null)
                        headers.set("X-PLATFORM", context.getPlatform());
                    if (context.getUuid() != null)
                        headers.set("X-UUID", context.getUuid());
                    if (context.getClientIp() != null)
                        headers.set("X-CLIENT-IP", context.getClientIp());
                    if (context.getAppVersion() != null)
                        headers.set("X-APP-VERSION", context.getAppVersion());
                    if (context.getTraceId() != null)
                        headers.set("X-TRACE-ID", context.getTraceId());
                    if (context.getUserAgent() != null)
                        headers.set("X-USER-AGENT", context.getUserAgent());
                    if (context.getUserId() != null)
                        headers.set("X-USER-ID", context.getUserId());
                    if (context.getServiceId() != null)
                        headers.set("X-SERVICE-ID", context.getServiceId());
                    if (context.getRequestId() != null)
                        headers.set("X-REQUEST-ID", context.getRequestId());
                });
    }

    @Data
    @Builder
    public static class RequestContextVO {
        private String accessToken;
        private String language;
        private String platform;
        private String uuid;
        private String clientIp;
        private String appVersion;
        private String traceId;
        private String userAgent;
        private String userId;
        private String serviceId;
        private String requestId;
    }
}
