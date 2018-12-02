package com.moviedb.nhdphuong.moviedb.data.remote

import android.support.annotation.WorkerThread
import android.util.Log
import com.moviedb.nhdphuong.moviedb.data.MovieDataSource
import com.moviedb.nhdphuong.moviedb.data.entities.RemoteMovieData
import com.moviedb.nhdphuong.moviedb.network.api.MoviesApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection

class MovieRemoteDataSource(
    private val mMoviesApiService: MoviesApiService,
    private val mApiKey: String
) : MovieDataSource.Remote {
    companion object {
        private const val TAG = "MovieRemoteDataSource"
    }

    @WorkerThread
    override fun fetchRemoteMoviesList(
        pageNumber: Int,
        onComplete: (RemoteMovieData?) -> Unit,
        onError: (String, Int) -> Unit
    ) {
        mMoviesApiService.getRemoteMovieData(mApiKey, pageNumber).enqueue(object : Callback<RemoteMovieData> {
            override fun onResponse(call: Call<RemoteMovieData>, response: Response<RemoteMovieData>) {
                Log.d(
                    TAG,
                    "Fetch remote movies list successfully, code=${response.code()}, message=${response.message()}"
                )
                when (response.code()) {
                    HttpURLConnection.HTTP_OK -> {
                        onComplete(response.body())
                    }
                    else -> {
                        onError(response.message(), response.code())
                    }
                }
            }

            override fun onFailure(call: Call<RemoteMovieData>, t: Throwable) {
                Log.d(TAG, "Fetching remote movies list failed, exception=${t.message}")
                onError(t.message ?: "", -1)
            }
        })
    }
}