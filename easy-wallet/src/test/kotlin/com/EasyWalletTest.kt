package com

import com.entity.Wallet
import com.service.WalletService
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.r2dbc.core.DatabaseClient
import org.springframework.test.context.TestConstructor
import org.springframework.test.web.reactive.server.WebTestClient

//@ActiveProfiles("dev")
@SpringBootTest
@AutoConfigureWebTestClient
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class EasyWalletTest(
        databaseClient: DatabaseClient,
        private val client: WebTestClient,
        final val walletService: WalletService
) {
    init {
        val rowsUpdated = databaseClient.execute("create table if not exists wallet(id IDENTITY PRIMARY KEY, user_id VARCHAR NOT NULL, balance bigint NOT NULL)").fetch().rowsUpdated().block()!!
        println(rowsUpdated)
    }

    @Test
    fun create() {
        val wallet = createWallet()
        Assertions.assertNotEquals(wallet, null)
    }

    @Test
    fun getWallet() {
        val wallet = createWallet()
        val asWallet = getWallet(wallet.id!!)
        Assertions.assertEquals(wallet.id!!, asWallet.id)
    }

    @Test
    fun deleteWallet() {
        val wallet = createWallet()
        deleteWallet(wallet.id!!)
    }

    @Test
    fun replenish() {
        val wallet = createWallet()
        replenish(wallet.id!!, 10)
        val asWallet = getWallet(wallet.id!!)
        Assertions.assertNotEquals(wallet.balance, asWallet.balance)
    }

    @Test
    fun transferTo() {
        var wallet = createWallet()
        var anotherWallet = createWallet()
        val amount = 10L
        replenish(wallet.id!!, amount)
        wallet = getWallet(wallet.id!!)
        transferTo(wallet.id!!, anotherWallet.id!!, amount).expectStatus().isOk
        anotherWallet = getWallet(anotherWallet.id!!)
        wallet = getWallet(wallet.id!!)
        Assertions.assertNotEquals(amount, wallet.balance)
        Assertions.assertEquals(amount, anotherWallet.balance)
    }


    private fun createWallet() = client.post().uri("/api/wallets").exchange().expectBody(Wallet::class.java).returnResult().responseBody!!

    private fun getWallet(id: Long) = client.get().uri("/api/wallets/{id}", id).exchange().expectBody(Wallet::class.java).returnResult().responseBody!!

    private fun replenish(id: Long, amount: Long) = client.post().uri("/api/wallets/{id}/replenish?amount={amount}", id, amount).exchange().expectStatus().isOk

    private fun transferTo(from: Long, to: Long, amount: Long) = client.post().uri("/api/wallets/{from}/transfer?to={to}&amount={amount}", from, to, amount).exchange()

    private fun deleteWallet(id: Long) = client.delete().uri("/api/wallets/{id}", id).exchange().expectStatus().isOk
}