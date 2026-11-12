package com.origemite.lib.common.client;

import org.springframework.http.HttpStatusCode;
import org.springframework.lang.Nullable;

/**
 * 공통 RestClient 호출 결과. Outbox 릴레이 등이 status로 분기할 수 있게 둔다.
 */
public final class CommonRestResponse<T> {

    private final HttpStatusCode status;
    @Nullable
    private final T body;
    private final String url;

    public CommonRestResponse(HttpStatusCode status, @Nullable T body, String url) {
        this.status = status;
        this.body = body;
        this.url = url;
    }

    public HttpStatusCode getStatus() {
        return status;
    }

    @Nullable
    public T getBody() {
        return body;
    }

    public String getUrl() {
        return url;
    }

    public boolean is2xxSuccessful() {
        return status.is2xxSuccessful();
    }

    public int getStatusCodeValue() {
        return status.value();
    }
}
