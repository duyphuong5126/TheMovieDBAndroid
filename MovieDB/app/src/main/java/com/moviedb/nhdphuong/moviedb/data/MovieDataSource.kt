package com.moviedb.nhdphuong.moviedb.data

import android.support.annotation.WorkerThread
import com.moviedb.nhdphuong.moviedb.data.entities.FavoriteMovie
import com.moviedb.nhdphuong.moviedb.data.entities.Movie
import com.moviedb.nhdphuong.moviedb.data.entities.RemoteMovieData

interface MovieDataSource {
    interface Local {
        @WorkerThread
        fun addMovieToFavorite(movie: Movie, onComplete: (Boolean) -> Unit)

        @WorkerThread
        fun addMovieToFavorite(favoriteMovie: FavoriteMovie, onComplete: (Boolean) -> Unit)

        @WorkerThread
        fun removeMovieToFavorite(movieId: Long, onComplete: (Boolean) -> Unit)

        @WorkerThread
        fun checkIfFavoriteMovie(movieId: Long, onComplete: (Boolean) -> Unit)

        @WorkerThread
        fun getFavoriteMoviesCount(onComplete: (Int) -> Unit)

        @WorkerThread
        fun getFavoriteMovies(offset: Int, limit: Int, onComplete: (List<FavoriteMovie>) -> Unit)
    }

    interface Remote {
        @WorkerThread
        fun fetchRemoteMoviesList(
            pageNumber: Int,
            onComplete: (RemoteMovieData?) -> Unit,
            onError: (String, Int) -> Unit
        )
    }

    fun fetchMoviesList(pageNumber: Int, onComplete: (RemoteMovieData?) -> Unit, onError: (String, Int) -> Unit)

    fun insertFavoriteMovie(movie: Movie, onComplete: (Boolean) -> Unit)

    fun removeMovieToFavorite(movieId: Long, onComplete: (Boolean) -> Unit)

    fun checkIfFavoriteMovie(movieId: Long, onComplete: (Boolean) -> Unit)

    fun getFavoriteMoviesCount(onComplete: (Int) -> Unit)

    fun getFavoriteMovies(offset: Int, limit: Int, onComplete: (List<FavoriteMovie>) -> Unit)
}