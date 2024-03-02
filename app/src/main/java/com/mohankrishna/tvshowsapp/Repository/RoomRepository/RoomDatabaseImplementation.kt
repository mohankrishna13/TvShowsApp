package com.mohankrishna.tvshowsapp.Repository.RoomRepository

import androidx.lifecycle.MutableLiveData
import com.mohankrishna.tvshowsapp.ModelClass.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DatabaseHelperImpl(private val appDatabase: AppDatabase) : RoomDatabaseHelper {

    override fun getOfflineTvShowsData(): Flow<MutableList<Result>> =
        flow {
        emit(appDatabase.tvShowDataDao().getTvShows())
    }

    override fun storeOfflineTvShowData(users: Result): Flow<Unit> = flow {
        appDatabase.tvShowDataDao().insertMovieList(users)
        emit(Unit)
    }

    override fun updateOfflineTvShowData(result: Result): Flow<Unit> =flow {
        appDatabase.tvShowDataDao().updateOfflineTvShowData(result)
        emit(Unit)
    }

    override fun getOfflineTvShowDataByName(it: String?): Flow<List<Result>> = flow {
        emit(appDatabase.tvShowDataDao().getDataByName(it))
    }
}
