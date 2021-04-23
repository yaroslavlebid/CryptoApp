package course.apps.cryptoapp

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.google.gson.Gson
import course.apps.cryptoapp.api.ApiFactory
import course.apps.cryptoapp.database.AppDatabase
import course.apps.cryptoapp.pojo.CoinPriceInfo
import course.apps.cryptoapp.pojo.CoinPriceInfoRawData
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class CoinViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getInstance(application)
    private val compositeDisposable = CompositeDisposable()

    val priceList = db.coinPriceInfoDao().getPriceList()

    fun getDetailInfo(firstSymbol: String) : LiveData<CoinPriceInfo>
    {
        return db.coinPriceInfoDao().getPriceInfoAboutCoin(firstSymbol)
    }

    init {
        loadData()
    }



    private fun loadData() {
        val disposable = ApiFactory.apiService.getTopCoinsInfo(limit = 50)
            .map { it.data?.map { it.coinInfo?.name }?.joinToString(",") }
            .flatMap { ApiFactory.apiService.getFullPriceList(fromSymbols = it) }
            .map { getPriceListFromRawData(it) }
            .delaySubscription(10, TimeUnit.SECONDS)
            .repeat()
            .retry()
            .subscribeOn(Schedulers.io())
            .subscribe({
                Log.d("TEST_OF_LOADING_DATA", "Success $it")
                db.coinPriceInfoDao().insertPriceList(it)

            }, {
                it.message?.let { it1 -> Log.d("TEST_OF_LOADING_DATA", "Failure! $it1") }
            })
        compositeDisposable.add(disposable)
    }

    private fun getPriceListFromRawData(
        coinPriceInfoRawData: CoinPriceInfoRawData
    ): List<CoinPriceInfo> {
        val result = arrayListOf<CoinPriceInfo>()
        val jsonObject = coinPriceInfoRawData.coinPriceInfoJSONObject ?: return result
        val coinKeySet = jsonObject.keySet()
        for (coinKey in coinKeySet) {
            val currencyJson = jsonObject.getAsJsonObject(coinKey)
            val currencyKeySet = currencyJson.keySet()
            for (currencyKey in currencyKeySet) {
                val priceInfo = Gson().fromJson(
                    currencyJson.getAsJsonObject(currencyKey),
                    CoinPriceInfo::class.java

                )
                result.add(priceInfo)
            }
        }
        return result
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}