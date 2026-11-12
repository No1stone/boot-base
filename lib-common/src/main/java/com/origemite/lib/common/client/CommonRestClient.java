package com.origemite.lib.common.client;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * 형식이 고정된 공통 HTTP 호출 헬퍼.
 * get/post 등은 숏컷이고, 헤더·쿼리·본문은 호출마다 넘긴다.
 * 4xx/5xx도 예외로 바꾸지 않고 {@link CommonRestResponse}에 status를 담는다.
 */
public final class CommonRestClient {

    private final RestClient restClient;

    public CommonRestClient(RestClient restClient) {
        this.restClient = restClient;
    }

    /**
     * Boot {@link RestClient.Builder}에 공통 설정을 붙인 뒤 래퍼를 만든다.
     */
    public static CommonRestClient from(RestClient.Builder builder, CommonRestClientProperties properties) {
        new CommonRestClientCustomizer(properties).customize(builder);
        return new CommonRestClient(builder.build());
    }

    public static CommonRestClient from(RestClient.Builder builder) {
        return from(builder, new CommonRestClientProperties());
    }

    /** 필요하면 fluent API를 직접 쓴다. */
    public RestClient raw() {
        return restClient;
    }

    public <T> CommonRestResponse<T> get(
            String uri,
            @Nullable HttpHeaders headers,
            @Nullable MultiValueMap<String, String> query,
            Class<T> responseType
    ) {
        return exchange(HttpMethod.GET, uri, query, headers, null, responseType);
    }

    public <T> CommonRestResponse<T> post(
            String uri,
            @Nullable HttpHeaders headers,
            @Nullable Object body,
            Class<T> responseType
    ) {
        return exchange(HttpMethod.POST, uri, null, headers, body, responseType);
    }

    public <T> CommonRestResponse<T> put(
            String uri,
            @Nullable HttpHeaders headers,
            @Nullable Object body,
            Class<T> responseType
    ) {
        return exchange(HttpMethod.PUT, uri, null, headers, body, responseType);
    }

    public <T> CommonRestResponse<T> patch(
            String uri,
            @Nullable HttpHeaders headers,
            @Nullable Object body,
            Class<T> responseType
    ) {
        return exchange(HttpMethod.PATCH, uri, null, headers, body, responseType);
    }

    public <T> CommonRestResponse<T> delete(
            String uri,
            @Nullable HttpHeaders headers,
            @Nullable MultiValueMap<String, String> query,
            Class<T> responseType
    ) {
        return exchange(HttpMethod.DELETE, uri, query, headers, null, responseType);
    }

    public <T> CommonRestResponse<T> exchange(
            HttpMethod method,
            String uri,
            @Nullable MultiValueMap<String, String> query,
            @Nullable HttpHeaders headers,
            @Nullable Object body,
            Class<T> responseType
    ) {
        // String URI를 써야 baseUrl이 적용된다.
        String target = buildUri(uri, query);
        RestClient.RequestBodySpec spec = restClient.method(method).uri(target);
        if (headers != null) {
            spec.headers(h -> h.addAll(headers));
        }
        if (body != null) {
            if (headers == null || headers.getContentType() == null) {
                spec.contentType(MediaType.APPLICATION_JSON);
            }
            spec.body(body);
        }
        return spec.exchange((request, response) -> {
            T parsed = readBody(response, responseType);
            return new CommonRestResponse<>(response.getStatusCode(), parsed, request.getURI().toString());
        });
    }

    private static String buildUri(String uri, @Nullable MultiValueMap<String, String> query) {
        if (query == null || query.isEmpty()) {
            return uri;
        }
        return UriComponentsBuilder.fromUriString(uri).queryParams(query).build(true).toUriString();
    }

    @Nullable
    private static <T> T readBody(
            RestClient.RequestHeadersSpec.ConvertibleClientHttpResponse response,
            Class<T> responseType
    ) {
        if (responseType == null || responseType == Void.class || responseType == void.class) {
            return null;
        }
        return response.bodyTo(responseType);
    }
}
