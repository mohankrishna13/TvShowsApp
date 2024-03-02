package com.mohankrishna.tvshowsapp.Repository.RoomRepository

import androidx.annotation.WorkerThread
import com.mohankrishna.tvshowsapp.ModelClass.Result
import kotlinx.coroutines.flow.Flow

interface RoomDatabaseHelper {
    fun getOfflineTvShowsData(): Flow<MutableList<Result>>
    fun storeOfflineTvShowData(users: Result): Flow<Unit>
    fun updateOfflineTvShowData(result: Result): Flow<Unit>
    fun getOfflineTvShowDataByName(it: String?): Flow<List<Result>>
}
