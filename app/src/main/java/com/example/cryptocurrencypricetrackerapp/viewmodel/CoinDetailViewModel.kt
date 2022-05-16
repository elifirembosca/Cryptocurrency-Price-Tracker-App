package com.example.cryptocurrencypricetrackerapp.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.cryptocurrencypricetrackerapp.dto.CoinListItem
import com.example.cryptocurrencypricetrackerapp.service.CoinListDatabase
import kotlinx.coroutines.launch

class CoinDetailViewModel(application: Application) : BaseViewModel(application) {

    val countryLiveData = MutableLiveData<CoinListItem>()

    fun getDataFromRoom(uuid: Int) {
        launch {
            val dao = CoinListDatabase(getApplication()).coinListDao()
            val coin = dao.getCoin(uuid)
            countryLiveData.value = coin
        }

    }
}