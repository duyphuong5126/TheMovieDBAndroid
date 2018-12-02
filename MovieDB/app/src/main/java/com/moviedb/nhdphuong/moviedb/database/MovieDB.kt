package com.moviedb.nhdphuong.moviedb.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.moviedb.nhdphuong.moviedb.data.entities.FavoriteMovie

@Database(entities = [FavoriteMovie::class], version = 1)
abstract class MovieDB : RoomDatabase() {
    abstract fun getFavoriteMovieDAO(): FavoriteMovieDAO
}