package com.mohankrishna.tvshowsapp.ui.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.mohankrishna.tvshowsapp.Adapters.SearchScreenAdapter
import com.mohankrishna.tvshowsapp.R
import com.mohankrishna.tvshowsapp.databinding.FragmentSearchShowBinding
import com.mohankrishna.tvshowsapp.utils.DataFetchResults
import com.mohankrishna.tvshowsapp.utils.InternetModeProvider
import com.mohankrishna.tvshowsapp.viewModels.HomeScreenViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class Search_ShowFragment : Fragment() {
    val myViewModel: HomeScreenViewModel by viewModel()
    lateinit var fragBinding: FragmentSearchShowBinding
    lateinit var recycleViewListAdapter: SearchScreenAdapter
    val internetModeProvider:InternetModeProvider by inject()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_show, container, false)
        return fragBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycleViewListAdapter= SearchScreenAdapter(arrayListOf())
        fragBinding.recyclerView.layoutManager = GridLayoutManager(requireContext(),2)
        fragBinding.recyclerView.adapter=recycleViewListAdapter


        createObserverForDataByName()
        createObserverForWeek()
        createObserverForOffline()

        myViewModel.searchKey.observe(requireActivity(),Observer{
            if(internetModeProvider.isNetworkConnected){
                if(it.isBlank() || it.isEmpty()){
                    myViewModel.onlineTvShowDataForWeek()
                }else{
                    myViewModel.onlineTvShowDataByName(it)
                }
            }else{
                myViewModel.offlineTvShowDataByName(it)
            }
        })
    }

    private fun createObserverForOffline() {
        myViewModel.getLocalDataSearchByName.observe(requireActivity(), Observer { result ->
            when (result) {
                is DataFetchResults.Loading -> {
                    fragBinding.progressbarLayout.visibility = View.VISIBLE
                }

                is DataFetchResults.Success -> {
                    recycleViewListAdapter.updateProductsList(result.data)
                    fragBinding.progressbarLayout.visibility=View.GONE
                }

                is DataFetchResults.Failure -> {
                    fragBinding.progressbarLayout.visibility = View.GONE
                }

                else -> {
                    fragBinding.progressbarLayout.visibility = View.GONE
                }
            }
        })
    }
    private fun createObserverForDataByName() {
        myViewModel.getDataByNameOnline.observe(requireActivity(), Observer { result ->
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

    private fun createObserverForWeek() {
        myViewModel.getDataForWeek.observe(requireActivity(), Observer { result ->
            when (result) {
                is DataFetchResults.Loading -> {
                    fragBinding.progressbarLayout.visibility=View.VISIBLE
                }
                is DataFetchResults.Success -> {
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