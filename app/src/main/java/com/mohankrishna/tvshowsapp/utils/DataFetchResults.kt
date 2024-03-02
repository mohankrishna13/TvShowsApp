package com.mohankrishna.tvshowsapp.utils

import com.mohankrishna.tvshowsapp.ModelClass.Result

sealed class DataFetchResults  {
    class Loading() : DataFetchResults()
    data class Success(val data: List<Result>) : DataFetchResults()
    data class Failure(val error: Throwable) : DataFetchResults()
    data class Error(val error: String) : DataFetchResults()
}