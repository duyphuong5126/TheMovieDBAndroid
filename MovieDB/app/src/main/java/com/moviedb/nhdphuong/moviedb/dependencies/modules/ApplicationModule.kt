package com.moviedb.nhdphuong.moviedb.dependencies.modules

import com.moviedb.nhdphuong.moviedb.MovieDBApplication
import com.moviedb.nhdphuong.moviedb.annotation.coroutine.Default
import com.moviedb.nhdphuong.moviedb.annotation.coroutine.IO
import com.moviedb.nhdphuong.moviedb.annotation.coroutine.Main
import com.moviedb.nhdphuong.moviedb.annotation.coroutine.Unconfined
import com.moviedb.nhdphuong.moviedb.database.Database
import com.moviedb.nhdphuong.moviedb.database.FavoriteMovieDAO
import com.moviedb.nhdphuong.moviedb.network.ServiceGenerator
import com.moviedb.nhdphuong.moviedb.network.api.MoviesApiService
import com.moviedb.nhdphuong.moviedb.support.Constants
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
class ApplicationModule(private val mMovieDBApp: MovieDBApplication) {
    @Provides
    fun provideContext() = mMovieDBApp.applicationContext!!

    @Provides
    fun provideApplication() = mMovieDBApp

    @Singleton
    @Provides
    fun providesMovieApiService(): MoviesApiService {
        ServiceGenerator.setBaseUrl(Constants.API_BASE_URL)
        ServiceGenerator.setInterceptor(null)
        return ServiceGenerator.createService(MoviesApiService::class.java)
    }

    @Singleton
    @Provides
    fun providesFavoriteMovieDAO(): FavoriteMovieDAO = Database.instance.getFavoriteMovieDAO()

    @Provides
    @Main
    fun providesMainCoroutineScope(): CoroutineScope = CoroutineScope(Dispatchers.Main)

    @Provides
    @IO
    fun providesIOCoroutineScope(): CoroutineScope = CoroutineScope(Dispatchers.IO)

    @Provides
    @Default
    fun providesDefaultCoroutineScope(): CoroutineScope = CoroutineScope(Dispatchers.Default)

    @Provides
    @Unconfined
    fun providesUnconfinedCoroutineScope(): CoroutineScope = CoroutineScope(Dispatchers.Unconfined)
}