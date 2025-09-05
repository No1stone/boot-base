package com.origemite.lib.common.client;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Deprecated
public class RestClentLoggingInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        logRequest(request, body);
        ClientHttpResponse response = execution.execute(request, body);
        logResponse(response);
        return response;
    }

    private void logRequest(HttpRequest request, byte[] body) throws IOException {
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
    }

    private void logResponse(ClientHttpResponse response) throws IOException {
        StringBuilder inputStringBuilder = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getBody(), StandardCharsets.UTF_8))) {
            inputStringBuilder.append(bufferedReader.lines().collect(Collectors.joining("\n")));
        }
        System.out.println("Response Status Code: " + response.getStatusCode());
        System.out.println("Response Headers: " + response.getHeaders());
        System.out.println("Response Body: " + inputStringBuilder.toString());
    }
}