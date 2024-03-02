package com.mohankrishna.tvshowsapp.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohankrishna.tvshowsapp.BuildConfig
import com.mohankrishna.tvshowsapp.ModelClass.Result
import com.mohankrishna.tvshowsapp.ModelClass.TvShowsFromApiModel
import com.mohankrishna.tvshowsapp.Repository.RetrofitRepository.TvShowsApiInterface
import com.mohankrishna.tvshowsapp.Repository.RoomRepository.RoomDatabaseHelper
import com.mohankrishna.tvshowsapp.utils.DataFetchResults
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeScreenViewModel(private var apiRepository: TvShowsApiInterface,
                          private var databaseHelper: RoomDatabaseHelper):ViewModel(){
    val getAllTrendingData = MutableLiveData<DataFetchResults>()
    val getDataByName = MutableLiveData<DataFetchResults>()
    val getDataForWeek = MutableLiveData<DataFetchResults>()
    val getDataInLocal = MutableLiveData<DataFetchResults>()
    val getDataForLocal = MutableLiveData<List<Result>>()

    val searchKey=MutableLiveData<String>()
    fun onlineTvShowTrendingData() {
        CoroutineScope(Dispatchers.IO).launch {
             try {
                 supervisorScope {
                     CoroutineScope(Dispatchers.Main).launch {
                         getAllTrendingData.value = DataFetchResults.Loading()
                     }
                    apiRepository.getTrendingShowsPerDay(BuildConfig.API_KEY)
                         .enqueue(object : Callback<TvShowsFromApiModel> {
                             override fun onResponse(
                                 call: Call<TvShowsFromApiModel>,
                                 response: Response<TvShowsFromApiModel> ){
                                 if (response.isSuccessful) {
                                    CoroutineScope(Dispatchers.Main).launch {
                                        getAllTrendingData.value=DataFetchResults.Success(response.body()!!.results)
                                        getDataForLocal.value=response.body()!!.results
                                    }
                                     //insertDataToOffline(response.body()!!.results)
                                 } else {
                                     getAllTrendingData.value=DataFetchResults.Error("Getting Error from server")
                                 }
                             }

                             override fun onFailure(call: Call<TvShowsFromApiModel>, t: Throwable) {
                                 getAllTrendingData.value=DataFetchResults.Failure(t)
                             }
                         })
                 }
             } catch (e: Exception) {
                 DataFetchResults.Failure(e)
             }
         }
    }
    fun onlineTvShowDataByName(nameOfShow: String) {
         viewModelScope.launch {
            try {
                supervisorScope {
                    CoroutineScope(Dispatchers.Main).launch {
                        getDataByName.value = DataFetchResults.Loading()
                    }
                    apiRepository.getTrendingShowsByName(BuildConfig.API_KEY,nameOfShow)
                        .enqueue(object : Callback<TvShowsFromApiModel> {
                            override fun onResponse(
                                call: Call<TvShowsFromApiModel>,
                                response: Response<TvShowsFromApiModel> ){
                                if (response.isSuccessful) {
                                    response.body()?.let {
                                        CoroutineScope(Dispatchers.Main).launch {
                                            getDataByName.value=DataFetchResults.Success(it.results)
                                        }
                                    }
                                } else {
                                    getDataByName.value=DataFetchResults.Error("Getting Error from server")
                                }
                            }

                            override fun onFailure(call: Call<TvShowsFromApiModel>, t: Throwable) {
                                getDataByName.value=DataFetchResults.Failure(t)
                            }
                        })
                }
            } catch (e: Exception) {
                getDataByName.value=DataFetchResults.Failure(e)
            }
        }

    }
    fun onlineTvShowDataForWeek() {
        viewModelScope.launch {
            try {
                supervisorScope {
                    CoroutineScope(Dispatchers.Main).launch {
                        getDataForWeek.value = DataFetchResults.Loading()
                    }
                    apiRepository.getTrendingShowsPerWeek(BuildConfig.API_KEY)
                        .enqueue(object : Callback<TvShowsFromApiModel> {
                            override fun onResponse(
                                call: Call<TvShowsFromApiModel>,
                                response: Response<TvShowsFromApiModel> ){
                                if (response.isSuccessful) {
                                    CoroutineScope(Dispatchers.Main).launch{
                                        getDataForWeek.value=DataFetchResults.Success(response.body()!!.results)
                                        getDataForLocal.value=response.body()!!.results
                                    }
                                } else {
                                    getDataForWeek.value=DataFetchResults.Error("Getting Error from server")
                                }
                            }

                            override fun onFailure(call: Call<TvShowsFromApiModel>, t: Throwable) {
                                getDataForWeek.value=DataFetchResults.Failure(t)
                            }
                        })
                }
            } catch (e: Exception) {
                getDataForWeek.value=DataFetchResults.Failure(e)
            }
        }

    }


    fun offlineTvShowData() {
        viewModelScope.launch {
            databaseHelper.getOfflineTvShowsData()
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    CoroutineScope(Dispatchers.Main).launch{
                        getDataInLocal.value=DataFetchResults.Failure(e)
                    }
                }
                .collect {
                    CoroutineScope(Dispatchers.Main).launch{
                        getDataInLocal.value=DataFetchResults.Success(it)
                    }
                }
        }
    }
     fun insertDataToOffline(results: List<Result>) {

         viewModelScope.launch(Dispatchers.IO) {
             try {
                 for(item in results) {
                     databaseHelper.storeOfflineTvShowData(item)
                 }
             } catch (e: Exception) {
                 e.printStackTrace()
             }
         }
    }
    fun offlineTvShowDataByName(it: String?) {
        viewModelScope.launch {
            databaseHelper.getOfflineTvShowDataByName(it)
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    CoroutineScope(Dispatchers.Main).launch{
                        getDataInLocal.value=DataFetchResults.Failure(e)
                    }
                }
                .collect {
                    CoroutineScope(Dispatchers.Main).launch {
                        getDataInLocal.value=DataFetchResults.Success(it)
                    }
                }
        }
    }
}