package com.moviedb.nhdphuong.moviedb.usecases

import com.moviedb.nhdphuong.moviedb.data.MovieRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetTotalRemotePagesUseCase @Inject constructor(private val mMovieRepository: MovieRepository) :
    UseCase<GetTotalRemotePagesUseCase.GetRemotePagesRequestValue, GetTotalRemotePagesUseCase.GetRemotePagesResponse, GetTotalRemotePagesUseCase.GetRemotePagesError> {
    private var mExecuteCallback: UseCase.ExecuteCallback<GetRemotePagesResponse, GetRemotePagesError>? = null

    override fun execute(requestValue: GetRemotePagesRequestValue) {
        mMovieRepository.fetchMoviesList(1, onComplete = { remoteMovieData ->
            mExecuteCallback?.onComplete(GetRemotePagesResponse(remoteMovieData?.totalPages ?: 0))
        }, onError = { message, code ->
            mExecuteCallback?.onError(GetRemotePagesError(message, code))
        })
    }

    override fun cancel() {

    }

    override fun setExecuteCallback(executeCallback: UseCase.ExecuteCallback<GetRemotePagesResponse, GetRemotePagesError>) {
        mExecuteCallback = executeCallback
    }

    override fun cleanUp() {
        mExecuteCallback = null
    }

    class GetRemotePagesRequestValue : UseCase.RequestValue

    data class GetRemotePagesResponse(val totalPages: Int) : UseCase.ResponseValue

    class GetRemotePagesError(val message: String, val code: Int) : UseCase.ErrorValue
}