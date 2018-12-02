package com.moviedb.nhdphuong.moviedb.usecases

import com.moviedb.nhdphuong.moviedb.data.MovieRepository
import com.moviedb.nhdphuong.moviedb.data.entities.Movie
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddMovieToFavoriteListUseCase @Inject constructor(private val mMovieRepository: MovieRepository) :
    UseCase<AddMovieToFavoriteListUseCase.FavoriteMovieRequest, AddMovieToFavoriteListUseCase.FavoriteMovieResponse, AddMovieToFavoriteListUseCase.FavoriteMovieError> {

    private var mExecuteCallback: UseCase.ExecuteCallback<FavoriteMovieResponse, FavoriteMovieError>? = null

    override fun execute(requestValue: FavoriteMovieRequest) {
        if (!requestValue.movie.isDummy()) {
            mMovieRepository.insertFavoriteMovie(requestValue.movie, onComplete = { isSuccess ->
                mExecuteCallback?.onComplete(FavoriteMovieResponse(isSuccess))
            })
        } else {
            mExecuteCallback?.onError(FavoriteMovieError("Cannot add a dummy item"))
        }
    }

    override fun cancel() {

    }

    override fun setExecuteCallback(executeCallback: UseCase.ExecuteCallback<FavoriteMovieResponse, FavoriteMovieError>) {
        mExecuteCallback = executeCallback
    }

    data class FavoriteMovieRequest(val movie: Movie) : UseCase.RequestValue

    data class FavoriteMovieResponse(var isSuccess: Boolean) : UseCase.ResponseValue

    data class FavoriteMovieError(val message: String) : UseCase.ErrorValue
}