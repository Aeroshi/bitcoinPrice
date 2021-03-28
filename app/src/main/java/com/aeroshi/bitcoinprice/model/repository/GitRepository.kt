package com.aeroshi.bitcoinprice.model.repository

import com.aeroshi.bitcoinprice.model.NetworkAPI
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class bitcoinRepository {

    fun doMaketPrice(timespan: String): Single<String> {
        return NetworkAPI.getGitService()
            .getMarketPrice("json","23hours",timespan)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}