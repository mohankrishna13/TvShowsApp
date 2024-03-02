package com.mohankrishna.tvshowsapp.Repository.RoomRepository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mohankrishna.tvshowsapp.ModelClass.Result

@Database(entities = [Result::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun tvShowDataDao(): TvServiceRoomDao
}

object DatabaseClient {
    private lateinit var instance: AppDatabase
    fun initialize(context: Context):AppDatabase {
        instance = Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "MyDatabase"
        ).build()
        return instance
    }
}