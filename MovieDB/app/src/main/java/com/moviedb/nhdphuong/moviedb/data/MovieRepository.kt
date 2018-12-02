package com.moviedb.nhdphuong.moviedb.data

import com.moviedb.nhdphuong.moviedb.annotation.coroutine.Default
import com.moviedb.nhdphuong.moviedb.annotation.coroutine.Main
import com.moviedb.nhdphuong.moviedb.data.entities.FavoriteMovie
import com.moviedb.nhdphuong.moviedb.data.entities.Movie
import com.moviedb.nhdphuong.moviedb.data.entities.RemoteMovieData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository @Inject constructor(
    @Default private val default: CoroutineScope,
    @Main private val main: CoroutineScope,
    private val mMovieRemoteDataSource: MovieDataSource.Remote,
    private val mMovieLocalDataSource: MovieDataSource.Local
) : MovieDataSource {
    override fun fetchMoviesList(
        pageNumber: Int,
        onComplete: (RemoteMovieData?) -> Unit,
        onError: (String, Int) -> Unit
    ) {
        default.launch {
            mMovieRemoteDataSource.fetchRemoteMoviesList(pageNumber, onComplete = { remoteMovieData ->
                main.launch {
                    onComplete(remoteMovieData)
                }
            }, onError = { message, code ->
                main.launch {
                    onError(message, code)
                }
            })
        }
    }

    override fun insertFavoriteMovie(movie: Movie, onComplete: (Boolean) -> Unit) {
        default.launch {
            mMovieLocalDataSource.addMovieToFavorite(movie, onComplete = { success ->
                main.launch {
                    onComplete(success)
                }
            })
        }
    }

    override fun removeMovieToFavorite(movieId: Long, onComplete: (Boolean) -> Unit) {
        default.launch {
            mMovieLocalDataSource.removeMovieToFavorite(movieId, onComplete = { success ->
                main.launch {
                    onComplete(success)
                }
            })
        }
    }

    override fun getFavoriteMoviesCount(onComplete: (Int) -> Unit) {
        default.launch {
            mMovieLocalDataSource.getFavoriteMoviesCount { favoriteCount ->
                main.launch {
                    onComplete(favoriteCount)
                }
            }
        }
    }

    override fun checkIfFavoriteMovie(movieId: Long, onComplete: (Boolean) -> Unit) {
        default.launch {
            mMovieLocalDataSource.checkIfFavoriteMovie(movieId, onComplete = { isFavorite ->
                main.launch {
                    onComplete(isFavorite)
                }
            })
        }
    }

    override fun getFavoriteMovies(offset: Int, limit: Int, onComplete: (List<FavoriteMovie>) -> Unit) {
        default.launch {
            mMovieLocalDataSource.getFavoriteMovies(offset, limit, onComplete = { favoriteMoviesList ->
                main.launch {
                    onComplete(favoriteMoviesList)
                }
            })
        }
    }
}