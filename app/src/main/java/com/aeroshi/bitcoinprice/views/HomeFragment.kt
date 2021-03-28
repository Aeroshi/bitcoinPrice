package com.aeroshi.bitcoinprice.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.aeroshi.bitcoinprice.MainActivity
import com.aeroshi.bitcoinprice.R
import com.aeroshi.bitcoinprice.data.AppDatabase
import com.aeroshi.bitcoinprice.data.ValuesBitcoinRepository
import com.aeroshi.bitcoinprice.data.entitys.ValueBitcoin
import com.aeroshi.bitcoinprice.data.entitys.enuns.ErrorType
import com.aeroshi.bitcoinprice.databinding.FragmentHomeBinding
import com.aeroshi.bitcoinprice.extensions.logDebug
import com.aeroshi.bitcoinprice.util.Executors.Companion.ioThread
import com.aeroshi.bitcoinprice.util.InternetUtil.Companion.isOnline
import com.aeroshi.bitcoinprice.util.MarketPriceUtil.Companion.getDateByTimesmap
import com.aeroshi.bitcoinprice.viewmodels.HomeViewModel
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Cartesian
import com.google.android.material.snackbar.Snackbar


class HomeFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    companion object {
        private const val TAG = "HomeFragment"
    }

    private lateinit var mMainActivity: MainActivity


    private lateinit var mViewModel: HomeViewModel

    private lateinit var mBinding: FragmentHomeBinding

    lateinit var lineChart: Cartesian


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mMainActivity = activity as MainActivity


        if (!isViewModelInitialized())
            initializeViewModel()

        if (!isBindingInitialized())
            initializeBinding(inflater, container)

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addViewModelObservers()
    }


    override fun onRefresh() {
        mViewModel.doMaketPrice(mMainActivity.applicationContext, "30days")
        if (isOnline(mMainActivity.applicationContext)) {
            ioThread {
                mMainActivity.applicationContext.let { context ->

                    val configManagerRepository = ValuesBitcoinRepository.getInstance(
                        AppDatabase.getInstance(context).valuesReps()
                    )
                    configManagerRepository.deleteAll()

                    mViewModel.doMaketPrice(context, "30days")

                }
            }
        } else {
            mBinding.swipeRefresh.isRefreshing = false
            Snackbar.make(
                mBinding.principal,
                R.string.error,
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }

    override fun onStop() {
        logDebug(TAG, "onStop")
        super.onStop()
        mViewModel.clearDisposables()
    }


    private fun isViewModelInitialized() = ::mViewModel.isInitialized

    private fun initializeViewModel() {
        mViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
    }

    private fun addViewModelObservers() {
        mViewModel.mBitcoinsValuesResult.observe(viewLifecycleOwner, observeValuesBitcoins())
    }

    private fun isBindingInitialized() = ::mBinding.isInitialized

    private fun initializeBinding(inflater: LayoutInflater, container: ViewGroup?) {
        mBinding = FragmentHomeBinding.inflate(inflater, container, false)
        mBinding.lifecycleOwner = this
        mBinding.swipeRefresh.setOnRefreshListener(this)
        startLoading()
        ioThread {
            mMainActivity.applicationContext.let { context ->

                val managerRepository = ValuesBitcoinRepository.getInstance(
                    AppDatabase.getInstance(context).valuesReps()
                )
                val repositories = managerRepository.getAll()
                if (repositories.isNullOrEmpty()) {
                    mBinding.swipeRefresh.isRefreshing = true
                    mViewModel.doMaketPrice(context, "30days")
                } else {
                    mViewModel.mBitcoinsValuesResult.postValue(Pair(repositories, ErrorType.NONE))
                }
            }
        }
        listeners()
    }


    private fun listeners() {

        mBinding.buttonThirtyDays.setOnClickListener {
            mViewModel.doMaketPrice(mMainActivity.applicationContext, "30days")
        }
        mBinding.buttonOneYear.setOnClickListener {
            mViewModel.doMaketPrice(mMainActivity.applicationContext, "1years")
        }
        mBinding.buttonThreYears.setOnClickListener {
            mViewModel.doMaketPrice(mMainActivity.applicationContext, "3years")
        }
        mBinding.buttonAll.setOnClickListener {
            mViewModel.doMaketPrice(mMainActivity.applicationContext, "10years")
        }
    }


    private fun observeValuesBitcoins(): Observer<Pair<List<ValueBitcoin>?, ErrorType>> {
        return Observer {
            if (it.second == ErrorType.NONE) {
                if (!it.first.isNullOrEmpty()) {
                    val values = it.first!!
                    mBinding.valueDay.text = "US: ${values[values.size - 1].price}"
                    mBinding.date.text = getDateByTimesmap(values[values.size - 1].dateInTimeStamp)

                    updateChart(values)
                }
                finishLoading()
            } else {
                Snackbar.make(
                    mBinding.principal,
                    R.string.error,
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun updateChart(values: List<ValueBitcoin>) {
        this.lineChart = AnyChart.line()

        val data: MutableList<DataEntry> = ArrayList()

        values.forEach { bitcoin ->
            data.add(
                ValueDataEntry(
                    getDateByTimesmap(bitcoin.dateInTimeStamp),
                    bitcoin.price
                )
            )
        }
        this.lineChart.data(data)
        this.lineChart.inMarquee();
        mBinding.anyChartView.setChart(this.lineChart)
    }


    private fun finishLoading() {
        mBinding.swipeRefresh.isRefreshing = false
    }

    private fun startLoading() {
        mBinding.swipeRefresh.isRefreshing = true
    }


}