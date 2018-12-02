package com.moviedb.nhdphuong.moviedb.viewmodels

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.moviedb.nhdphuong.moviedb.data.entities.FavoriteMovie
import com.moviedb.nhdphuong.moviedb.data.entities.Movie
import com.moviedb.nhdphuong.moviedb.usecases.GetFavoriteMovieCountUseCase
import com.moviedb.nhdphuong.moviedb.usecases.LoadFavoritesMoviesUseCase
import com.moviedb.nhdphuong.moviedb.usecases.UseCase
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

class FavoriteViewModel @Inject constructor(
    private val mLoadFavoritesMovies: LoadFavoritesMoviesUseCase,
    private val mGetFavoriteMovieCount: GetFavoriteMovieCountUseCase
) : ViewModel(),
    UseCase.ExecuteCallback<LoadFavoritesMoviesUseCase.FavoriteMovieResponse, LoadFavoritesMoviesUseCase.FavoriteMovieError> {
    companion object {
        private const val TAG = "MoviesViewModel"
        private const val OFFSET = 20
    }

    @Volatile
    private var mCurrentPosition = 0

    @Volatile
    private var mTotalPage = 0
    private val mMoviesListLiveData = MutableLiveData<List<Movie>>()
    private val mNetworkErrorLiveData = MutableLiveData<String>()

    private val mMoviesList = ArrayList<Movie>()
    val moviesList get() = mMoviesList

    private var mIsInitialLoaded = AtomicBoolean(false)
    val isInitialLoaded: Boolean
        get() = mIsInitialLoaded.get()

    init {
        mLoadFavoritesMovies.setExecuteCallback(this)
    }

    /**
     * Fetching movies by page number callback
     */
    override fun onComplete(responseValue: LoadFavoritesMoviesUseCase.FavoriteMovieResponse) {
        Log.d(TAG, "LoadFavoritesMoviesUseCase onComplete favoriteList=${responseValue.favoriteMovies.size}")
        mCurrentPosition += if (responseValue.favoriteMovies.size >= OFFSET) OFFSET else responseValue.favoriteMovies.size
        mMoviesListLiveData.postValue(generateMoviesList(responseValue.favoriteMovies))
    }

    override fun onError(errorValue: LoadFavoritesMoviesUseCase.FavoriteMovieError) {
        Log.d(TAG, "LoadFavoritesMoviesUseCase onError message=${errorValue.message}")
        mNetworkErrorLiveData.postValue(errorValue.message)
    }

    fun downloadInitialMovesList() {
        Log.d(TAG, "downloadInitialMovesList isInitialLoaded=$isInitialLoaded")
        if (!isInitialLoaded) {
            mIsInitialLoaded.set(true)
            mCurrentPosition = 0
            mGetFavoriteMovieCount.setExecuteCallback(object :
                UseCase.ExecuteCallback<GetFavoriteMovieCountUseCase.FavoriteMovieResponse, GetFavoriteMovieCountUseCase.FavoriteMovieError> {
                override fun onComplete(responseValue: GetFavoriteMovieCountUseCase.FavoriteMovieResponse) {
                    mTotalPage = responseValue.favoriteCount / OFFSET
                    if (responseValue.favoriteCount % OFFSET > 0) {
                        mTotalPage++
                    }
                    if (mTotalPage > 0) {
                        mLoadFavoritesMovies.execute(
                            LoadFavoritesMoviesUseCase.FavoriteMovieRequest(
                                mCurrentPosition,
                                OFFSET
                            )
                        )
                    }
                }

                override fun onError(errorValue: GetFavoriteMovieCountUseCase.FavoriteMovieError) {
                    mTotalPage = 0
                }
            })
            mGetFavoriteMovieCount.execute(GetFavoriteMovieCountUseCase.FavoriteMovieRequest())
        }
    }

    fun downloadNextPage() {
        Log.d(TAG, "downloadNextPage current position=$mCurrentPosition")
        mCurrentPosition = 0
        mLoadFavoritesMovies.execute(LoadFavoritesMoviesUseCase.FavoriteMovieRequest(mCurrentPosition, OFFSET))
    }

    fun registerRemoteMoviesListChangedListener(
        lifeCycleOwner: LifecycleOwner,
        onMoviesListChanged: (List<Movie>, Int) -> Unit
    ) {
        mMoviesListLiveData.observe(lifeCycleOwner, Observer { listMovies ->
            Log.d(TAG, "Movies list changed, new list=$listMovies")
            listMovies?.run {
                onMoviesListChanged(this, (mCurrentPosition / OFFSET) + 1)
            }
        })
    }

    fun registerNetworkErrorListener(
        lifeCycleOwner: LifecycleOwner,
        onNetworkFailed: (String) -> Unit
    ) {
        mNetworkErrorLiveData.observe(lifeCycleOwner, Observer { message ->
            Log.d(TAG, "Network error message=$message")
            onNetworkFailed(message ?: "Unknown error")
        })
    }

    fun unRegisterRemoteMoviesListChangedListener(lifeCycleOwner: LifecycleOwner) {
        mMoviesListLiveData.removeObservers(lifeCycleOwner)
    }

    fun unRegisterNetworkErrorListener(lifeCycleOwner: LifecycleOwner) {
        mNetworkErrorLiveData.removeObservers(lifeCycleOwner)
    }

    private fun generateMoviesList(favoriteMoviesList: List<FavoriteMovie>): List<Movie> {
        val result = ArrayList<Movie>()
        for (favoriteMovie in favoriteMoviesList) {
            result.add(favoriteMovie.movie)
        }
        return result
    }
}