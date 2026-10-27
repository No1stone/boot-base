package com.origemite.graphqldgskt.config

import com.origemite.lib.grpc.MemberIdentificationGrpc.MemberIdentificationServiceGrpc
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GrpcMemberIdentificationConfig(
    @Value("\${app.grpc.api-auth.host}") private val apiAuthHost: String,
    @Value("\${app.grpc.api-auth.port}") private val apiAuthPort: Int,
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @Bean(name = ["authChannel"])
    fun authChannel(): ManagedChannel {
        log.info("Connecting gRPC to api-auth at {}:{}", apiAuthHost, apiAuthPort)

        return ManagedChannelBuilder
            .forAddress(apiAuthHost, apiAuthPort)
            .usePlaintext()
            .build()
    }

    @Bean
    fun memberIdentificationBlockingStub(channel: ManagedChannel): MemberIdentificationServiceGrpc.MemberIdentificationServiceBlockingV2Stub? =
        MemberIdentificationServiceGrpc.newBlockingV2Stub(channel)
}
