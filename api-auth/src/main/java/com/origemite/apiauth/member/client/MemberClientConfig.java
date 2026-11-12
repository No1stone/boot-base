package com.origemite.apiauth.member.client;

import com.origemite.apiauth.infrastructure.rest.OutboundRestClientCustomizer;
import com.origemite.apiauth.infrastructure.rest.OutboundRestClientLoggingInterceptor;
import com.origemite.apiauth.infrastructure.rest.OutboundRestClientProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

/**
 * MemberController outbound 클라이언트.
 * {@code origemite.member.client.enabled=true} 일 때만 빈 등록.
 * <p>
 * Boot가 꾸민 {@link RestClient.Builder}를 써야 W3C {@code traceparent} 전파·스팬이 붙는다.
 * {@code RestClient.builder()} 직접 생성은 트레이스가 안 나간다.
 */
@Configuration
@ConditionalOnProperty(prefix = "origemite.member.client", name = "enabled", havingValue = "true")
@EnableConfigurationProperties(OutboundRestClientProperties.class)
public class MemberClientConfig {

    @Bean
    MemberClientPort memberClientPort(
            RestClient.Builder restClientBuilder,
            OutboundRestClientProperties properties,
            @Value("${origemite.member.client.base-url:http://localhost:3031/auth}") String baseUrl
    ) {
        RestClient.Builder builder = restClientBuilder.baseUrl(baseUrl);
        new OutboundRestClientCustomizer(properties, new OutboundRestClientLoggingInterceptor())
                .customize(builder);
        return new MemberClientAdapter(builder.build());
    }
}
