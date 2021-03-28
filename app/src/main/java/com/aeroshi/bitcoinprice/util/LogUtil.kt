package com.aeroshi.bitcoinprice.util

import java.text.SimpleDateFormat
import java.util.*

abstract class LogUtil {

    companion object {
        fun getDate(): String = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())

        fun getTime(): String = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
    }
}

