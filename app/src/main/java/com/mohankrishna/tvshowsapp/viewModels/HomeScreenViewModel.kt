package com.mohankrishna.tvshowsapp.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohankrishna.tvshowsapp.BuildConfig
import com.mohankrishna.tvshowsapp.ModelClass.Result
import com.mohankrishna.tvshowsapp.ModelClass.TvShowsFromApiModel
import com.mohankrishna.tvshowsapp.MyApplication
import com.mohankrishna.tvshowsapp.Repository.RetrofitRepository.TvShowsApiInterface
import com.mohankrishna.tvshowsapp.utils.DataFetchResults
import com.mohankrishna.tvshowsapp.utils.DataFetchResultsOffline
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeScreenViewModel(private var apiRepository: TvShowsApiInterface):ViewModel(){
    val getAllTrendingData = MutableLiveData<DataFetchResultsOffline>()
    val getDataByNameOnline = MutableLiveData<DataFetchResults>()
    val getDataForWeek = MutableLiveData<DataFetchResults>()
    val getDataInLocal = MutableLiveData<DataFetchResults>()
    val getLocalDataSearchByName=MutableLiveData<DataFetchResults>()
    val getDataForLocal = MutableLiveData<List<Result>>()
    val searchKey=MutableLiveData<String>()
    fun onlineTvShowTrendingData() {
        CoroutineScope(Dispatchers.IO).launch {
             try {
                 supervisorScope {
                     CoroutineScope(Dispatchers.Main).launch {
                         getAllTrendingData.value = DataFetchResultsOffline.Loading()
                     }
                    apiRepository.getTrendingShowsPerDay(BuildConfig.API_KEY)
                         .enqueue(object : Callback<TvShowsFromApiModel> {
                             override fun onResponse(
                                 call: Call<TvShowsFromApiModel>,
                                 response: Response<TvShowsFromApiModel> ){
                                 if (response.isSuccessful) {

                                     response.body()?.let {
                                         insertDataToOffline(it.results)
                                     }
                                 } else {
                                     getAllTrendingData.value=DataFetchResultsOffline.Error("Getting Error from server")
                                 }
                             }

                             override fun onFailure(call: Call<TvShowsFromApiModel>, t: Throwable) {
                                 getAllTrendingData.value=DataFetchResultsOffline.Failure(t)
                             }
                         })
                 }
             } catch (e: Exception) {
                 DataFetchResults.Failure(e)
             }
         }
    }
    fun onlineTvShowDataByName(nameOfShow: String) {
        CoroutineScope(Dispatchers.IO).launch{
            try {
                supervisorScope {
                    CoroutineScope(Dispatchers.Main).launch {
                        getDataByNameOnline.value = DataFetchResults.Loading()
                    }
                    apiRepository.getTrendingShowsByName(BuildConfig.API_KEY,nameOfShow)
                        .enqueue(object : Callback<TvShowsFromApiModel> {
                            override fun onResponse(
                                call: Call<TvShowsFromApiModel>,
                                response: Response<TvShowsFromApiModel> ){
                                if (response.isSuccessful) {
                                        CoroutineScope(Dispatchers.Main).launch {
                                            getDataByNameOnline.value=DataFetchResults.Success(response.body()!!.results)
                                    }
                                } else {

                                    getDataByNameOnline.value=DataFetchResults.Error("Getting Error from server")
                                }
                            }

                            override fun onFailure(call: Call<TvShowsFromApiModel>, t: Throwable) {
                                getDataByNameOnline.value=DataFetchResults.Failure(t)
                            }
                        })
                }
            } catch (e: Exception) {
                getDataByNameOnline.value=DataFetchResults.Failure(e)
            }
        }

    }
    fun onlineTvShowDataForWeek() {
        CoroutineScope(Dispatchers.IO).launch {
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
                                        insertDataToOffline(response.body()!!.results)
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
        CoroutineScope(Dispatchers.IO).launch {
            val userList: List<Result> = MyApplication.mydatabase.myRoomDao().getTvShows()
            CoroutineScope(Dispatchers.Main).launch{
                getDataInLocal.value=DataFetchResults.Success(userList)
                getDataForLocal.value=userList
            }
        }
    }
    fun insertDataToOffline(results: List<Result>) {
          if(!results.isEmpty()){
              CoroutineScope(Dispatchers.IO).launch {
                  for(item in results){
                      val newUser = item
                      MyApplication.mydatabase.myRoomDao().insertTvShowsData(newUser)
                  }
              }
          }
    }
    fun offlineTvShowDataByName(it: String?) {
        CoroutineScope(Dispatchers.IO).launch {
            val userList: List<Result> = MyApplication.mydatabase.myRoomDao().getDataByName(it)
            CoroutineScope(Dispatchers.Main).launch{
                getLocalDataSearchByName.value=DataFetchResults.Success(userList)
            }
        }
    }
}