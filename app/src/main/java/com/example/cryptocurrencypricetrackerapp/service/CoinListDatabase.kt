package com.example.cryptocurrencypricetrackerapp.service

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.cryptocurrencypricetrackerapp.dto.CoinListItem

@Database(entities = [CoinListItem::class],version = 1)
abstract class CoinListDatabase : RoomDatabase() {

    abstract fun coinListDao() : CoinListDao

    companion object {

        @Volatile private var instance : CoinListDatabase? = null

        private val lock = Any()

        operator fun invoke(context : Context) = instance ?: synchronized(lock) {
            instance ?: makeDatabase(context).also {
                instance = it
            }
        }

        private fun makeDatabase(context : Context) = Room.databaseBuilder(
            context.applicationContext,CoinListDatabase::class.java,"coindatabase"
        ).build()
    }
}