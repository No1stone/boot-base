package com.origemite.lib.message.routing.rabbit;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 라우팅 exchange·연결 복구.
 * {@code origemite.message.routing.*}
 */
@ConfigurationProperties(prefix = "origemite.message.routing")
public class MessageRoutingProperties {

    /** topic exchange 이름. */
    private String exchange = "app.topic";

    /** 네트워크 단절 후 재연결 시도 간격(ms). Rabbit automatic recovery. */
    private long networkRecoveryIntervalMs = 5_000L;

    /** heartbeat(초). 0이면 클라이언트 기본. */
    private int requestedHeartbeatSeconds = 30;

    /** 리스너가 큐 부재 시 치명으로 볼지. 재연결 중에는 false 권장. */
    private boolean missingQueuesFatal = false;

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public long getNetworkRecoveryIntervalMs() {
        return networkRecoveryIntervalMs;
    }

    public void setNetworkRecoveryIntervalMs(long networkRecoveryIntervalMs) {
        this.networkRecoveryIntervalMs = networkRecoveryIntervalMs;
    }

    public int getRequestedHeartbeatSeconds() {
        return requestedHeartbeatSeconds;
    }

    public void setRequestedHeartbeatSeconds(int requestedHeartbeatSeconds) {
        this.requestedHeartbeatSeconds = requestedHeartbeatSeconds;
    }

    public boolean isMissingQueuesFatal() {
        return missingQueuesFatal;
    }

    public void setMissingQueuesFatal(boolean missingQueuesFatal) {
        this.missingQueuesFatal = missingQueuesFatal;
    }
}
