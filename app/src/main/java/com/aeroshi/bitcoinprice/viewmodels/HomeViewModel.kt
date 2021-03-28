package com.aeroshi.bitcoinprice.viewmodels


import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aeroshi.bitcoinprice.data.AppDatabase
import com.aeroshi.bitcoinprice.data.ValuesBitcoinRepository
import com.aeroshi.bitcoinprice.data.entitys.ValueBitcoin
import com.aeroshi.bitcoinprice.data.entitys.enuns.ErrorType
import com.aeroshi.bitcoinprice.extensions.logError
import com.aeroshi.bitcoinprice.model.repository.BitcoinRepository
import com.aeroshi.bitcoinprice.util.BaseSchedulerProvider
import com.aeroshi.bitcoinprice.util.Executors.Companion.ioThread
import com.aeroshi.bitcoinprice.util.MarketPriceUtil.Companion.marketPriceJsonParser
import com.aeroshi.bitcoinprice.util.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy


class HomeViewModel(
    private val mRepository: BitcoinRepository = BitcoinRepository(),
    private val mScheduler: BaseSchedulerProvider = SchedulerProvider()
) : ViewModel() {
    companion object {
        private const val TAG = "HomeViewModel"
    }

    private val mCompositeDisposable = CompositeDisposable()

    val mBitcoinsValuesResult = MutableLiveData<Pair<List<ValueBitcoin>?, ErrorType>>()

    override fun onCleared() {
        super.onCleared()
        clearDisposables()
    }

    fun clearDisposables() = mCompositeDisposable.clear()


    fun doMaketPrice(context: Context, timespan: String) {
        mCompositeDisposable.add(
            mRepository
                .doMaketPrice(timespan)
                .subscribeOn(mScheduler.io())
                .observeOn(mScheduler.ui())
                .subscribeBy(
                    onSuccess = { jsonResult ->
                        try {
                            var test = marketPriceJsonParser(jsonResult)
                            setResult(marketPriceJsonParser(jsonResult).values, context)
                        } catch (exception: Exception) {
                            mBitcoinsValuesResult.value = Pair(null, ErrorType.PARSER)
                            logError(TAG, "Error on parser response of doMaketPrice", exception)
                        }
                    },
                    onError = {
                        mBitcoinsValuesResult.value = Pair(null, ErrorType.NETWORK)
                        logError(TAG, "Error on get response of doMaketPrice", it)
                    }
                )
        )
    }

    private fun setResult(valuesBitcoin: List<ValueBitcoin>, context: Context) {
        saverRepOnDb(valuesBitcoin, context)
        mBitcoinsValuesResult.value = Pair(valuesBitcoin, ErrorType.NONE)
    }

    private fun saverRepOnDb(reps: List<ValueBitcoin>, context: Context) {
        ioThread {
            try {
                val configManagerRepository =
                    ValuesBitcoinRepository.getInstance(
                        AppDatabase.getInstance(context).valuesReps()
                    )
                configManagerRepository.deleteAll()
                configManagerRepository.insert(reps)
            } catch (exception: Exception) {
                logError(TAG, "Error on save data on db", exception)
            }
        }
    }

}