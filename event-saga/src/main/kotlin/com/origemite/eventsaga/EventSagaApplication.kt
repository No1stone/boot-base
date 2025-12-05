package com.origemite.eventsaga

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class EventSagaApplication

fun main(args: Array<String>) {
    runApplication<EventSagaApplication>(*args)
}
