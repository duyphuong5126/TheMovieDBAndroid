package com.moviedb.nhdphuong.moviedb.data.entities

import android.arch.persistence.room.*
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.moviedb.nhdphuong.moviedb.support.Constants

@Entity(tableName = Constants.TABLE_MOVIES, indices = [Index(value = [Constants.MOVIE_ORIGINAL_TITLE])])
data class FavoriteMovie(
    @PrimaryKey @ColumnInfo(name = Constants.MOVIE_ID) var id: Long,
    @ColumnInfo(name = Constants.MOVIE_ORIGINAL_TITLE) var originalTitle: String,
    @ColumnInfo(name = Constants.MOVIE_TITLE) var title: String,
    @ColumnInfo(name = Constants.MOVIE_ORIGINAL_LANGUAGE) var originalLanguage: String,
    @ColumnInfo(name = Constants.MOVIE_IS_VIDEO) var isVideo: Boolean,
    @ColumnInfo(name = Constants.MOVIE_ADULT) var isForAdult: Boolean,
    @ColumnInfo(name = Constants.MOVIE_GENRE_IDS) var genreIds: String,
    @ColumnInfo(name = Constants.MOVIE_OVERVIEW) var overview: String,
    @ColumnInfo(name = Constants.MOVIE_VOTE_COUNT) var voteCount: Int,
    @ColumnInfo(name = Constants.MOVIE_AVERAGE_VOTE) var averageVote: Float,
    @ColumnInfo(name = Constants.MOVIE_POPULARITY) var popularity: Double,
    @ColumnInfo(name = Constants.MOVIE_POSTER_PATH) var posterPath: String,
    @ColumnInfo(name = Constants.MOVIE_BACKDROP_PATH) var backDropPath: String,
    @ColumnInfo(name = Constants.MOVIE_RELEASE_DATE) var releaseDate: String
) {
    companion object {
        fun integerListToString(integers: List<Int>): String {
            val jsonArray = JsonArray()
            for (integer in integers) {
                jsonArray.add(integer)
            }
            return jsonArray.toString()
        }
    }

    @Ignore
    val movie = Movie(
        id, originalTitle, title, originalLanguage, isVideo, isForAdult, getListGenreId(), overview,
        voteCount, averageVote, popularity, posterPath, backDropPath, releaseDate
    )

    constructor(movie: Movie) : this(
        movie.id,
        movie.originalTitle,
        movie.title,
        movie.originalLanguage,
        movie.isVideo,
        movie.isForAdult,
        FavoriteMovie.integerListToString(movie.genreIds),
        movie.overview,
        movie.voteCount,
        movie.averageVote,
        movie.popularity,
        movie.posterPath,
        movie.backDropPath,
        movie.releaseDate
    )

    private fun getListGenreId(): List<Int> {
        val gSon = Gson()
        val jsonArray = gSon.fromJson<JsonArray>(genreIds, JsonArray::class.java)
        val result = ArrayList<Int>()
        for (element in jsonArray) {
            result.add(element.asInt)
        }
        return result
    }
}