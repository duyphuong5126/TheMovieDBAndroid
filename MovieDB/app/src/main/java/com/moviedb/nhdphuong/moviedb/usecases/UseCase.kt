package com.moviedb.nhdphuong.moviedb.usecases

interface UseCase<RQ : UseCase.RequestValue, RV : UseCase.ResponseValue, EV : UseCase.ErrorValue> {
    fun execute(requestValue: RQ)
    fun cancel()
    fun setExecuteCallback(executeCallback: ExecuteCallback<RV, EV>)
    fun cleanUp()

    interface RequestValue

    interface ResponseValue

    interface ErrorValue

    interface ExecuteCallback<RV, EV> {
        fun onComplete(responseValue: RV)
        fun onError(errorValue: EV)
    }
}