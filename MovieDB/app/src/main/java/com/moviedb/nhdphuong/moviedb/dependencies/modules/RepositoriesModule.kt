package com.moviedb.nhdphuong.moviedb.dependencies.modules

import com.moviedb.nhdphuong.moviedb.data.MovieDataSource
import com.moviedb.nhdphuong.moviedb.data.local.MovieLocalDataSource
import com.moviedb.nhdphuong.moviedb.data.remote.MovieRemoteDataSource
import com.moviedb.nhdphuong.moviedb.database.FavoriteMovieDAO
import com.moviedb.nhdphuong.moviedb.network.api.MoviesApiService
import com.moviedb.nhdphuong.moviedb.support.Constants
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoriesModule {
    @Singleton
    @Provides
    fun providesMoviesRemoteDataSource(moviesApiService: MoviesApiService): MovieDataSource.Remote =
        MovieRemoteDataSource(moviesApiService, Constants.API_KEY)

    @Singleton
    @Provides
    fun providesMoviesLocalDataSource(favoriteMovieDAO: FavoriteMovieDAO): MovieDataSource.Local =
        MovieLocalDataSource(favoriteMovieDAO)
}