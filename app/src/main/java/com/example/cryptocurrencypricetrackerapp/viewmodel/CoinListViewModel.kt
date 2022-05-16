package com.example.cryptocurrencypricetrackerapp.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.cryptocurrencypricetrackerapp.dto.CoinListItem
import com.example.cryptocurrencypricetrackerapp.service.ApiService
import com.example.cryptocurrencypricetrackerapp.service.CoinListDatabase
import com.example.cryptocurrencypricetrackerapp.util.CustomSharedPreferences
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class CoinListViewModel(application: Application): BaseViewModel(application) {

    private val coinListApiService = ApiService()
    private val disposable = CompositeDisposable()
    private var custompref= CustomSharedPreferences(getApplication())
    private var refreshTime = 10 * 60 * 1000 * 1000 * 1000L
    private var page = "1"

    val coinList = MutableLiveData<List<CoinListItem>>()

    val coinListError = MutableLiveData<Boolean>()

    val coinListLoading = MutableLiveData<Boolean>()


    fun refreshData(){
        val updateTime = custompref.getTime()
        if (updateTime != null && updateTime != 0L && System.nanoTime() - updateTime < refreshTime) {
            getDataFromSQlite()
        } else {
            getDataFromAPI()
        }

    }

    private fun getDataFromSQlite(){
        coinListLoading.value=true
        coinListError.value=false
        launch {
            val coins = CoinListDatabase(getApplication()).coinListDao().getAllCoins()
            showCoins(coins)
            Toast.makeText(getApplication(),"Coin List From SQLite", Toast.LENGTH_SHORT).show()
        }
    }

    fun getDataFromAPI() {
        coinListLoading.value = true
        disposable.add(
            coinListApiService.getData()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<CoinListItem>>(){
                    override fun onSuccess(t: List<CoinListItem>) {
                        storeInSQLite(t)
                        Toast.makeText(getApplication(),"Coin List From API",Toast.LENGTH_LONG).show()
                    }
                    override fun onError(e: Throwable) {
                        coinListLoading.value = false
                        coinListError.value = true
                        e.printStackTrace()
                    }

                })
        )
    }

    private fun showCoins(countryList: List<CoinListItem>) {
        coinList.value = countryList
        coinListError.value = false
        coinListLoading.value = false
    }

    private fun storeInSQLite(list: List<CoinListItem>) {
        launch {
            val dao = CoinListDatabase(getApplication()).coinListDao()
            dao.deleteAllCoins()
            val listLong = dao.insertAll(*list.toTypedArray()) // -> list -> individual
            var i = 0
            while (i < list.size) {
                list[i].uuid = listLong[i].toInt()
                i += 1
            }

            showCoins(list)
        }
            custompref.saveTime(System.nanoTime())
    }

    override fun onCleared() {
        super.onCleared()

        disposable.clear()
    }


}