package com.aeroshi.bitcoinprice.util

import com.aeroshi.bitcoinprice.data.entitys.MarketPrice
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.*

class MarketPriceUtil {
    companion object {
        fun marketPriceJsonParser(json: String): MarketPrice =
            Gson().fromJson(json, MarketPrice::class.java)

         fun getDateByTimesmap(time: Int): String? {
            val date = Date(time * 1000L)
            val sdf = SimpleDateFormat("dd/MMM/yyyy ")
            sdf.timeZone = TimeZone.getDefault()
            return sdf.format(date)
        }

    }
}