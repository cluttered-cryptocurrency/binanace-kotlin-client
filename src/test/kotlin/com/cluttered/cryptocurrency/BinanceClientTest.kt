package com.cluttered.cryptocurrency

import com.cluttered.cryptocurrency.TestHelpers.getJson
import com.cluttered.cryptocurrency.model.enum.DepthLimit.ONE_HUNDRED
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.math.BigDecimal
import java.time.Instant

@RunWith(JUnit4::class)
class BinanceClientTest {

    private lateinit var binanceClient: BinanceClient
    private lateinit var mockServer: MockWebServer

    @Before
    @Throws
    fun setUp() {
        mockServer = MockWebServer()
        mockServer.start()

        BinanceConstants.BASE_URL = mockServer.url("").toString()
        binanceClient = BinanceClient()
    }

    @After
    @Throws
    fun tearDown() {
        mockServer.shutdown()
    }

    @Test
    fun testPing() {
        val path = "/api/v1/ping"

        mockServer.enqueue(
                MockResponse()
                        .setResponseCode(200)
                        .addHeader("Content-Type", "application/json")
                        .setBody("{}"))

        val testObserver = binanceClient.rest.ping().test()

        testObserver.assertNoErrors()
        testObserver.assertValueCount(0)
        testObserver.assertComplete()

        val request = mockServer.takeRequest()
        assertThat(request.path).isEqualTo(path)
    }

    @Test
    fun testTime() {
        val path = "/api/v1/time"

        mockServer.enqueue(
                MockResponse()
                        .setResponseCode(200)
                        .addHeader("Content-Type", "application/json")
                        .setBody(getJson("json/time.json")))

        val testObserver = binanceClient.rest.time().test()

        testObserver.assertNoErrors()
        testObserver.assertValueCount(1)

        assertThat(testObserver.values()[0].serverTime).isEqualTo(Instant.ofEpochMilli(1499827319559))

        val request = mockServer.takeRequest()
        assertThat(request.path).isEqualTo(path)
    }

    @Test
    fun testExchangeInfo() {
        val path = "/api/v1/exchangeInfo"

        mockServer.enqueue(
                MockResponse()
                        .setResponseCode(200)
                        .addHeader("Content-Type", "application/json")
                        .setBody(getJson("json/exchangeInfo.json")))

        val testObserver = binanceClient.rest.exchangeInfo().test()

        testObserver.assertNoErrors()
        testObserver.assertValueCount(1)

        assertThat(testObserver.values()[0].serverTime).isEqualTo(Instant.ofEpochMilli(1508631584636))

        val request = mockServer.takeRequest()
        assertThat(request.path).isEqualTo(path)
    }

    @Test
    fun testDepth() {
        val path = "/api/v1/depth?symbol=ETHBTC"

        mockServer.enqueue(
                MockResponse()
                        .setResponseCode(200)
                        .addHeader("Content-Type", "application/json")
                        .setBody(getJson("json/depth.json")))

        val testObserver = binanceClient.rest.depth("ETHBTC").test()

        testObserver.assertNoErrors()
        testObserver.assertValueCount(1)

        assertThat(testObserver.values()[0].lastUpdateId).isEqualTo(1027024)

        val request = mockServer.takeRequest()
        assertThat(request.path).isEqualTo(path)
    }

    @Test
    fun testDepthLimits() {
        val path = "/api/v1/depth?symbol=ETHBTC&limit=100"

        mockServer.enqueue(
                MockResponse()
                        .setResponseCode(200)
                        .addHeader("Content-Type", "application/json")
                        .setBody(getJson("json/depth.json")))

        val testObserver = binanceClient.rest.depth("ETHBTC", ONE_HUNDRED).test()

        testObserver.assertNoErrors()
        testObserver.assertValueCount(1)

        assertThat(testObserver.values()[0].lastUpdateId).isEqualTo(1027024)

        val request = mockServer.takeRequest()
        assertThat(request.path).isEqualTo(path)
    }

    @Test
    fun testTrades() {
        val path = "/api/v1/trades?symbol=ETHBTC"

        mockServer.enqueue(
                MockResponse()
                        .setResponseCode(200)
                        .addHeader("Content-Type", "application/json")
                        .setBody(getJson("json/trades.json")))

        val testObserver = binanceClient.rest.trades("ETHBTC").test()

        testObserver.assertNoErrors()
        testObserver.assertValueCount(1)

        assertThat(testObserver.values()[0][0].quantity).isEqualTo(BigDecimal("12.00000000"))

        val request = mockServer.takeRequest()
        assertThat(request.path).isEqualTo(path)
    }

