package io.diddda.lib.common.client;


import io.diddda.lib.common.util.TransformUtils;
import org.springframework.http.*;
import org.springframework.http.client.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class AbstractRestClient {
    private final RestClient restClient;
    //private final Retry retry;

    protected AbstractRestClient() {
        this(null, 5000, 3000);
    }

    protected AbstractRestClient(Integer connectTimeout, Integer readTimeout) {
        this(null, connectTimeout, readTimeout);
    }

    protected AbstractRestClient(String baseUrl, Integer connectTimeout, Integer readTimeout) {

        // Retry 설정 생성
//        RetryConfig config = RetryConfig.custom()
//                .maxAttempts(3)
//                .waitDuration(Duration.ofSeconds(2))
//                .build();

        // Retry 인스턴스 생성
//        RetryRegistry registry = RetryRegistry.of(config);
//        retry = registry.retry("default.retry");

        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(connectTimeout);
        factory.setReadTimeout(readTimeout);

        restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .requestFactory(new BufferingClientHttpRequestFactory(factory))
                //.requestInterceptor(new RestClentLoggingInterceptor())
                .build();
    }


    protected RestResponse<ResultMap> get(String url) {
        return get(url, null, null, ResultMap.class);
    }

    protected <T> RestResponse<T> get(String url, Class<T> clazz) {
        return get(url, null, null, clazz);
    }

    protected RestResponse<ResultMap> get(String url, MultiValueMap<String, Object> params) {
        return get(url, null, params, ResultMap.class);
    }

    protected  <T> RestResponse<T> get(String url, HttpHeaders headers,Class<T> clazz) {
        return get(url, headers, null, clazz);
    }

    protected  RestResponse get(String url, HttpHeaders headers) {
        return get(url, headers, null, ResultMap.class);
    }

    protected <T> RestResponse<T> get(String url, HttpHeaders headers, MultiValueMap<String, Object> params, Class<T> clazz) {

        RestResponse.RestResponseBuilder<T> builder = RestResponse.builder();

        restClient.get()
                .uri(url, params == null ? Map.of() : params)
                .headers(e -> {
                    if (headers != null) e.addAll(headers);
                })
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {
                    onReceived(params, null, clazz, req, res, builder);
                })
                .onStatus(HttpStatusCode::is2xxSuccessful, (req, res) -> {
                    onReceived(params, null, clazz, req, res, builder);
                })
                .toBodilessEntity();


        return builder.build();

    }

    protected RestResponse<ResultMap> post(String url, Object body) {
        return post(url, MediaType.APPLICATION_JSON, body);
    }

    protected RestResponse<ResultMap> post(String url, MediaType mediaType, Object body) {
        return post(url, mediaType, null, body, ResultMap.class);
    }

    protected RestResponse<ResultMap> post(String url, HttpHeaders headers, Object body) {
        return post(url, MediaType.APPLICATION_JSON, headers, body, ResultMap.class);
    }

    protected <T> RestResponse<T> post(String url, MediaType mediaType, HttpHeaders headers, Object body, Class<T> clazz) {

        RestResponse.RestResponseBuilder<T> builder = RestResponse.builder();

        restClient.post()
                .uri(url)
                .contentType(mediaType == null ? MediaType.APPLICATION_JSON : mediaType)
                .headers(e -> {
                    if (headers != null) e.addAll(headers);
                })
                .body(body)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {
                    onReceived(body, null, clazz, req, res, builder);
                })
                .onStatus(HttpStatusCode::is2xxSuccessful, (req, res) -> {
                    onReceived(body, null, clazz, req, res, builder);
                })
                .toBodilessEntity();


        return builder.build();
    }

    protected RestResponse<ResultMap> put(String url) {
        return put(url, null);
    }

    protected RestResponse<ResultMap> put(String url, MultiValueMap<String, Object> body) {
        return put(url, MediaType.APPLICATION_JSON, body, ResultMap.class);
    }

    protected <T> RestResponse<T> put(String url, MediaType mediaType, MultiValueMap<String, Object> body, Class<T> clazz) {
        return put(url, mediaType, null, body, clazz);
    }

    protected <T> RestResponse<T> put(String url, MediaType mediaType, HttpHeaders headers, Object body, Class<T> clazz) {

        RestResponse.RestResponseBuilder<T> builder = RestResponse.builder();

        restClient.put()
                .uri(url)
                .contentType(mediaType == null ? MediaType.APPLICATION_JSON : mediaType)
                .headers(e -> {
                    if (headers != null) e.addAll(headers);
                })
                .body(body)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {
                    onReceived(body, null, clazz, req, res, builder);
                })
                .onStatus(HttpStatusCode::is2xxSuccessful, (req, res) -> {
                    onReceived(body, null, clazz, req, res, builder);
                })
                .toBodilessEntity();

        return builder.build();


    }

    protected RestResponse<ResultMap> delete(String url) {
        return delete(url, ResultMap.class);
    }

    protected <T> RestResponse<T> delete(String url, Class<T> clazz) {
        RestResponse.RestResponseBuilder<T> builder = RestResponse.builder();

        restClient.delete()
                .uri(url)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {
                    onReceived(null, null, clazz, req, res, builder);
                })
                .onStatus(HttpStatusCode::is2xxSuccessful, (req, res) -> {
                    onReceived(null, null, clazz, req, res, builder);
                })
                .toBodilessEntity();

        return builder.build();

    }

    private <T> void onReceived(Object paramter, Object body, Class<T> clazz, HttpRequest req, ClientHttpResponse res, RestResponse.RestResponseBuilder<T> builder) throws IOException {
        byte[] bodyBytes = res.getBody().readAllBytes();
        String rawBody = new String(bodyBytes, StandardCharsets.UTF_8);

        try {
            builder.status(res.getStatusCode())
                    .body(TransformUtils.parse(rawBody, clazz))
                    .rawBody(rawBody)
                    .url(req.getURI().toString())
                    .rawPayload(TransformUtils.stringify(body))
                    .rawParameter(TransformUtils.stringify(paramter));
        } finally {
            log(req, res, bodyBytes);
        }
    }

    private void log(HttpRequest request, ClientHttpResponse response, byte[] body) throws IOException {

        System.out.println("Request URI: " + request.getURI());
        System.out.println("Request Method: " + request.getMethod());
        System.out.println("Request Headers: " + request.getHeaders());

        // GET 요청의 경우 쿼리 스트링에서 파라미터를 추출합니다.
        String query = request.getURI().getQuery();
        if (query != null) {
            Map<String, String> queryParams = Stream.of(query.split("&"))
                    .map(param -> param.split("="))
                    .collect(Collectors.toMap(pair -> pair[0], pair -> pair.length > 1 ? pair[1] : ""));
            System.out.println("Query Parameters: " + queryParams);
        } else {
            System.out.println("Query Parameters: [empty]");
        }

        if (body.length > 0) {
            String payload = new String(body, StandardCharsets.UTF_8);
            System.out.println("Request Body: " + payload);
        } else {
            System.out.println("Request Body: [empty]");
        }

        StringBuilder inputStringBuilder = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getBody(), StandardCharsets.UTF_8))) {
            inputStringBuilder.append(bufferedReader.lines().collect(Collectors.joining("\n")));
        }
        System.out.println("Response Status Code: " + response.getStatusCode());
        System.out.println("Response Headers: " + response.getHeaders());
        System.out.println("Response Body: " + inputStringBuilder.toString());
    }

}


