package com.mohankrishna.tvshowsapp.ui.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.mohankrishna.tvshowsapp.Adapters.HomeScreenAdapter
import com.mohankrishna.tvshowsapp.R
import com.mohankrishna.tvshowsapp.databinding.FragmentTredingTvShowsBinding
import com.mohankrishna.tvshowsapp.utils.DataFetchResults
import com.mohankrishna.tvshowsapp.utils.InternetModeProvider
import com.mohankrishna.tvshowsapp.viewModels.HomeScreenViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class TrendingShowsFragment : Fragment() {
    val  myViewModel: HomeScreenViewModel by viewModel()
    lateinit var fragBinding: FragmentTredingTvShowsBinding
    lateinit var recycleViewListAdapter: HomeScreenAdapter
    val internetModeProvider:InternetModeProvider by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_treding_tv_shows, container, false)
        return fragBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycleViewListAdapter= HomeScreenAdapter(arrayListOf())
        fragBinding.recyclerView.layoutManager =GridLayoutManager(requireContext(),2)
        fragBinding.recyclerView.adapter=recycleViewListAdapter


        createObserverForTrendingData()

        createObserverForTrendingDataOffline()

        if(internetModeProvider.isNetworkConnected){
            myViewModel.onlineTvShowTrendingData()
        }else {
            myViewModel.offlineTvShowData()
        }

        myViewModel.getDataForLocal.observe(requireActivity(),Observer{
            myViewModel.insertDataToOffline(it)
        })

    }

    private fun createObserverForTrendingDataOffline() {
        myViewModel.getDataInLocal.observe(requireActivity(), Observer { result ->
            when (result) {
                is DataFetchResults.Loading -> {
                    fragBinding.progressbarLayout.visibility=View.VISIBLE
                }
                is DataFetchResults.Success -> {
                    recycleViewListAdapter.updateProductsList(result.data)
                    fragBinding.progressbarLayout.visibility=View.GONE
                }
                is DataFetchResults.Failure -> {
                    fragBinding.progressbarLayout.visibility=View.GONE
                }
                else -> {
                    fragBinding.progressbarLayout.visibility=View.GONE
                }
            }
        })
    }

    private fun createObserverForTrendingData() {
        myViewModel.getAllTrendingData.observe(requireActivity(), Observer { result ->
            when (result) {
                is DataFetchResults.Loading -> {
                    fragBinding.progressbarLayout.visibility=View.VISIBLE
                }
                is DataFetchResults.Success -> {
                    recycleViewListAdapter.updateProductsList(result.data)
                    fragBinding.progressbarLayout.visibility=View.GONE
                }
                is DataFetchResults.Failure -> {
                    fragBinding.progressbarLayout.visibility=View.GONE
                }
                else -> {
                    fragBinding.progressbarLayout.visibility=View.GONE
                }
            }
        })

    }
}