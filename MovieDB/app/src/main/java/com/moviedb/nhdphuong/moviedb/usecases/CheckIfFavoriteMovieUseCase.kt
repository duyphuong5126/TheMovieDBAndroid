package com.moviedb.nhdphuong.moviedb.usecases

import com.moviedb.nhdphuong.moviedb.data.MovieRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CheckIfFavoriteMovieUseCase @Inject constructor(private val mMovieRepository: MovieRepository) :
    UseCase<CheckIfFavoriteMovieUseCase.FavoriteMovieRequest, CheckIfFavoriteMovieUseCase.FavoriteMovieResponse, CheckIfFavoriteMovieUseCase.FavoriteMovieError> {

    private var mExecuteCallback: UseCase.ExecuteCallback<FavoriteMovieResponse, FavoriteMovieError>? = null

    override fun execute(requestValue: FavoriteMovieRequest) {
        if (requestValue.movieId >= 0) {
            mMovieRepository.checkIfFavoriteMovie(requestValue.movieId, onComplete = { isFavorite ->
                mExecuteCallback?.onComplete(FavoriteMovieResponse(isFavorite))
            })
        } else {
            mExecuteCallback?.onError(FavoriteMovieError("Movie's id must be greater than zero"))
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

    data class FavoriteMovieRequest(val movieId: Long) : UseCase.RequestValue

    data class FavoriteMovieResponse(var isFavorite: Boolean) : UseCase.ResponseValue

    data class FavoriteMovieError(val message: String) : UseCase.ErrorValue
}