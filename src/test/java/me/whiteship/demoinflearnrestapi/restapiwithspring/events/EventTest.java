package me.whiteship.demoinflearnrestapi.restapiwithspring.events;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EventTest {

    @Test
    void builder() {
        // given
        Event event = Event.builder().build();
        assertThat(event).isNotNull();
    }
}