package com.mohankrishna.tvshowsapp.utils

import com.mohankrishna.tvshowsapp.ModelClass.Result

sealed class DataFetchResultsOffline  {
    class Loading() : DataFetchResultsOffline()
    data class Success(val data: List<Result>) : DataFetchResultsOffline()
    data class Failure(val error: Throwable) : DataFetchResultsOffline()
    data class Error(val error: String) : DataFetchResultsOffline()
}