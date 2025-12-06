package com.origemite.graphqldgskt.member

import com.netflix.graphql.dgs.DgsQueryExecutor
import com.netflix.graphql.dgs.client.codegen.GraphQLQueryRequest
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.graphql.GraphQlRequest
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MemberIdentificationGraphQLTest {

    private lateinit var client: WebTestClient
    @BeforeEach
    fun setUp() {
        client = WebTestClient.bindToServer()
            .baseUrl("http://localhost:3033")
            .build()
    }
    @Test
    fun `member identification graphql`() {
        val query = """
            query {
              memberIdentification(id: 1) {
                id
                name
                email
              }
            }
        """

        client.post()
            .uri("/graphql")
            .bodyValue(mapOf("query" to query))
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .consumeWith { println("GRAPHQL RES = " + String(it.responseBody!!)) }
            .jsonPath("$.data.memberIdentification.id").isEqualTo(1)
    }
}