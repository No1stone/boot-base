package com.origemite.graphqldgskt.config

import com.netflix.discovery.EurekaClient
import com.origemite.lib.grpc.MemberIdentificationGrpc.MemberIdentificationServiceGrpc
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.slf4j.LoggerFactory


@Configuration
class GrpcMemberIdentificationConfig (
    private val eurekaClient: EurekaClient,
){
    private val log = LoggerFactory.getLogger(javaClass)

    @Bean(name = ["authChannel"])
    fun authChannel(): ManagedChannel {
        val app = eurekaClient.getApplication("API-AUTH")
            ?: throw IllegalStateException("Eureka: API-AUTH not found")

        val instance = app.instances.first()

        val grpcPortStr = instance.metadata["grpcPort"]
            ?: instance.port.toString()

        val host = instance.ipAddr   // or instance.hostName
        val grpcPort = grpcPortStr.toInt()

        log.info("Connecting gRPC to api-auth at {}:{}", host, grpcPort)

        return ManagedChannelBuilder
            .forAddress(host, grpcPort)
            .usePlaintext()
            .build()
    }

    @Bean
    fun memberIdentificationBlockingStub(channel: ManagedChannel): MemberIdentificationServiceGrpc.MemberIdentificationServiceBlockingV2Stub? =
        MemberIdentificationServiceGrpc.newBlockingV2Stub(channel)

}