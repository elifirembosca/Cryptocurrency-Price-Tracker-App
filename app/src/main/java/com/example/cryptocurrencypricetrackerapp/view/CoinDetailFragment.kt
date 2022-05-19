package com.example.cryptocurrencypricetrackerapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.cryptocurrencypricetrackerapp.databinding.FragmentCoinDetailBinding
import com.example.cryptocurrencypricetrackerapp.viewmodel.CoinDetailViewModel
import com.like.LikeButton
import com.like.OnLikeListener
import com.squareup.picasso.Picasso


class CoinDetailFragment : Fragment() {

    private lateinit var viewModel: CoinDetailViewModel
    private lateinit var binding: FragmentCoinDetailBinding
    private var documentId: String = ""
    private var coinName: String = ""
    private var favCoin: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCoinDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            coinName = CoinDetailFragmentArgs.fromBundle(it).coinName
            favCoin = CoinDetailFragmentArgs.fromBundle(it).favCoinFlag
            documentId = CoinDetailFragmentArgs.fromBundle(it).documentId
        }
        viewModel = ViewModelProviders.of(this).get(CoinDetailViewModel::class.java)
        viewModel.getDataFromAPI(coinName)
        observeLiveData()
    }

    private fun observeLiveData() {
        viewModel.coinDetail.observe(viewLifecycleOwner, Observer { coin ->
            coin?.let {
                binding.run {
                    container.visibility = View.VISIBLE
                    Picasso.get().load(coin.image.large).into(detailImage)
                    pricePercentageChange.text = "% ${coin.market_data.price_change_percentage_24h}"
                    pricePercentageChangeTitle.text = "PRICE CHANGE PERCENTAGE (24H)"
                    coinDetailPrice.text = "$ ${coin.market_data.current_price.usd}"
                    coinDetailPriceTitle.text = "CURRENT PRICE"
                    coinDetailName.text = coin.name
                    coinDetailDescp.text = HtmlCompat.fromHtml(coin.description.en, HtmlCompat.FROM_HTML_MODE_LEGACY)
                    coin.hashing_algorithm?.let {
                        coinDetailHashingAlgorithm.text = it
                        coinDetailHashingAlgorithm.visibility = View.VISIBLE
                    }
                    if(favCoin){
                        starButton.isLiked = true
                    }
                    starButton.setOnLikeListener(object : OnLikeListener {
                        override fun liked(likeButton: LikeButton) {
                            viewModel.uploadData(coin.id)
                        }
                        override fun unLiked(likeButton: LikeButton) {
                            viewModel.deleteData(documentId)
                        }
                    })
                }
            }
        })
        viewModel.coinDetailError.observe(viewLifecycleOwner, Observer { error ->
            error?.let {
                if (it) {
                    binding.errorImage.visibility = View.VISIBLE
                } else {
                    binding.errorImage.visibility = View.GONE
                }
            }
        })
        viewModel.coinDetailLoading.observe(viewLifecycleOwner, Observer { loading ->
            loading?.let {
                if (it) {
                    binding.loadingImage.visibility = View.VISIBLE
                } else {
                    binding.loadingImage.visibility = View.GONE
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
    }

}