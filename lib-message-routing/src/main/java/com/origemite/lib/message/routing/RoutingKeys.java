package com.origemite.lib.message.routing;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * Rabbit routing key 규칙.
 * 점 세그먼트: {@code {영역}.{리소스}.{…}}.
 * 사가/Kafka 토픽 규칙과 섞지 않는다.
 */
public final class RoutingKeys {

    private RoutingKeys() {
    }

    public static String of(String... segments) {
        Assert.notEmpty(segments, "segments must not be empty");
        StringBuilder sb = new StringBuilder();
        for (String segment : segments) {
            requireSegment(segment);
            if (!sb.isEmpty()) {
                sb.append('.');
            }
            sb.append(segment);
        }
        return sb.toString();
    }

    private static void requireSegment(String value) {
        Assert.isTrue(StringUtils.hasText(value), "segment must not be blank");
        Assert.isTrue(!value.contains("."), "segment must not contain '.'");
        Assert.isTrue(!value.contains(":"), "segment must not contain ':'");
        Assert.isTrue(!value.contains(" "), "segment must not contain space");
    }
}
