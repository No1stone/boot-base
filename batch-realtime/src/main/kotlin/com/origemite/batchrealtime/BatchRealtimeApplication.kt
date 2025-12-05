package com.origemite.batchrealtime

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BatchRealtimeApplication

fun main(args: Array<String>) {
	runApplication<BatchRealtimeApplication>(*args)
}
