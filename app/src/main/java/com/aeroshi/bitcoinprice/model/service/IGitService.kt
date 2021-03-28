package com.aeroshi.bitcoinprice.model.service

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface IBitcoinService {

    @GET("market-price")
    fun getMarketPrice(
        @Query("format",) format: String,
        @Query("rollingAverage",) rollingAverage: String,
        @Query("timespan",) timespan: String
    ): Single<String>
}