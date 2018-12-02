package com.moviedb.nhdphuong.moviedb.database

import android.arch.persistence.room.*
import com.moviedb.nhdphuong.moviedb.data.entities.FavoriteMovie
import com.moviedb.nhdphuong.moviedb.support.Constants

@Dao
interface FavoriteMovieDAO {
    companion object {
        private const val TABLE_MOVIES = Constants.TABLE_MOVIES
        private const val MOVIE_ID = Constants.MOVIE_ID
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(favoriteMovie: FavoriteMovie)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg favoriteMovies: FavoriteMovie)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(favoriteMovies: List<FavoriteMovie>)

    @Query("DELETE FROM $TABLE_MOVIES WHERE $MOVIE_ID = :movieId")
    fun deleteFavoriteMovie(movieId: Long)

    @Query("SELECT COUNT(*) FROM $TABLE_MOVIES")
    fun getFavoriteMoviesCount(): Int

    @Query("SELECT COUNT(*) FROM $TABLE_MOVIES WHERE $MOVIE_ID = :movieId")
    fun getFavoriteMoviesCount(movieId: Long): Int

    @Query("SELECT * FROM $TABLE_MOVIES limit :limit offset :offset")
    fun getFavoriteMovies(offset: Int, limit: Int): List<FavoriteMovie>
}