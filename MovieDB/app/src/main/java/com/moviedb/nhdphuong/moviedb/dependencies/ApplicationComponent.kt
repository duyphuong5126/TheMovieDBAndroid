package com.moviedb.nhdphuong.moviedb.dependencies

import com.moviedb.nhdphuong.moviedb.MovieDBApplication
import com.moviedb.nhdphuong.moviedb.dependencies.modules.*
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [ApplicationModule::class, RepositoriesModule::class, ViewModelModule::class,
        AndroidInjectionModule::class, ActivityModule::class,
        AndroidSupportInjectionModule::class, FragmentModule::class]
)
interface ApplicationComponent : AndroidInjector<MovieDBApplication>