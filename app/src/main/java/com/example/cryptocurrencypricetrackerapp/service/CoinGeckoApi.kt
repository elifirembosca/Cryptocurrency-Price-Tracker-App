package com.example.cryptocurrencypricetrackerapp.service

import com.example.cryptocurrencypricetrackerapp.dto.CoinListItem
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface CoinGeckoApi {


    @GET("api/v3/coins/markets?vs_currency=usd&order=market_cap_desc&per_page=100&page=1&sparkline=false")
    fun getAllCoins(@Query("page")page: String): Single<List<CoinListItem>>


}