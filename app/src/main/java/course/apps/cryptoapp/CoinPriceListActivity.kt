package course.apps.cryptoapp

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import course.apps.cryptoapp.adapters.CoinInfoAdapter
import course.apps.cryptoapp.pojo.CoinPriceInfo
import io.reactivex.disposables.CompositeDisposable

class CoinPriceListActivity : AppCompatActivity() {
    private val compositeDisposable = CompositeDisposable()

    //private lateinit var viewModel : CoinViewModel
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coin_price_list)
        recyclerView = findViewById(R.id.recyclerViewCoinPriceList)

        val adapter = CoinInfoAdapter(this)
        adapter.onCoinClickListener = object : CoinInfoAdapter.OnCoinClickListener {
            override fun onCoinClick(coinPriceInfo: CoinPriceInfo) {
                Log.d("TEST_ON_COIN_CLICK", coinPriceInfo.fromSymbol)
                val intent = CoinDetailActivity.newIntent(
                    coinPriceInfo.fromSymbol,
                    this@CoinPriceListActivity
                )
                startActivity(intent)
            }
        }


        recyclerView.adapter = adapter


        val viewModel by viewModels<CoinViewModel>()
        //viewModel.loadData()

        viewModel.priceList.observe(this, {
            adapter.coinInfoList = it
        })


    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }
}