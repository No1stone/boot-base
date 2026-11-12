package com.origemite.lib.message.saga.rest;

import com.origemite.lib.message.saga.SagaMessage;
import com.origemite.lib.message.saga.SagaMessageType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class RestSagaPublishAdapterTest {

    @Test
    @DisplayName("COMPLETE를 POST /sagas 로 보낸다")
    void postsToSagas() {
        RestClient.Builder builder = RestClient.builder().baseUrl("http://localhost:8095");
        MockRestServiceServer server = MockRestServiceServer.bindTo(builder).build();
        server.expect(requestTo("http://localhost:8095/sagas"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andRespond(withSuccess());

        RestSagaPublishAdapter adapter = new RestSagaPublishAdapter(builder.build());
        adapter.publish(
                SagaMessage.builder(SagaMessageType.COMPLETE, "order")
                        .sagaId("s-1")
                        .status("SUCCEEDED")
                        .build()
        );
        server.verify();
    }
}
