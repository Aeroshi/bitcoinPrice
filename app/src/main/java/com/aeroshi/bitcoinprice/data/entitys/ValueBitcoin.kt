package com.aeroshi.bitcoinprice.data.entitys

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "valuesBitcoin")
data class ValueBitcoin(
    @PrimaryKey
    val id: Long,
    @SerializedName("x") val dateInTimeStamp : Int,
    @SerializedName("y") val price : Double
)