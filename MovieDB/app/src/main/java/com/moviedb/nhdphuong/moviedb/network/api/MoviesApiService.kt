package com.moviedb.nhdphuong.moviedb.network.api

import com.moviedb.nhdphuong.moviedb.data.entities.RemoteMovieData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesApiService {
    @GET("/3/movie/now_playing")
    fun getRemoteMovieData(@Query("api_key") apiKey: String, @Query("page") pageNumber: Int): Call<RemoteMovieData>
}