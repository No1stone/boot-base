package com.origemite.lib.message.routing.rabbit;

import com.origemite.lib.message.routing.RoutingKeys;
import com.origemite.lib.message.routing.RoutingMessage;
import com.origemite.lib.message.routing.example.BarrierOpenRequested;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import tools.jackson.databind.json.JsonMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RabbitMessagePublishAdapterTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Test
    @DisplayName("기본 exchange로 JSON 본문을 발행한다")
    void publishesJsonToDefaultExchange() {
        MessageRoutingProperties properties = new MessageRoutingProperties();
        properties.setExchange("app.topic");
        JsonMapper jsonMapper = JsonMapper.builder().findAndAddModules(getClass().getClassLoader()).build();
        RabbitMessagePublishAdapter adapter = new RabbitMessagePublishAdapter(rabbitTemplate, properties, jsonMapper);

        String rk = RoutingKeys.of("parking", "barrier", "b1", "command");
        RoutingMessage message = RoutingMessage.builder(BarrierOpenRequested.MESSAGE_TYPE)
                .data(new BarrierOpenRequested("site-1", "b1", "exit"))
                .build();

        adapter.publish(rk, message);

        ArgumentCaptor<Message> captor = ArgumentCaptor.forClass(Message.class);
        verify(rabbitTemplate).send(eq("app.topic"), eq(rk), captor.capture());
        Message amqp = captor.getValue();
        Object typeHeader = amqp.getMessageProperties().getHeader(RabbitMessagePublishAdapter.HEADER_MESSAGE_TYPE);
        assertThat(typeHeader).isEqualTo(BarrierOpenRequested.MESSAGE_TYPE);
        RoutingMessage roundTrip = jsonMapper.readValue(amqp.getBody(), RoutingMessage.class);
        assertThat(roundTrip.messageType()).isEqualTo(BarrierOpenRequested.MESSAGE_TYPE);
        assertThat(roundTrip.data()).isNotNull();
    }
}
