package com.mohankrishna.tvshowsapp.ui.Activities

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.mohankrishna.tvshowsapp.Adapters.PaginationAdapter
import com.mohankrishna.tvshowsapp.ModelClass.Result
import com.mohankrishna.tvshowsapp.R
import com.mohankrishna.tvshowsapp.Repository.RoomRepository.RoomDatabaseHelper
import com.mohankrishna.tvshowsapp.databinding.ActivityDetailsBinding
import com.mohankrishna.tvshowsapp.utils.InternetModeProvider
import com.mohankrishna.tvshowsapp.viewModels.DetailsScreenViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class DetailsScreenActivity : AppCompatActivity() {
    val  myViewModel:DetailsScreenViewModel by viewModel()
    lateinit var recycleViewListAdapter: PaginationAdapter
    lateinit var binding: ActivityDetailsBinding

    val roomDatabaseHelper:RoomDatabaseHelper by inject()
    val internetModeProvider:InternetModeProvider by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_details)

        val intent = intent
        val data: Result? = intent.getSerializableExtra("selectedData") as Result?
        myViewModel.setData(data?.id.toString())

        binding.showDescription.text=data?.overview

        recycleViewListAdapter= PaginationAdapter()
        binding.similarRecyclerView.layoutManager = LinearLayoutManager(
            this@DetailsScreenActivity,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        binding.similarRecyclerView.adapter=recycleViewListAdapter

        if(data?.isFavourite==null){
            binding.notFavourite.visibility=View.VISIBLE
            binding.favourite.visibility=View.GONE
        }else{
            binding.favourite.visibility=View.VISIBLE
            binding.notFavourite.visibility=View.GONE
        }
        binding.notFavourite.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                binding.notFavourite.visibility=View.GONE
                binding.favourite.visibility=View.VISIBLE

                var singleData=data
                singleData?.isFavourite=true

                if (singleData != null) {
                    setFavouriteDataToLocal(singleData)
                }
            }

        })
        binding.favourite.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                binding.notFavourite.visibility=View.VISIBLE
                binding.favourite.visibility=View.GONE

                var singleData=data
                singleData?.isFavourite=false

                if (singleData != null) {
                    setFavouriteDataToLocal(singleData)
                }

            }
        })

        if(internetModeProvider.isNetworkConnected){
            fetchDataByPagination()
        }
        val imageUrl = "http://image.tmdb.org/t/p/w500${data?.poster_path}"
        Glide.with(this)
            .load(imageUrl)
            .placeholder(R.drawable.loading)
            .error(R.drawable.error)
            .into(binding.imagePoster)


    }


    private fun setFavouriteDataToLocal(daa:Result) {
        CoroutineScope(Dispatchers.Main).launch{
            myViewModel.setFavouriteFlag(daa)
        }
    }

    private fun fetchDataByPagination() {
        CoroutineScope(Dispatchers.IO).launch {
            myViewModel.moviesList.collect {
                recycleViewListAdapter.submitData(it)
            }
        }
    }
}