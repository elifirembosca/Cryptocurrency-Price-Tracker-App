package com.example.cryptocurrencypricetrackerapp.view

import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.cryptocurrencypricetrackerapp.R
import com.example.cryptocurrencypricetrackerapp.adapter.CoinListAdapter
import com.example.cryptocurrencypricetrackerapp.viewmodel.CoinListViewModel
import kotlinx.android.synthetic.main.fragment_coin_list.*


class CoinListFragment : Fragment(), SearchView.OnQueryTextListener {

    private lateinit var viewModel: CoinListViewModel
    private val coinListAdapter = CoinListAdapter(arrayListOf())

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
        coinListRv.setHasFixedSize(true)
        coinListRv.apply {
            layoutManager = GridLayoutManager(context,3)
        }
        coinListRv.adapter=coinListAdapter

        swipeRefresh.setOnRefreshListener {
            coinListRv.visibility = View.GONE
            loading_image.visibility = View.VISIBLE
            error_image.visibility = View.GONE
            viewModel.getDataFromAPI()
            swipeRefresh.isRefreshing = false
        }
        observeLiveData()
    }

    private fun observeLiveData(){
        viewModel.coinList.observe(viewLifecycleOwner, Observer { list ->
            list?.let {
                coinListRv.visibility = View.VISIBLE
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

    override fun onQueryTextSubmit(p0: String?): Boolean {
        TODO("Not yet implemented")
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        coinListAdapter.filter.filter(p0)
        return true
    }


}