package com.mohankrishna.tvshowsapp.Repository.RoomRepository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mohankrishna.tvshowsapp.ModelClass.Result
import kotlinx.coroutines.flow.Flow

@Dao
interface TvServiceRoomDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovieList(tvShowData: Result)

    @Query("SELECT * FROM my_service_table")
    fun getTvShows(): MutableList<Result>

    @Update
    fun updateOfflineTvShowData(result: Result)

    @Query("SELECT * FROM my_service_table WHERE name LIKE '%' || :it || '%'")
    fun getDataByName(it: String?):List<Result>

}