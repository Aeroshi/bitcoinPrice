package com.aeroshi.bitcoinprice.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aeroshi.bitcoinprice.data.entitys.ValueBitcoin


@Dao
interface ValuesBitcoinRep {

    @Query("SELECT * FROM valuesBitcoin")
    fun getAll(): List<ValueBitcoin>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertData(configManager: List<ValueBitcoin>)

    @Query("DELETE FROM valuesBitcoin")
    fun deleteTable(): Int
}




