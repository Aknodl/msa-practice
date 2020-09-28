package com

import org.springframework.boot.Banner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers

@SpringBootApplication
class EasyChatApp {
    @Bean
    fun springSecurityFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        return http.securityMatcher(ServerWebExchangeMatchers.pathMatchers("/secured/**")).build()
    }
}

fun main(args: Array<String>) {
    runApplication<EasyChatApp>(*args) {
        setBannerMode(Banner.Mode.OFF)
    }
}
