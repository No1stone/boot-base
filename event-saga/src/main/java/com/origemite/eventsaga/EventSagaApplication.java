package com.origemite.eventsaga;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {"com.origemite.eventsaga", "com.origemite.lib.message.saga"})
@EnableScheduling
public class EventSagaApplication {

    public static void main(String[] args) {
        SpringApplication.run(EventSagaApplication.class, args);
    }
}
