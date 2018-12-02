package com.moviedb.nhdphuong.moviedb.data.local

import android.support.annotation.WorkerThread
import com.moviedb.nhdphuong.moviedb.data.MovieDataSource
import com.moviedb.nhdphuong.moviedb.data.entities.FavoriteMovie
import com.moviedb.nhdphuong.moviedb.data.entities.Movie
import com.moviedb.nhdphuong.moviedb.database.FavoriteMovieDAO
import java.lang.Exception

class MovieLocalDataSource(private val mFavoriteMovieDAO: FavoriteMovieDAO) : MovieDataSource.Local {

    @WorkerThread
    override fun addMovieToFavorite(movie: Movie, onComplete: (Boolean) -> Unit) {
        try {
            mFavoriteMovieDAO.insert(FavoriteMovie(movie))
            onComplete(true)
        } catch (exception: Exception) {
            onComplete(false)
        }
    }

    @WorkerThread
    override fun addMovieToFavorite(favoriteMovie: FavoriteMovie, onComplete: (Boolean) -> Unit) {
        try {
            mFavoriteMovieDAO.insert(favoriteMovie)
            onComplete(true)
        } catch (exception: Exception) {
            onComplete(false)
        }
    }

    @WorkerThread
    override fun removeMovieToFavorite(movieId: Long, onComplete: (Boolean) -> Unit) {
        try {
            mFavoriteMovieDAO.deleteFavoriteMovie(movieId)
            onComplete(true)
        } catch (exception: Exception) {
            onComplete(false)
        }
    }

    @WorkerThread
    override fun checkIfFavoriteMovie(movieId: Long, onComplete: (Boolean) -> Unit) {
        try {
            val favoriteMoviesCount = mFavoriteMovieDAO.getFavoriteMoviesCount(movieId)
            onComplete(favoriteMoviesCount > 0)
        } catch (exception: Exception) {
            onComplete(false)
        }
    }

    @WorkerThread
    override fun getFavoriteMoviesCount(onComplete: (Int) -> Unit) {
        try {
            onComplete(mFavoriteMovieDAO.getFavoriteMoviesCount())
        } catch (exception: Exception) {
            onComplete(0)
        }
    }

    @WorkerThread
    override fun getFavoriteMovies(offset: Int, limit: Int, onComplete: (List<FavoriteMovie>) -> Unit) {
        try {
            onComplete(mFavoriteMovieDAO.getFavoriteMovies(offset, limit))
        } catch (exception: Exception) {
            onComplete(emptyList())
        }
    }
}