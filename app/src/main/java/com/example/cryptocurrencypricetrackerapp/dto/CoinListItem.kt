package com.example.cryptocurrencypricetrackerapp.dto

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class CoinListItem(
    val current_price: Double? = null,
    @ColumnInfo(name = "id")
    @SerializedName("id")
    var coinId: String? = null,
    @ColumnInfo(name = "image")
    @SerializedName("image")
    var coinImage: String? = null,
    val market_cap_change_percentage_24h: Double? = null,
    @ColumnInfo(name = "name")
    @SerializedName("name")
    var coinName: String? = null,
    val price_change_percentage_24h: Double? = null,
    val symbol: String? = null,
){
    @PrimaryKey(autoGenerate = true)
    var uuid: Int = 0
}