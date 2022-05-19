package com.example.cryptocurrencypricetrackerapp.dto

data class MarketData(
    val current_price: CurrentPrice,
    val price_change_24h: Double,
    val price_change_percentage_24h: Double,
)