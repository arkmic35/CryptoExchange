# CryptoExchange
REST API for cryptocurrency conversion. Uses CoinGecko public API.

# Technology stack
* Java 11
* Gradle
* Spring Boot
* Spring WebFlux
* Lombok
* Log4j2
* JUnit
* Mockito
* OkHttp MockWebServer

# How to run
Run `com.michalak.cryptoexchange.CryptoExchangeApplication.main` with Java.

Default port is 8080.

# Endpoint examples
## Get currency rates endpoint
`GET /currencies/BTC?filter[]=USD&filter[]=ETH`

Filtering is optional.

Response:
```json
{
    "target": "bitcoin",
    "rates": {
        "eth": 48.064002,
        "usd": 9635.0
    }
}
```

## Exchange currencies endpoint

`POST /currencies/exchange`

Request body:
```json
{
    "from": "usd",
    "to": [
        "btc",
        "usdt",
        "zec"
    ],
    "amount": 1000
}
```

Response:
```json
[
    {
        "baseCurrency": "usd",
        "quoteCurrency": "bitcoin",
        "exchangeRate": 9610.66,
        "baseCurrencyAmount": 1000,
        "fee": 10,
        "baseCurrencyExchangedAmount": 990,
        "quoteCurrencyAmount": 0.10301061
    },
    {
        "baseCurrency": "usd",
        "quoteCurrency": "zcash",
        "exchangeRate": 42.67,
        "baseCurrencyAmount": 1000,
        "fee": 10,
        "baseCurrencyExchangedAmount": 990,
        "quoteCurrencyAmount": 23.201312
    },
    {
        "baseCurrency": "usd",
        "quoteCurrency": "tether",
        "exchangeRate": 0.998977,
        "baseCurrencyAmount": 1000,
        "fee": 10,
        "baseCurrencyExchangedAmount": 990,
        "quoteCurrencyAmount": 991.01380
    }
]
```
