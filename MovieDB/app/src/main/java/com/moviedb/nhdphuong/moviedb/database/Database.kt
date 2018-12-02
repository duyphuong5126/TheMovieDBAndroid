package com.moviedb.nhdphuong.moviedb.database

import android.arch.persistence.room.Room
import com.moviedb.nhdphuong.moviedb.MovieDBApplication
import com.moviedb.nhdphuong.moviedb.support.Constants

class Database {
    companion object {
        private var mInstance: MovieDB? = null
        val instance: MovieDB
            get() {
                if (mInstance == null) {
                    mInstance = Room.databaseBuilder(
                        MovieDBApplication.instance?.applicationContext!!,
                        MovieDB::class.java,
                        Constants.MOVIES_DATABASE
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                }
                return mInstance!!
            }
    }
}