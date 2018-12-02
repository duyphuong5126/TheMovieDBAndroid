package com.moviedb.nhdphuong.moviedb.dependencies.modules

import com.moviedb.nhdphuong.moviedb.ui.details.MovieDetailsActivity
import com.moviedb.nhdphuong.moviedb.ui.home.HomeActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {
    @ContributesAndroidInjector
    internal abstract fun contributeHomeActivity(): HomeActivity

    @ContributesAndroidInjector
    internal abstract fun contributeMovieDetailsActivity(): MovieDetailsActivity
}