package com.example.cryptocurrencypricetrackerapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptocurrencypricetrackerapp.R
import com.example.cryptocurrencypricetrackerapp.dto.CoinListItem
import com.example.cryptocurrencypricetrackerapp.view.CoinListFragmentDirections
import com.example.cryptocurrencypricetrackerapp.viewmodel.CoinDetailViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.coin_list_item.view.*
import kotlin.collections.ArrayList

class CoinListAdapter(var coinList: ArrayList<CoinListItem>):
    RecyclerView.Adapter<CoinListAdapter.MenuViewHolder>() {

    class MenuViewHolder(var view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.coin_list_item, parent, false)
        return MenuViewHolder(view)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.view.coin_name.text = coinList[position].coinName
        Picasso.get().load(coinList[position].coinImage).into(holder.view.coin_image)
        holder.view.setOnClickListener {
            getFavCoin(coinList[position].coinId!!,it,position)
        }
    }

    override fun getItemCount(): Int {
        return coinList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateCoinList(newCoinList: List<CoinListItem>) {
        coinList.clear()
        coinList.addAll(newCoinList)
        notifyDataSetChanged()
    }

    private fun getFavCoin(coinName : String, view : View, position : Int ) {
        var flag = false
        var documentId = ""
        val docRef = CoinDetailViewModel.FirebaseUtils().db.collection("coins")
        docRef.get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    if(document.data.containsValue(coinName)){
                        flag = true
                       documentId = document.id
                        break
                    }
                }
                val action = CoinListFragmentDirections.actionCoinListFragmentToCoinDetailFragment(coinList[position].coinId!!,flag,documentId)
                Navigation.findNavController(view).navigate(action)
            }
    }

}



