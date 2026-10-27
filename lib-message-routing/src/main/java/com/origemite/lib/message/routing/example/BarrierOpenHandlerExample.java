package com.origemite.lib.message.routing.example;

import com.origemite.lib.message.routing.MessageHandler;
import com.origemite.lib.message.routing.RoutingMessage;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

/**
 * 구독 핸들러 예. 호스트에서 {@code @Component}로 등록하면 Dispatcher가 type으로 연결한다.
 */
public class BarrierOpenHandlerExample implements MessageHandler {

    private final JsonMapper jsonMapper;
    private final Runnable onOpen;

    public BarrierOpenHandlerExample(JsonMapper jsonMapper, Runnable onOpen) {
        this.jsonMapper = jsonMapper;
        this.onOpen = onOpen;
    }

    @Override
    public String messageType() {
        return BarrierOpenRequested.MESSAGE_TYPE;
    }

    @Override
    public void onMessage(RoutingMessage message) {
        BarrierOpenRequested payload = convertData(message.data());
        // 실제: 차단기 장치 어댑터 호출 등
        if (payload != null) {
            onOpen.run();
        }
    }

    private BarrierOpenRequested convertData(Object data) {
        if (data instanceof BarrierOpenRequested requested) {
            return requested;
        }
        JsonNode node = jsonMapper.valueToTree(data);
        return jsonMapper.convertValue(node, BarrierOpenRequested.class);
    }
}
