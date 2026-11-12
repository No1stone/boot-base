package com.origemite.lib.common.client;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * 공통 RestClient 타임아웃·리트라이·로깅 설정.
 * 값은 모두 덮어쓸 수 있다. 앱에서 {@code @ConfigurationProperties}로 바인딩해도 된다.
 */
public class CommonRestClientProperties {

    /** 연결 타임아웃 기본 1초. */
    private Duration connectTimeout = Duration.ofSeconds(1);

    /** 읽기 타임아웃 기본 1초. */
    private Duration readTimeout = Duration.ofSeconds(1);

    private boolean loggingEnabled = true;

    /** req/res body를 로그에 넣을지. 기본 false. buffering이 켜져 있어야 응답 본문을 읽을 수 있다. */
    private boolean loggingBodyEnabled = false;

    /** 요청 헤더를 로그에 넣을지. Authorization 등은 마스킹한다. 기본 false. */
    private boolean loggingHeadersEnabled = false;

    /** 요청·응답 본문 buffering. body 로그·재읽기에 필요. 기본 true. */
    private boolean bufferContent = true;

    /** 전송 실패(타임아웃·연결 오류 등) 리트라이. 기본 켜짐. */
    private boolean retryEnabled = true;

    /** 최초 시도 포함 횟수. 기본 2(한 번만 더 재시도). */
    private int retryMaxAttempts = 2;

    /** 재시도 대기. 기본 200ms. */
    private Duration retryWait = Duration.ofMillis(200);

    /** 5xx도 공통에서 재시도할지. 기본 false(앱·Outbox가 status로 판단). */
    private boolean retryOnServerError = false;

    /** 재시도할 HTTP status 목록. 비어 있지 않으면 해당 코드만(및 전송 실패) 재시도. */
    private List<Integer> retryStatusCodes = new ArrayList<>();

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

    public boolean isLoggingBodyEnabled() {
        return loggingBodyEnabled;
    }

    public void setLoggingBodyEnabled(boolean loggingBodyEnabled) {
        this.loggingBodyEnabled = loggingBodyEnabled;
    }

    public boolean isLoggingHeadersEnabled() {
        return loggingHeadersEnabled;
    }

    public void setLoggingHeadersEnabled(boolean loggingHeadersEnabled) {
        this.loggingHeadersEnabled = loggingHeadersEnabled;
    }

    public boolean isBufferContent() {
        return bufferContent;
    }

    public void setBufferContent(boolean bufferContent) {
        this.bufferContent = bufferContent;
    }

    public boolean isRetryEnabled() {
        return retryEnabled;
    }

    public void setRetryEnabled(boolean retryEnabled) {
        this.retryEnabled = retryEnabled;
    }

    public int getRetryMaxAttempts() {
        return retryMaxAttempts;
    }

    public void setRetryMaxAttempts(int retryMaxAttempts) {
        this.retryMaxAttempts = retryMaxAttempts;
    }

    public Duration getRetryWait() {
        return retryWait;
    }

    public void setRetryWait(Duration retryWait) {
        this.retryWait = retryWait;
    }

    public boolean isRetryOnServerError() {
        return retryOnServerError;
    }

    public void setRetryOnServerError(boolean retryOnServerError) {
        this.retryOnServerError = retryOnServerError;
    }

    public List<Integer> getRetryStatusCodes() {
        return retryStatusCodes;
    }

    public void setRetryStatusCodes(List<Integer> retryStatusCodes) {
        this.retryStatusCodes = retryStatusCodes == null ? new ArrayList<>() : new ArrayList<>(retryStatusCodes);
    }
}
