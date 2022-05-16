package com.example.cryptocurrencypricetrackerapp.service

import com.example.cryptocurrencypricetrackerapp.dto.CoinListItem
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class ApiService {
    private val BASE_URL = "https://api.coingecko.com/"
    private val api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(CoinGeckoApi::class.java)

    fun getData() : Single<List<CoinListItem>> {
        return api.getAllCoins("1")
    }
}