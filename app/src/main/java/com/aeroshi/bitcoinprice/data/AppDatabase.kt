package com.aeroshi.bitcoinprice.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.aeroshi.bitcoinprice.data.entitys.ValueBitcoin
import com.aeroshi.bitcoinprice.util.Constants.Companion.DATABASE_NAME


@Database(
    entities = [ValueBitcoin::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun valuesReps(): ValuesBitcoinRep

    companion object {

        // For Singleton instantiation
        @Volatile
        private var instance: AppDatabase? = null


        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                .build()
        }
    }

}
