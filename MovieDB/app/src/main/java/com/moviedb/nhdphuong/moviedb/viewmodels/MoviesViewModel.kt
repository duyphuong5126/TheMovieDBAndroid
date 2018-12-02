package com.moviedb.nhdphuong.moviedb.viewmodels

import android.arch.lifecycle.*
import android.util.Log
import com.moviedb.nhdphuong.moviedb.data.entities.Movie
import com.moviedb.nhdphuong.moviedb.usecases.FetchMoviesUseCase
import com.moviedb.nhdphuong.moviedb.usecases.GetTotalRemotePagesUseCase
import com.moviedb.nhdphuong.moviedb.usecases.UseCase
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

class MoviesViewModel @Inject constructor(
    private val mMoviesUseCase: FetchMoviesUseCase,
    private val mGetTotalRemotePages: GetTotalRemotePagesUseCase
) : ViewModel(),
    UseCase.ExecuteCallback<FetchMoviesUseCase.FetchMoviesResponse, FetchMoviesUseCase.FetchMoviesError> {

    companion object {
        private const val TAG = "MoviesViewModel"
    }

    // Page must be greater than 0
    @Volatile
    private var mCurrentPage = 1
    @Volatile
    private var mTotalPages = 0
    private val mMoviesListLiveData = MutableLiveData<List<Movie>>()
    private val mNetworkErrorLiveData = MutableLiveData<String>()

    private val mMoviesList = ArrayList<Movie>()
    val moviesList get() = mMoviesList

    private var mIsInitialLoaded = AtomicBoolean(false)
    val isInitialLoaded: Boolean
        get() = mIsInitialLoaded.get()

    init {
        mMoviesUseCase.setExecuteCallback(this)
    }

    /**
     * Fetching movies by page number callback
     */
    override fun onComplete(responseValue: FetchMoviesUseCase.FetchMoviesResponse) {
        Log.d(TAG, "FetchMoviesUseCase onComplete listSize=${responseValue.movies.size}")
        mMoviesListLiveData.postValue(responseValue.movies)
    }

    override fun onError(errorValue: FetchMoviesUseCase.FetchMoviesError) {
        Log.d(TAG, "FetchMoviesUseCase onComplete with error=${errorValue.message}, code=${errorValue.code}")
        mNetworkErrorLiveData.postValue(errorValue.message)
    }

    fun downloadInitialMovesList() {
        Log.d(TAG, "downloadInitialMovesList isInitialLoaded=$isInitialLoaded")
        if (!isInitialLoaded) {
            mIsInitialLoaded.set(true)
            mCurrentPage = 1
            mGetTotalRemotePages.setExecuteCallback(object :
                UseCase.ExecuteCallback<GetTotalRemotePagesUseCase.GetRemotePagesResponse, GetTotalRemotePagesUseCase.GetRemotePagesError> {
                override fun onComplete(responseValue: GetTotalRemotePagesUseCase.GetRemotePagesResponse) {
                    Log.d(TAG, "GetTotalRemotePagesUseCase onComplete totalPages=${responseValue.totalPages}")
                    mTotalPages = responseValue.totalPages
                    mMoviesUseCase.execute(FetchMoviesUseCase.FetchMoviesRequestValue(mCurrentPage))
                }

                override fun onError(errorValue: GetTotalRemotePagesUseCase.GetRemotePagesError) {
                    Log.d(TAG, "GetTotalRemotePagesUseCase onComplete with error=${errorValue.message}, code=${errorValue.code}")
                    mTotalPages = 0
                }
            })
            mGetTotalRemotePages.execute(GetTotalRemotePagesUseCase.GetRemotePagesRequestValue())
        }
    }

    fun downloadNextPage() {
        Log.d(TAG, "downloadNextPage current page=$mCurrentPage")
        if (mCurrentPage < mTotalPages) {
            mMoviesUseCase.execute(FetchMoviesUseCase.FetchMoviesRequestValue(++mCurrentPage))
        } else {
            mNetworkErrorLiveData.postValue("Current index reached the total pages, cannot fetch anymore")
        }
    }

    fun registerRemoteMoviesListChangedListener(
        lifeCycleOwner: LifecycleOwner,
        onMoviesListChanged: (List<Movie>, Int) -> Unit
    ) {
        mMoviesListLiveData.observe(lifeCycleOwner, Observer { listMovies ->
            Log.d(TAG, "Movies list changed, new list=$listMovies")
            listMovies?.run {
                onMoviesListChanged(this, mCurrentPage)
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
}