package com.origemite.lib.message.saga;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * 토픽: {@code {type}.{domain}} — 예: {@code create.order}, {@code status.payment}.
 */
public final class SagaTopics {

    private SagaTopics() {
    }

    public static String of(SagaMessageType type, String domain) {
        Assert.notNull(type, "type must not be null");
        requireDomain(domain);
        return type.topicSegment() + "." + domain;
    }

    public static String of(SagaMessage message) {
        return of(message.type(), message.domain());
    }

    private static void requireDomain(String domain) {
        Assert.isTrue(StringUtils.hasText(domain), "domain must not be blank");
        Assert.isTrue(!domain.contains("."), "domain must not contain '.'");
        Assert.isTrue(!domain.contains(" "), "domain must not contain space");
    }
}
