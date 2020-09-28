package com.service

import com.entity.Wallet
import com.repository.WalletRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Mono

@Service
class WalletService(val walletRepository: WalletRepository) {
    fun getId(id: Long): Mono<Wallet> = walletRepository.findById(id)
            .switchIfEmpty(Mono.error(NoSuchElementException("Wallet $id not found")))


    fun create(): Mono<Wallet> {
        val wallet = Wallet(null, "test", 0)
        return walletRepository.save(wallet)
    }

    fun replenish(id: Long, amount: Long) = walletRepository.replenish(id = id, amount = amount)
            .doOnNext { affected ->
                if (affected != 1L) throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Wallet not found")
            }
            .then()

    fun delete(id: Long) = walletRepository.deleteById(id)


    fun transfer(id: Long, to: Long, amount: Long) =
            walletRepository.remittance(id, amount).single()
                    .zipWith(walletRepository.remittance(to, -amount).single()) { affectedFrom, affectedTo ->
                        if (affectedFrom < 0) throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect source wallet")
                        if (affectedTo <= 0) throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect target wallet")
                    }
                    .then()

}