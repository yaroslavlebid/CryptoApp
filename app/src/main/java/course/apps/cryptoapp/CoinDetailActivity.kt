package course.apps.cryptoapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import course.apps.cryptoapp.databinding.ActivityCoinDetailBinding

class CoinDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCoinDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCoinDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!intent.hasExtra(EXTRA_FROM_SYMBOL)) {
            finish()
            return
        }
        val fromSymbol = intent.getStringExtra(EXTRA_FROM_SYMBOL) ?: return
        val viewModel by viewModels<CoinViewModel>()
        viewModel.getDetailInfo(fromSymbol).observe(this, {
            with(binding)
            {
                Picasso.get().load(it.getFullImgUrl()).into(imageViewLogo)
                textViewFromSymbol.text = it.fromSymbol
                textViewToSymbol.text = it.toSymbol
                textViewPrice.text = it.price.toString()
                textViewMinPrice.text = it.lowDay.toString()
                textViewMaxPrice.text = it.highDay.toString()
                textViewLastMarket.text = it.lastMarket
                textViewLastUpdate.text = it.getFormattedTime()
            }
        })
    }

    companion object {
        private const val EXTRA_FROM_SYMBOL = "fromSymbol"

        fun newIntent(fromSymbol: String, context: Context): Intent {
            val intent = Intent(context, CoinDetailActivity::class.java)
            intent.putExtra(EXTRA_FROM_SYMBOL, fromSymbol)
            return intent
        }
    }
}