package com.mohankrishna.tvshowsapp.Repository.RoomRepository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mohankrishna.tvshowsapp.ModelClass.Result

@Dao
interface TvServiceRoomDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertTvShowsData(tvShowData: Result)

    @Query("SELECT * FROM my_service_table")
    fun getTvShows(): List<Result>
    
    @Update
    fun updateOfflineTvShowData(result: Result)

    @Query("SELECT * FROM my_service_table WHERE name LIKE '%' || :it || '%'")
    fun getDataByName(it: String?):List<Result>

}