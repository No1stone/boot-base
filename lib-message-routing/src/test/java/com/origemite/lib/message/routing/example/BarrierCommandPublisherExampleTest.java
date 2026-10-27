package com.origemite.lib.message.routing.example;

import com.origemite.lib.message.routing.MessagePublishPort;
import com.origemite.lib.message.routing.RoutingMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BarrierCommandPublisherExampleTest {

    @Mock
    private MessagePublishPort messagePublishPort;

    @Test
    @DisplayName("Facade 패턴으로 routing key와 messageType을 맞춰 발행한다")
    void publishesBarrierOpen() {
        BarrierCommandPublisherExample publisher = new BarrierCommandPublisherExample(messagePublishPort);

        publisher.requestOpen("site-1", "b1", "exit");

        ArgumentCaptor<RoutingMessage> messageCaptor = ArgumentCaptor.forClass(RoutingMessage.class);
        verify(messagePublishPort).publish(eq("parking.barrier.b1.command"), messageCaptor.capture());
        assertThat(messageCaptor.getValue().messageType()).isEqualTo(BarrierOpenRequested.MESSAGE_TYPE);
        assertThat(messageCaptor.getValue().data()).isInstanceOf(BarrierOpenRequested.class);
    }
}
