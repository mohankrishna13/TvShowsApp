package com.mohankrishna.tvshowsapp.Repository.PaginationRepository

import android.net.http.HttpException
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresExtension
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mohankrishna.tvshowsapp.BuildConfig
import com.mohankrishna.tvshowsapp.ModelClass.Result
import com.mohankrishna.tvshowsapp.ModelClass.TvShowsFromApiModel
import com.mohankrishna.tvshowsapp.Repository.RetrofitRepository.TvShowsApiInterface
import com.mohankrishna.tvshowsapp.Repository.RoomRepository.RoomDatabaseHelper
import com.mohankrishna.tvshowsapp.utils.DataFetchResults
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PaginationDataSource(private var apiRepository:TvShowsApiInterface,var id:Int): PagingSource<Int, Result>() {
    override fun getRefreshKey(state: PagingState<Int, Result>): Int? {
        return null
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Result> {
        return try {
            val currentPage = params.key ?: 1
            var dataCall=apiRepository.
            getSimilarShows(id,BuildConfig.API_KEY,currentPage)
            var responseData= mutableListOf<Result>()
            responseData.addAll(dataCall.body()?.results?: emptyList())
            LoadResult.Page(
                data = responseData,
                prevKey = if (currentPage == 1) null else -1,
                nextKey = currentPage.plus(1)
            )
        }catch (e:Exception){
            LoadResult.Error(e)

        }catch (e:HttpException){
            LoadResult.Error(e)
        }
    }
}