package com.moviedb.nhdphuong.moviedb.dependencies.modules

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.moviedb.nhdphuong.moviedb.annotation.ViewModelKey
import com.moviedb.nhdphuong.moviedb.viewmodels.AppViewModelFactory
import com.moviedb.nhdphuong.moviedb.viewmodels.FavoriteViewModel
import com.moviedb.nhdphuong.moviedb.viewmodels.MovieDetailsViewModel
import com.moviedb.nhdphuong.moviedb.viewmodels.MoviesViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(MoviesViewModel::class)
    abstract fun bindMoviesViewModel(moviesViewModel: MoviesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MovieDetailsViewModel::class)
    abstract fun bindMovieDetailsViewModel(movieDetailsViewModel: MovieDetailsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FavoriteViewModel::class)
    abstract fun bindFavoriteViewModel(favoriteViewModel: FavoriteViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: AppViewModelFactory): ViewModelProvider.Factory
}