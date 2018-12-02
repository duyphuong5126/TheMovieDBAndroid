package com.moviedb.nhdphuong.moviedb

import android.app.Activity
import android.app.Application
import android.support.v4.app.Fragment
import android.util.Log
import com.moviedb.nhdphuong.moviedb.dependencies.DaggerApplicationComponent
import com.moviedb.nhdphuong.moviedb.dependencies.ApplicationComponent
import com.moviedb.nhdphuong.moviedb.dependencies.modules.ApplicationModule
import com.moviedb.nhdphuong.moviedb.dependencies.modules.RepositoriesModule
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class MovieDBApplication : Application(), HasActivityInjector, HasSupportFragmentInjector {
    companion object {
        private const val TAG = "MovieDBApplication"

        private var mInstance: MovieDBApplication? = null
        val instance: MovieDBApplication?
            get() = mInstance
    }

    private lateinit var mApplicationComponent: ApplicationComponent

    @Inject
    lateinit var mActivityInjector: DispatchingAndroidInjector<Activity>

    @Inject
    lateinit var mFragmentInjector: DispatchingAndroidInjector<Fragment>

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate")
        if (mInstance == null) {
            mInstance = this
        }
        mApplicationComponent = DaggerApplicationComponent.builder()
            .applicationModule(ApplicationModule(this))
            .repositoriesModule(RepositoriesModule())
            .build()
        mApplicationComponent.inject(this)
    }

    override fun activityInjector(): AndroidInjector<Activity> = mActivityInjector

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = mFragmentInjector
}