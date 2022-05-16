package com.example.cryptocurrencypricetrackerapp.service

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.cryptocurrencypricetrackerapp.dto.CoinListItem

@Dao
interface CoinListDao {

    @Insert
    suspend fun insertAll(vararg coinsList: CoinListItem) :List<Long>

    @Query("SELECT * FROM coinListItem")
    suspend fun getAllCoins():List<CoinListItem>

    @Query("SELECT * FROM coinListItem WHERE uuid=:id")
    suspend fun getCoin(id :Int) :CoinListItem

    @Query("DELETE FROM coinListItem")
    suspend fun deleteAllCoins()


}