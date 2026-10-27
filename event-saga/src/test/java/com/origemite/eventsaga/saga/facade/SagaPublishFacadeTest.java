package com.origemite.eventsaga.saga.facade;

import com.origemite.eventsaga.saga.dto.SagaReq;
import com.origemite.eventsaga.saga.dto.SagaRes;
import com.origemite.eventsaga.saga.outbox.EventOutboxPort;
import com.origemite.eventsaga.saga.outbox.EventOutboxRecord;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tools.jackson.databind.json.JsonMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SagaPublishFacadeTest {

    @Mock
    EventOutboxPort eventOutboxPort;

    @Test
    @DisplayName("COMPLETE → event_outbox")
    void completeToOutbox() {
        JsonMapper jsonMapper = JsonMapper.builder().findAndAddModules(getClass().getClassLoader()).build();
        when(eventOutboxPort.save(any())).thenAnswer(inv -> inv.getArgument(0));

        SagaPublishFacade facade = new SagaPublishFacade(eventOutboxPort, jsonMapper);

        SagaReq.Ingress request = new SagaReq.Ingress();
        request.setType("COMPLETE");
        request.setDomain("order");

        SagaRes.Accepted res = facade.accept(request);

        assertThat(res.isAccepted()).isTrue();
        ArgumentCaptor<EventOutboxRecord> captor = ArgumentCaptor.forClass(EventOutboxRecord.class);
        verify(eventOutboxPort).save(captor.capture());
        assertThat(captor.getValue().payloadJson()).contains("SUCCEEDED");
    }

    @Test
    @DisplayName("CREATE 거부")
    void createRejected() {
        JsonMapper jsonMapper = JsonMapper.builder().findAndAddModules(getClass().getClassLoader()).build();
        SagaPublishFacade facade = new SagaPublishFacade(eventOutboxPort, jsonMapper);

        SagaReq.Ingress request = new SagaReq.Ingress();
        request.setType("CREATE");
        request.setDomain("order");

        assertThatThrownBy(() -> facade.accept(request))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
