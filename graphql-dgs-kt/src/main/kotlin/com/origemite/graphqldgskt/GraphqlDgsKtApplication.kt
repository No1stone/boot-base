package com.origemite.graphqldgskt

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@EnableConfigurationProperties
@SpringBootApplication(
	scanBasePackages = ["com.origemite.graphqldgskt"
	], exclude = [SecurityAutoConfiguration::class]
)
open class GraphqlDgsKtApplication

fun main(args: Array<String>) {
	runApplication<GraphqlDgsKtApplication>(*args)
}
