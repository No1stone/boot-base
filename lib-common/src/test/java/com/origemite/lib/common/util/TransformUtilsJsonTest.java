package com.origemite.lib.common.util;

import tools.jackson.core.JacksonException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TransformUtilsJsonTest {

    @Test
    void parsesEnvelopeWithAdditionalFields() throws Exception {
        Payload value = TransformUtils.parseJson("{\"id\":7,\"extra\":true}", Payload.class);
        assertThat(value.id()).isEqualTo(7);
        assertThat(TransformUtils.parseTree("{\"id\":7}").path("id").asInt()).isEqualTo(7);
    }

    @Test
    void rejectsMalformedJsonForCallerErrorHandling() {
        assertThatThrownBy(() -> TransformUtils.parseJson("not json", Payload.class))
                .isInstanceOf(JacksonException.class);
        assertThatThrownBy(() -> TransformUtils.parseTree("not json"))
                .isInstanceOf(JacksonException.class);
    }

    public record Payload(int id) {
    }
}
