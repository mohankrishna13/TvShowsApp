package com.mohankrishna.tvshowsapp.ModelClass
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable
@Entity(tableName = "my_service_table")
data class Result(
    @PrimaryKey
    @ColumnInfo(name="name")
    val name: String,
    @ColumnInfo(name="id")
    val id: Int,
    @ColumnInfo(name="adult")
    val adult: Boolean?,
    @ColumnInfo(name="backdrop_path")
    val backdrop_path: String?,
    @ColumnInfo(name="first_air_date")
    val first_air_date: String?,
    @ColumnInfo(name="media_type")
    val media_type: String?,
    @ColumnInfo(name="original_language")
    val original_language: String?,
    @ColumnInfo(name="original_name")
    val original_name: String?,
    @ColumnInfo(name="overview")
    val overview: String?,
    @ColumnInfo(name="popularity")
    val popularity: Double?,
    @ColumnInfo(name="poster_path")
    val poster_path: String?,
    @ColumnInfo(name="vote_average")
    val vote_average: Double?,
    @ColumnInfo(name="vote_count")
    val vote_count: Int?,
    @ColumnInfo(name="is_favourite")
    var is_favourite: Boolean?
):Serializable