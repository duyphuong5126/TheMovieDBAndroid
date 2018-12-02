package com.moviedb.nhdphuong.moviedb.usecases

import com.moviedb.nhdphuong.moviedb.data.MovieRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetFavoriteMovieCountUseCase @Inject constructor(private val mMovieRepository: MovieRepository) :
    UseCase<GetFavoriteMovieCountUseCase.FavoriteMovieRequest, GetFavoriteMovieCountUseCase.FavoriteMovieResponse, GetFavoriteMovieCountUseCase.FavoriteMovieError> {
    private var mExecuteCallback: UseCase.ExecuteCallback<FavoriteMovieResponse, FavoriteMovieError>? = null

    override fun execute(requestValue: FavoriteMovieRequest) {
        mMovieRepository.getFavoriteMoviesCount { favoriteCount ->
            mExecuteCallback?.onComplete(FavoriteMovieResponse(favoriteCount))
        }
    }

    override fun cancel() {

    }

    override fun setExecuteCallback(executeCallback: UseCase.ExecuteCallback<FavoriteMovieResponse, FavoriteMovieError>) {
        mExecuteCallback = executeCallback
    }

    class FavoriteMovieRequest : UseCase.RequestValue

    data class FavoriteMovieResponse(val favoriteCount: Int) : UseCase.ResponseValue

    class FavoriteMovieError(val message: String) : UseCase.ErrorValue
}