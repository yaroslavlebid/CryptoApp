package course.apps.cryptoapp.api

import course.apps.cryptoapp.pojo.CoinInfoListOfData
import course.apps.cryptoapp.pojo.CoinPriceInfoRawData
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    companion object{
        private const val QUERY_PARAM_LIMIT = "limit"
        private const val QUERY_PARAM_TO_SYMBOL = "tsym"
        private const val QUERY_PARAM_API_KEY = "api_key"

        private const val QUERY_PARAM_FROM_SYMBOLS = "fsyms"
        private const val QUERY_PARAM_TO_SYMBOLS = "tsyms"

        private const val CURRENCY = "USD"
        private const val API_KEY = "a7f9bdb34169791c32652f66300a6ec531231d6b89f86029e86b3cce8f3afb65"
    }

    @GET("top/totalvolfull")
    fun getTopCoinsInfo(
        @Query(QUERY_PARAM_API_KEY) api_key: String = API_KEY,
        @Query(QUERY_PARAM_LIMIT) limit: Int = 10,
        @Query(QUERY_PARAM_TO_SYMBOL) tsym: String = "USD"
    ): Single<CoinInfoListOfData>


    @GET("pricemultifull")
    fun getFullPriceList(
        @Query(QUERY_PARAM_API_KEY) api_key: String = API_KEY,
        @Query(QUERY_PARAM_TO_SYMBOLS) toSymbols: String = CURRENCY,
        @Query(QUERY_PARAM_FROM_SYMBOLS) fromSymbols: String

    ): Single<CoinPriceInfoRawData>


}