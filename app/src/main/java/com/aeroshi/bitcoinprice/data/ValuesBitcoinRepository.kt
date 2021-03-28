package com.aeroshi.bitcoinprice.data

import com.aeroshi.bitcoinprice.data.entitys.ValueBitcoin


class ValuesBitcoinRepository private constructor(private val valuesBitcoinRep: ValuesBitcoinRep) {

    fun getAll() = valuesBitcoinRep.getAll()

    fun insert(reps: List<ValueBitcoin>) = valuesBitcoinRep.insertData(reps)

    fun deleteAll() {
        valuesBitcoinRep.deleteTable()
    }

    companion object {

        @Volatile
        private var instance: ValuesBitcoinRepository? = null

        fun getInstance(publicRepsDao: ValuesBitcoinRep) =
            instance
                ?: synchronized(this) {
                    instance
                        ?: ValuesBitcoinRepository(
                            publicRepsDao
                        ).also { instance = it }
                }


    }
}