    @Test
    fun testTradesLimits() {
        val path = "/api/v1/trades?symbol=ETHBTC&limit=250"

        mockServer.enqueue(
                MockResponse()
                        .setResponseCode(200)
                        .addHeader("Content-Type", "application/json")
                        .setBody(getJson("json/trades.json")))

        val testObserver = binanceClient.rest.trades("ETHBTC", 250).test()

        testObserver.assertNoErrors()
        testObserver.assertValueCount(1)

        assertThat(testObserver.values()[0][0].quantity).isEqualTo(BigDecimal("12.00000000"))

        val request = mockServer.takeRequest()
        assertThat(request.path).isEqualTo(path)
    }

    @Test
    fun testHistoricalTrades() {
        val path = "/api/v1/historicalTrades?symbol=ETHBTC"

        mockServer.enqueue(
                MockResponse()
                        .setResponseCode(200)
                        .addHeader("Content-Type", "application/json")
                        .setBody(getJson("json/trades.json")))

        val testObserver = binanceClient.rest.historicalTrades("ETHBTC").test()

        testObserver.assertNoErrors()
        testObserver.assertValueCount(1)

        assertThat(testObserver.values()[0][0].quantity).isEqualTo(BigDecimal("12.00000000"))

        val request = mockServer.takeRequest()
        assertThat(request.path).isEqualTo(path)
    }

    @Test
    fun testHistoricalTradesLimit() {
        val path = "/api/v1/historicalTrades?symbol=ETHBTC&limit=235"

        mockServer.enqueue(
                MockResponse()
                        .setResponseCode(200)
                        .addHeader("Content-Type", "application/json")
                        .setBody(getJson("json/trades.json")))

        val testObserver = binanceClient.rest.historicalTrades("ETHBTC", 235).test()

        testObserver.assertNoErrors()
        testObserver.assertValueCount(1)

        assertThat(testObserver.values()[0][0].quantity).isEqualTo(BigDecimal("12.00000000"))

        val request = mockServer.takeRequest()
        assertThat(request.path).isEqualTo(path)
    }

    @Test
    fun testHistoricalTradesFromId() {
        val path = "/api/v1/historicalTrades?symbol=ETHBTC&fromId=6374"

        mockServer.enqueue(
                MockResponse()
                        .setResponseCode(200)
                        .addHeader("Content-Type", "application/json")
                        .setBody(getJson("json/trades.json")))

        val testObserver = binanceClient.rest.historicalTrades("ETHBTC", fromId = 6374).test()

        testObserver.assertNoErrors()
        testObserver.assertValueCount(1)

        assertThat(testObserver.values()[0][0].quantity).isEqualTo(BigDecimal("12.00000000"))

        val request = mockServer.takeRequest()
        assertThat(request.path).isEqualTo(path)
    }

    @Test
    fun testHistoricalTradesLimitFromId() {
        val path = "/api/v1/historicalTrades?symbol=ETHBTC&limit=75&fromId=123456"

        mockServer.enqueue(
                MockResponse()
                        .setResponseCode(200)
                        .addHeader("Content-Type", "application/json")
                        .setBody(getJson("json/trades.json")))

        val testObserver = binanceClient.rest.historicalTrades("ETHBTC", 75, 123456).test()

        testObserver.assertNoErrors()
        testObserver.assertValueCount(1)

        assertThat(testObserver.values()[0][0].quantity).isEqualTo(BigDecimal("12.00000000"))

        val request = mockServer.takeRequest()
        assertThat(request.path).isEqualTo(path)
    }

    @Test
    fun testAggregateTrades() {
        val path = "/api/v1/aggTrades?symbol=ETHBTC"

        mockServer.enqueue(
                MockResponse()
                        .setResponseCode(200)
                        .addHeader("Content-Type", "application/json")
                        .setBody(getJson("json/aggregateTrades.json")))

        val testObserver = binanceClient.rest.aggregateTrades("ETHBTC").test()

        testObserver.assertNoErrors()
        testObserver.assertValueCount(1)

        assertThat(testObserver.values()[0][0].price).isEqualTo(BigDecimal("0.01633102"))

        val request = mockServer.takeRequest()
        assertThat(request.path).isEqualTo(path)
    }
}