package com

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
class EasyWalletApp

fun main(args: Array<String>) {
    runApplication<EasyWalletApp>(*args)
}
