package com.mohankrishna.tvshowsapp.Repository.RetrofitRepository

import com.mohankrishna.tvshowsapp.ModelClass.TvShowsFromApiModel
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TvShowsApiInterface {

    @GET("trending/tv/week?")
    fun getTrendingShowsPerWeek(@Query("api_key") api_key : String): Call<TvShowsFromApiModel>
    @GET("trending/tv/day?")
    fun getTrendingShowsPerDay(@Query("api_key") api_key : String): Call<TvShowsFromApiModel>

    @GET("search/tv?")
    fun getTrendingShowsByName( @Query("api_key") api_key : String,@Query("query") name:String): Call<TvShowsFromApiModel>
    @GET("tv/{series_id}/similar")
    suspend fun getSimilarShows(
        @Path("series_id") series_id : Int,
        @Query("api_key") api_key : String,
        @Query("page") page : Int): Response<TvShowsFromApiModel>
}