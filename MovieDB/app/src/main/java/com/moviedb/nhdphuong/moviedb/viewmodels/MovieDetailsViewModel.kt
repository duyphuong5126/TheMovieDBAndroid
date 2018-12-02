package com.moviedb.nhdphuong.moviedb.viewmodels

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.moviedb.nhdphuong.moviedb.data.entities.Movie
import com.moviedb.nhdphuong.moviedb.usecases.AddMovieToFavoriteListUseCase
import com.moviedb.nhdphuong.moviedb.usecases.CheckIfFavoriteMovieUseCase
import com.moviedb.nhdphuong.moviedb.usecases.RemoveMovieFromFavoriteListUseCase
import com.moviedb.nhdphuong.moviedb.usecases.UseCase
import java.util.*
import javax.inject.Inject

class MovieDetailsViewModel @Inject constructor(
    private val mAddMovieToFavoriteList: AddMovieToFavoriteListUseCase,
    private val mRemoveMovieFromFavoriteList: RemoveMovieFromFavoriteListUseCase,
    private val mCheckIfFavoriteMovie: CheckIfFavoriteMovieUseCase
) : ViewModel(),
    UseCase.ExecuteCallback<CheckIfFavoriteMovieUseCase.FavoriteMovieResponse, CheckIfFavoriteMovieUseCase.FavoriteMovieError> {
    companion object {
        private const val TAG = "MovieDetailsViewModel"
    }

    private var mMovie: Movie? = null

    init {
        mCheckIfFavoriteMovie.setExecuteCallback(this)
    }

    private val onFavoriteStatusChangedMutableLiveData = MutableLiveData<Boolean>()

    /**
     * Favorite movie checking callback
     */
    override fun onComplete(responseValue: CheckIfFavoriteMovieUseCase.FavoriteMovieResponse) {
        Log.d(TAG, "CheckIfFavoriteMovieUseCase complete favorite status=${responseValue.isFavorite}")
        onFavoriteStatusChangedMutableLiveData.postValue(responseValue.isFavorite)
    }

    override fun onError(errorValue: CheckIfFavoriteMovieUseCase.FavoriteMovieError) {
        Log.d(TAG, "CheckIfFavoriteMovieUseCase complete with error=${errorValue.message}")
    }

    /**
     * Add movie to favorite callback
     */
    private val mAddFavoriteCallback =
        object : UseCase.ExecuteCallback<AddMovieToFavoriteListUseCase.FavoriteMovieResponse,
                AddMovieToFavoriteListUseCase.FavoriteMovieError> {
            override fun onComplete(responseValue: AddMovieToFavoriteListUseCase.FavoriteMovieResponse) {
                Log.d(TAG, "AddMovieToFavoriteListUseCase complete")
                checkFavoriteStatus()
            }

            override fun onError(errorValue: AddMovieToFavoriteListUseCase.FavoriteMovieError) {
                Log.d(TAG, "AddMovieToFavoriteListUseCase complete with error=${errorValue.message}")
            }
        }

    /**
     * Remove movie from favorite callback
     */
    private val mRemoveFavoriteCallback =
        object : UseCase.ExecuteCallback<RemoveMovieFromFavoriteListUseCase.FavoriteMovieResponse,
                RemoveMovieFromFavoriteListUseCase.FavoriteMovieError> {
            override fun onComplete(responseValue: RemoveMovieFromFavoriteListUseCase.FavoriteMovieResponse) {
                Log.d(TAG, "RemoveMovieFromFavoriteListUseCase complete")
                checkFavoriteStatus()
            }

            override fun onError(errorValue: RemoveMovieFromFavoriteListUseCase.FavoriteMovieError) {
                Log.d(TAG, "RemoveMovieFromFavoriteListUseCase complete with error=${errorValue.message}")
            }
        }

    fun setUpData(
        movie: Movie,
        onDataReady: (
            bigTitle: String, smallTitle: String, overview: String, backdropUrl: String, posterUrl: String,
            trailerThumbnailsList: List<String>
        ) -> Unit
    ) {
        mMovie = movie
        val calendar = Calendar.getInstance(Locale.US)
        calendar.time = movie.getDateOfRelease()
        val bigTitle = "${movie.originalTitle} (${calendar.get(Calendar.YEAR)})"
        val smallTitle = movie.title
        val overview = movie.overview
        val backdropUrl = movie.backdropUrl
        val posterUrl = movie.posterUrl
        onDataReady(bigTitle, smallTitle, overview, backdropUrl, posterUrl, emptyList())
        checkFavoriteStatus()
        mAddMovieToFavoriteList.setExecuteCallback(mAddFavoriteCallback)
        mRemoveMovieFromFavoriteList.setExecuteCallback(mRemoveFavoriteCallback)
    }

    fun checkFavoriteStatus() {
        mMovie?.run {
            mCheckIfFavoriteMovie.execute(CheckIfFavoriteMovieUseCase.FavoriteMovieRequest(id))
        }
    }

    fun changeFavoriteStatus() {
        mMovie?.run {
            if (onFavoriteStatusChangedMutableLiveData.value == true) {
                mRemoveMovieFromFavoriteList.execute(RemoveMovieFromFavoriteListUseCase.FavoriteMovieRequest(id))
            } else {
                mAddMovieToFavoriteList.execute(AddMovieToFavoriteListUseCase.FavoriteMovieRequest(this))
            }
        }
    }

    fun registerFavoriteStatusChanged(
        lifecycleOwner: LifecycleOwner,
        onFavoriteStatusChanged: (isFavorite: Boolean) -> Unit
    ) {
        onFavoriteStatusChangedMutableLiveData.observe(lifecycleOwner, android.arch.lifecycle.Observer { isFavorite ->
            onFavoriteStatusChanged(isFavorite ?: false)
        })
    }

    fun unRegisterFavoriteStatusChanged(lifecycleOwner: LifecycleOwner) {
        onFavoriteStatusChangedMutableLiveData.removeObservers(lifecycleOwner)
    }
}