package com.moviedb.nhdphuong.moviedb.dependencies.modules

import com.moviedb.nhdphuong.moviedb.ui.home.FavoriteListFragment
import com.moviedb.nhdphuong.moviedb.ui.home.NowShowingFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {
    @ContributesAndroidInjector
    internal abstract fun contributeNowShowingFragment(): NowShowingFragment

    @ContributesAndroidInjector
    internal abstract fun contributeFavoriteListFragment(): FavoriteListFragment
}