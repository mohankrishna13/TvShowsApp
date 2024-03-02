package com.mohankrishna.tvshowsapp.viewModels
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.mohankrishna.tvshowsapp.ModelClass.Result
import com.mohankrishna.tvshowsapp.Repository.PaginationRepository.PaginationDataSource
import com.mohankrishna.tvshowsapp.Repository.RetrofitRepository.TvShowsApiInterface
import com.mohankrishna.tvshowsapp.Repository.RoomRepository.RoomDatabaseHelper

import kotlinx.coroutines.launch

class DetailsScreenViewModel(var apiRepository: TvShowsApiInterface,var databaseHelper: RoomDatabaseHelper):ViewModel() {
    var searchId=MutableLiveData<String>()
    fun setData(name:String){
        searchId.value=name
    }
    fun setFavouriteFlag(result: Result) {
         viewModelScope.launch{
            databaseHelper.storeOfflineTvShowData(result)
        }
    }
    val moviesList = Pager(PagingConfig(1)) {
        PaginationDataSource(apiRepository,searchId.value!!.toInt())
    }.flow
}