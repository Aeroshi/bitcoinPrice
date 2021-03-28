package com.aeroshi.bitcoinprice

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.aeroshi.bitcoinprice.data.entitys.enuns.ErrorType
import com.aeroshi.bitcoinprice.model.repository.BitcoinRepository
import com.aeroshi.bitcoinprice.util.StringUtil
import com.aeroshi.bitcoinprice.util.TrampolineSchedulerProvider
import com.aeroshi.bitcoinprice.viewmodels.HomeViewModel
import io.reactivex.Single
import org.junit.*
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations


@RunWith(JUnit4::class)
class HomeViewModelTest {

    companion object {
        private var mJsonSuccess: String? = null
        private var mJsonError: String? = null


        @BeforeClass
        @JvmStatic
        fun executeOnce() {
            this::class.java.classLoader?.let { classLoader ->
                mJsonSuccess = classLoader.getResource("json/successReturn.json").readText()
                mJsonError = classLoader.getResource("json/errorReturn.json").readText()
            }
        }
    }

    @get:Rule
    var mRule: TestRule = InstantTaskExecutorRule()

    private val context = Mockito.mock(Context::class.java)


    @Mock
    lateinit var mValuesBitcoinRepository: BitcoinRepository



    private lateinit var mViewModel: HomeViewModel


    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        mViewModel =
            HomeViewModel(
                mValuesBitcoinRepository,
                TrampolineSchedulerProvider()
            )
    }

    @Test
    fun HomeViewModel_doMaketPrice_deve_retorna_sucesso() {
        // Preparing
        val fakeJsonReturn = mJsonSuccess
            ?: StringUtil.EMPTY

        // Mock
        Mockito.`when`(mValuesBitcoinRepository.doMaketPrice("1year"))
            .thenReturn(Single.just(fakeJsonReturn))

        // Call
        mViewModel.doMaketPrice(context,"1year")

        // Assert
        verify(mValuesBitcoinRepository, times(1)).doMaketPrice("1year")

        Assert.assertNotNull(mViewModel.mBitcoinsValuesResult.value)
    }

    @Test
    fun HomeViewModel_doPubicRepositories_deve_retorna_erro_parser() {
        // Preparing
        val fakeJsonReturn = mJsonError
            ?: StringUtil.EMPTY

        // Mock
        Mockito.`when`(mValuesBitcoinRepository.doMaketPrice("1year"))
            .thenReturn(Single.just(fakeJsonReturn))


        // Call
        mViewModel.doMaketPrice(context,"1year")

        // Assert
        verify(mValuesBitcoinRepository, times(1)).doMaketPrice("1year")

        Assert.assertEquals(ErrorType.PARSER, mViewModel.mBitcoinsValuesResult.value!!.second)
    }

}