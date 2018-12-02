package com.moviedb.nhdphuong.moviedb.usecases

import com.moviedb.nhdphuong.moviedb.data.MovieRepository
import com.moviedb.nhdphuong.moviedb.data.entities.Movie
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FetchMoviesUseCase @Inject constructor(private val mMovieRepository: MovieRepository) :
    UseCase<FetchMoviesUseCase.FetchMoviesRequestValue, FetchMoviesUseCase.FetchMoviesResponse, FetchMoviesUseCase.FetchMoviesError> {
    private var mExecuteCallback: UseCase.ExecuteCallback<FetchMoviesResponse, FetchMoviesError>? = null

    override fun execute(requestValue: FetchMoviesRequestValue) {
        mMovieRepository.fetchMoviesList(requestValue.pageNumber, onComplete = { remoteMovieData ->
            mExecuteCallback?.onComplete(FetchMoviesResponse(remoteMovieData?.results))
        }, onError = { message, code ->
            mExecuteCallback?.onError(FetchMoviesError(message, code))
        })
    }

    override fun cancel() {

    }

    override fun setExecuteCallback(executeCallback: UseCase.ExecuteCallback<FetchMoviesResponse, FetchMoviesError>) {
        mExecuteCallback = executeCallback
    }

    data class FetchMoviesRequestValue(val pageNumber: Int) : UseCase.RequestValue

    data class FetchMoviesResponse(private val mMovies: List<Movie>?) : UseCase.ResponseValue {
        val movies: List<Movie>
            get() = mMovies ?: emptyList()
    }

    class FetchMoviesError(val message: String, val code: Int) : UseCase.ErrorValue
}