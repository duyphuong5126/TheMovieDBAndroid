package com.moviedb.nhdphuong.moviedb.usecases

import com.moviedb.nhdphuong.moviedb.data.MovieRepository
import com.moviedb.nhdphuong.moviedb.data.entities.FavoriteMovie
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoadFavoritesMoviesUseCase @Inject constructor(private val mMovieRepository: MovieRepository) :
    UseCase<LoadFavoritesMoviesUseCase.FavoriteMovieRequest, LoadFavoritesMoviesUseCase.FavoriteMovieResponse, LoadFavoritesMoviesUseCase.FavoriteMovieError> {

    private var mExecuteCallback: UseCase.ExecuteCallback<FavoriteMovieResponse, FavoriteMovieError>? = null

    override fun execute(requestValue: FavoriteMovieRequest) {
        if (requestValue.limit >= 0 && requestValue.offset >= 0) {
            mMovieRepository.getFavoriteMovies(requestValue.offset, requestValue.limit, onComplete = { favoriteMovies ->
                mExecuteCallback?.onComplete(FavoriteMovieResponse(favoriteMovies))
            })
        } else {
            mExecuteCallback?.onError(FavoriteMovieError("Limit and offset must be greater than zero"))
        }
    }

    override fun cancel() {

    }

    override fun setExecuteCallback(executeCallback: UseCase.ExecuteCallback<FavoriteMovieResponse, FavoriteMovieError>) {
        mExecuteCallback = executeCallback
    }

    override fun cleanUp() {
        mExecuteCallback = null
    }

    data class FavoriteMovieRequest(val offset: Int, val limit: Int) : UseCase.RequestValue

    data class FavoriteMovieResponse(val favoriteMovies: List<FavoriteMovie>) : UseCase.ResponseValue

    class FavoriteMovieError(val message: String) : UseCase.ErrorValue
}