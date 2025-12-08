package com.origemite.batchrealtime

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.data.jdbc.JdbcRepositoriesAutoConfiguration
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@EnableConfigurationProperties
@EnableJpaAuditing
@EnableCaching
@EnableBatchProcessing
@SpringBootApplication(
	scanBasePackages = ["com.origemite.batchrealtime"
		, "com.origemite.lib.common"
		, "com.origemite.lib.model"
	],
//	exclude = [RedisRepositoriesAutoConfiguration::class, JdbcRepositoriesAutoConfiguration::class, SecurityAutoConfiguration::class
//	]
)

class BatchRealtimeApplication

fun main(args: Array<String>) {
	runApplication<BatchRealtimeApplication>(*args)
}
