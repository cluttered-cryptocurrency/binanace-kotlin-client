package com.cluttered.cryptocurrency

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class PublicBinanceClientTest {

    private val client = PublicBinanceClient()

    @Test
    fun testPing() {
        client.public.ping()
                .test()
                .assertComplete()
    }

    @Test
    fun testTime() {
        val testObserver = client.public.time().test()

        println(testObserver.values()[0])

        testObserver.assertComplete()
        testObserver.assertValueCount(1)

        assertThat(testObserver.values()[0].serverTime).isNotNull()
    }

    @Test
    fun testExchangeInfo() {
        val testObserver = client.public.exchangeInfo().test()

        testObserver.assertComplete()
        testObserver.assertValueCount(1)

        val result = testObserver.values()
        println(result[0])

        assertThat(result[0].timezone).isEqualTo("UTC")
    }

    @Test
    fun testDepth() {
        val testObserver = client.public.depth("ETHBTC").test()

        testObserver.assertComplete()
        testObserver.assertValueCount(1)

        val result = testObserver.values()
        println(result[0])

        assertThat(result[0].lastUpdateId.toLong()).isGreaterThan(0)
    }

    @Test
    fun testDepthLimit() {
        val testObserver = client.public.depth("ETHBTC", 5).test()

        testObserver.assertComplete()
        testObserver.assertValueCount(1)

        val result = testObserver.values()
        println(result[0])

        assertThat(result[0].lastUpdateId.toLong()).isGreaterThan(0)
    }

    @Test
    fun testTrades() {
        val testObserver = client.public.trades("ETHBTC").test()

        testObserver.assertComplete()
        testObserver.assertValueCount(1)

        val result = testObserver.values()
        println(result[0])

        assertThat(result[0].size).isEqualTo(500)
    }

    @Test
    fun testTradesLimit() {
        val testObserver = client.public.trades("ETHBTC", 25).test()

        testObserver.assertComplete()
        testObserver.assertValueCount(1)

        val result = testObserver.values()
        println(result[0])

        assertThat(result[0].size).isEqualTo(25)
    }
}