package com.aeroshi.bitcoinprice.extensions


import android.util.Log
import com.aeroshi.bitcoinprice.util.LogUtil

private const val TAG = ">>>>> [BitcoinPrice]"
private const val PIPE = "|"


fun logInfo(className: String, message: String) {
    Log.i(
        TAG,
        "${LogUtil.getDate()} $PIPE ${LogUtil.getTime()} $PIPE ${Thread.currentThread().name} $PIPE Information $PIPE $className $PIPE $message"
    )
}

fun logDebug(className: String, message: String) {
    Log.d(
        TAG,
        "${LogUtil.getDate()} $PIPE ${LogUtil.getTime()} $PIPE ${Thread.currentThread().name} $PIPE Debug $PIPE $className $PIPE $message"
    )
}

fun logWarning(className: String, message: String) {
    Log.w(
        TAG,
        "${LogUtil.getDate()} $PIPE ${LogUtil.getTime()} $PIPE ${Thread.currentThread().name} $PIPE Warning $PIPE $className $PIPE $message"
    )
}

fun logError(
    className: String,
    message: String,
    exception: Exception
) {
    Log.w(
        TAG,
        "${LogUtil.getDate()} $PIPE ${LogUtil.getTime()} $PIPE ${Thread.currentThread().name} $PIPE Error $PIPE $className $PIPE $message $PIPE ${exception.message}"
    )
    exception.printStackTrace()
}

fun logError(
    className: String,
    message: String,
    throwable: Throwable
) {
    Log.w(
        TAG,
        "${LogUtil.getDate()} $PIPE ${LogUtil.getTime()} $PIPE ${Thread.currentThread().name} $PIPE Error $PIPE $className $PIPE $message $PIPE ${throwable.message}"
    )
    throwable.printStackTrace()
}

