package com.example.cryptocurrencypricetrackerapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptocurrencypricetrackerapp.R
import com.example.cryptocurrencypricetrackerapp.dto.CoinListItem
import com.example.cryptocurrencypricetrackerapp.util.placeholderProgressBar
import com.example.cryptocurrencypricetrackerapp.view.CoinListFragment
import com.example.cryptocurrencypricetrackerapp.view.CoinListFragmentDirections
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.coin_list_item.view.*

class CoinListAdapter(val coinList: ArrayList<CoinListItem>):
    RecyclerView.Adapter<CoinListAdapter.MenuViewHolder>(), Filterable {

    class MenuViewHolder(var view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.coin_list_item, parent, false)
        return MenuViewHolder(view)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.view.coin_name.text = coinList[position].coinName
        Picasso.get().load(coinList[position].coinImage).placeholder(placeholderProgressBar(holder.view.context)).into(holder.view.coin_image)
        holder.view.setOnClickListener {
            val action = CoinListFragmentDirections.actionCoinListFragmentToCoinDetailFragment(coinList[position].uuid)
            Navigation.findNavController(it).navigate(action)
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

    override fun getFilter(): Filter {
        return customFilter
    }

    private val customFilter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filteredList = mutableListOf<CoinListItem>()
            val string = constraint?.toString() ?: ""
            if (string.isEmpty()) {
                filteredList.addAll(coinList)
            } else {
                filteredList.filter {
                    it.coinName!!.lowercase().contains(string.lowercase()) || it.symbol!!.lowercase().contains(string.lowercase())
                }.forEach{
                    filteredList.add(it)
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence?, filterResults: FilterResults?) {
            updateCoinList(filterResults?.values as List<CoinListItem>)
        }

    }
}



