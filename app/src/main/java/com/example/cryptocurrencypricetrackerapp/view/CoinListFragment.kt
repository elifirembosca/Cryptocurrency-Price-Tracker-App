package com.example.cryptocurrencypricetrackerapp.view

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.cryptocurrencypricetrackerapp.MainActivity
import com.example.cryptocurrencypricetrackerapp.R
import com.example.cryptocurrencypricetrackerapp.adapter.CoinListAdapter
import com.example.cryptocurrencypricetrackerapp.dto.CoinListItem
import com.example.cryptocurrencypricetrackerapp.viewmodel.CoinListViewModel
import kotlinx.android.synthetic.main.fragment_coin_list.*


class CoinListFragment : Fragment() {

    private lateinit var viewModel: CoinListViewModel
    private val coinListAdapter = CoinListAdapter(arrayListOf())
    private var coinList: ArrayList<CoinListItem> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_coin_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(CoinListViewModel::class.java)
        viewModel.refreshData()
        coin_list_rv.setHasFixedSize(true)
        coin_list_rv.apply {
            layoutManager = GridLayoutManager(context,3)
        }
        coin_list_rv.adapter=coinListAdapter

        swipe_refresh.setOnRefreshListener {
            coin_list_rv.visibility = View.GONE
            loading_image.visibility = View.VISIBLE
            error_image.visibility = View.GONE
            viewModel.getDataFromAPI()
            swipe_refresh.isRefreshing = false
        }
        observeLiveData()
    }

    private fun observeLiveData() {
        viewModel.coinList.observe(viewLifecycleOwner, Observer { list ->
            list?.let {
                coinList = list as ArrayList<CoinListItem>
                coin_list_rv.visibility = View.VISIBLE
                coinListAdapter.updateCoinList(list)
            }

        })
        viewModel.coinListError.observe(viewLifecycleOwner, Observer { error ->
            error?.let {
                if (it) {
                    error_image.visibility = View.VISIBLE
                } else {
                    error_image.visibility = View.GONE
                }
            }
        })
        viewModel.coinListLoading.observe(viewLifecycleOwner, Observer { loading ->
            loading?.let {
                if (it) {
                    loading_image.visibility = View.VISIBLE
                } else {
                    loading_image.visibility = View.GONE
                }
            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.search_menu, menu)
        val item = menu.findItem(R.id.action_search)
        val searchView = SearchView(
            (activity as MainActivity).supportActionBar!!.themedContext
        )
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW or MenuItem.SHOW_AS_ACTION_IF_ROOM)
        item.actionView = searchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                var newList: ArrayList<CoinListItem> = ArrayList()
                for (row in coinList) {
                    if (row.coinName!!.lowercase()
                            .contains(newText.lowercase()) || row.symbol!!.lowercase()
                            .contains(newText.lowercase())
                    ) {
                        newList.add(row)
                    } else if(newText.isBlank()){
                        newList = coinList
                    }
                }
                coinListAdapter.updateCoinList(newList)
                return true
            }
        })
    }

}
