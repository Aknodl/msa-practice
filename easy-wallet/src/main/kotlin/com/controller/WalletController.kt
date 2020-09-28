package com.controller

import com.entity.Wallet
import com.service.WalletService
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/wallets")
class WalletController(val walletService: WalletService) {


    @PostMapping()
    fun create(): Mono<Wallet> = walletService.create()


    @GetMapping("/{id}")
    fun get(@PathVariable id: Long): Mono<Wallet> = walletService.getId(id)

    @PostMapping("/{id}/replenish")
    fun replenish(@PathVariable id: Long, @RequestParam amount: Long): Mono<Void> = walletService.replenish(id = id, amount = amount)


    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long) {
        walletService.delete(id)
    }

    @PostMapping("/{id}/transfer")
    fun transfer(@PathVariable id: Long, @RequestParam to: Long, @RequestParam amount: Long):
            Mono<Void> = walletService.transfer(id, to, amount)


}