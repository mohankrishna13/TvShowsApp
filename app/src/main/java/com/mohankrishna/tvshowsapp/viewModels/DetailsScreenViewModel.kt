package com.mohankrishna.tvshowsapp.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.mohankrishna.tvshowsapp.ModelClass.Result
import com.mohankrishna.tvshowsapp.MyApplication
import com.mohankrishna.tvshowsapp.Repository.PaginationRepository.PaginationDataSource
import com.mohankrishna.tvshowsapp.Repository.RetrofitRepository.TvShowsApiInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.launch

class DetailsScreenViewModel(var apiRepository: TvShowsApiInterface,
                             ):ViewModel() {
    var searchId=MutableLiveData<String>()
    fun setData(name:String){
        searchId.value=name
    }
    fun setFavouriteFlag(result: Result) {
        CoroutineScope(Dispatchers.IO).launch{
            MyApplication.mydatabase.myRoomDao().updateOfflineTvShowData(result)
        }
    }

    fun insertData(data: Result) {
        CoroutineScope(Dispatchers.IO).launch{
            MyApplication.mydatabase.myRoomDao().insertTvShowsData(data)
        }
    }

    val moviesList = Pager(PagingConfig(1)) {
        PaginationDataSource(apiRepository,searchId.value!!.toInt())
    }.flow
}