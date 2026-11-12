package com.origemite.lib.common.client;

import java.time.Duration;

/**
 * 공통 RestClient 타임아웃·로깅 설정.
 * 앱에서 {@code @ConfigurationProperties}로 바인딩해 넘겨도 된다.
 */
public class CommonRestClientProperties {

    private Duration connectTimeout = Duration.ofSeconds(5);
    private Duration readTimeout = Duration.ofSeconds(3);
    private boolean loggingEnabled = true;

    public Duration getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(Duration connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public Duration getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(Duration readTimeout) {
        this.readTimeout = readTimeout;
    }

    public boolean isLoggingEnabled() {
        return loggingEnabled;
    }

    public void setLoggingEnabled(boolean loggingEnabled) {
        this.loggingEnabled = loggingEnabled;
    }
}
