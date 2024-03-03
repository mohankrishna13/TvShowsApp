package com.mohankrishna.tvshowsapp.Repository.RoomRepository

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mohankrishna.tvshowsapp.ModelClass.Result

@Database(entities = [Result::class], version = 1)
abstract class MyRoomDatabase : RoomDatabase() {
    abstract fun myRoomDao(): TvServiceRoomDao
}