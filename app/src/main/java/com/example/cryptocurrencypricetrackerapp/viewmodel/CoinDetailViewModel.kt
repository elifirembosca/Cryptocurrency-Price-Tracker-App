package com.example.cryptocurrencypricetrackerapp.viewmodel

import android.app.Application
import android.content.ContentValues
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.cryptocurrencypricetrackerapp.dto.CoinDetail
import com.example.cryptocurrencypricetrackerapp.service.ApiService
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class CoinDetailViewModel(application: Application) : BaseViewModel(application) {

    private val disposable = CompositeDisposable()
    private val coinApiService = ApiService()

    val coinDetail = MutableLiveData<CoinDetail>()
    val coinDetailError = MutableLiveData<Boolean>()
    val coinDetailLoading = MutableLiveData<Boolean>()


    fun getDataFromAPI(coinName: String) {
        coinDetailLoading.value = true
        disposable.add(
            coinApiService.getCoin(coinName)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<CoinDetail>(){
                    override fun onSuccess(t: CoinDetail) {
                        showCoin(t)
                        Toast.makeText(getApplication(),"Coin Detail From API", Toast.LENGTH_LONG).show()
                    }
                    override fun onError(e: Throwable) {
                        coinDetailLoading.value = false
                        coinDetailError.value = true
                    }
                })
        )
    }

    private fun showCoin(coin : CoinDetail) {
        coinDetail.value = coin
        coinDetailError.value = false
        coinDetailLoading.value = false
    }

    class FirebaseUtils {
        val fireStoreDatabase = FirebaseFirestore.getInstance()
        val db = Firebase.firestore
    }

    fun uploadData(coinName: String) : String {
        var documentId = ""
        val hashMap = hashMapOf<String, Any>(
            "coin_id" to coinName
        )
        FirebaseUtils().fireStoreDatabase.collection("coins")
            .add(hashMap)
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "Added document with ID ${it.id}")
                documentId = it.id
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error adding document $exception")
            }
        return documentId
    }

    fun deleteData(documentId: String) {
        FirebaseUtils().db.collection("coins").document(documentId)
            .delete()
            .addOnSuccessListener { Log.d(ContentValues.TAG, "DocumentSnapshot successfully deleted!") }
            .addOnFailureListener { e -> Log.w(ContentValues.TAG, "Error deleting document", e) }
    }

}