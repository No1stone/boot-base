package io.origemite.lib.common.client;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClient;


@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RestResponse<T> {
    HttpStatusCode status;
    T body;
    String url;
    String rawBody;
    String rawParameter;
    String rawPayload;
//    RestClient.RequestHeadersSpec.ConvertibleClientHttpResponse rawResponse;
//    HttpRequest rawRequest;
}
