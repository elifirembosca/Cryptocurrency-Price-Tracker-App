package com.example.cryptocurrencypricetrackerapp.view

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.cryptocurrencypricetrackerapp.databinding.FragmentCoinDetailBinding
import com.example.cryptocurrencypricetrackerapp.viewmodel.CoinDetailViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso


class CoinDetailFragment : Fragment() {


    private lateinit var viewModel: CoinDetailViewModel
    private var coinUuid = 0
    private lateinit var binding: FragmentCoinDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCoinDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            coinUuid = CoinDetailFragmentArgs.fromBundle(it).countryUuid
        }

        viewModel = ViewModelProviders.of(this).get(CoinDetailViewModel::class.java)
        viewModel.getDataFromRoom(coinUuid)

        observeLiveData()
        uploadData()
    }

    private fun observeLiveData() {
        viewModel.countryLiveData.observe(viewLifecycleOwner, Observer { coin ->
            coin?.let {
                binding.run {
                    Picasso.get().load(coin.coinImage).into(detailImage)
                    pricePercentageChange.text = coin.market_cap_change_percentage_24h.toString()
                    pricePercentageChangeTitle.text = "PRICE CHANGE (24H)"
                    coinDetailPrice.text = coin.current_price.toString()
                    coinDetailPriceTitle.text = "CURRENT PRICE"
                    coinDetailName.text = coin.coinName
                }
            }
        })
    }

    class FirebaseUtils {
        val fireStoreDatabase = FirebaseFirestore.getInstance()
    }

    private fun uploadData() {
        binding!!.addFavoriteButton.setOnClickListener {

            // create a dummy data
            val hashMap = hashMapOf<String, Any>(
                "name" to "John doe",
                "city" to "Nairobi",
                "age" to 24
            )

            // use the add() method to create a document inside users collection
            FirebaseUtils().fireStoreDatabase.collection("users")
                .add(hashMap)
                .addOnSuccessListener {
                    Log.d(TAG, "Added document with ID ${it.id}")
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error adding document $exception")
                }
        }
    }
}