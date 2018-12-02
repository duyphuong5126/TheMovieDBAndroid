package com.moviedb.nhdphuong.moviedb.usecases

import com.moviedb.nhdphuong.moviedb.data.MovieRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoveMovieFromFavoriteListUseCase @Inject constructor(private val mMovieRepository: MovieRepository) :
    UseCase<RemoveMovieFromFavoriteListUseCase.FavoriteMovieRequest, RemoveMovieFromFavoriteListUseCase.FavoriteMovieResponse, RemoveMovieFromFavoriteListUseCase.FavoriteMovieError> {

    private var mExecuteCallback: UseCase.ExecuteCallback<FavoriteMovieResponse, FavoriteMovieError>? = null

    override fun execute(requestValue: FavoriteMovieRequest) {
        if (requestValue.movieId >= 0) {
            mMovieRepository.removeMovieToFavorite(requestValue.movieId, onComplete = { isSuccess ->
                mExecuteCallback?.onComplete(FavoriteMovieResponse(isSuccess))
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

    data class FavoriteMovieResponse(val isSuccess: Boolean) : UseCase.ResponseValue

    data class FavoriteMovieError(val message: String) : UseCase.ErrorValue
}