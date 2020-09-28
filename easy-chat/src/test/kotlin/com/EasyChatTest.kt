package com

import io.fabric8.openshift.api.model.ProjectRequestBuilder
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.messaging.rsocket.RSocketRequester
import org.springframework.test.context.TestConstructor
import org.springframework.util.Assert
import java.net.URI

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EasyChatTest() {
    lateinit var requester: RSocketRequester

    @BeforeAll
    fun init(@Autowired requestBuilder: RSocketRequester.Builder,
             @LocalServerPort port: Int) {
        requester = requestBuilder
                .connectWebSocket(URI("ws://localhost:$port/rsocket"))
                .block()!!
    }

    @Test
    fun testRSocket() {
        val message = "successful"
        val response = requester.route("/message")
                .data(message)
                .retrieveMono(String::class.java)
                .block()
        Assert.hasText(response, message)
    }
}