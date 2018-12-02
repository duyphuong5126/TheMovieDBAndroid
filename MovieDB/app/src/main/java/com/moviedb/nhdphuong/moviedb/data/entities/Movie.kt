package com.moviedb.nhdphuong.moviedb.data.entities

import com.google.gson.annotations.SerializedName
import com.moviedb.nhdphuong.moviedb.support.Constants
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

data class Movie(
    @field:SerializedName(Constants.MOVIE_ID) var id: Long,
    @field:SerializedName(Constants.MOVIE_ORIGINAL_TITLE) var originalTitle: String,
    @field:SerializedName(Constants.MOVIE_TITLE) var title: String,
    @field:SerializedName(Constants.MOVIE_ORIGINAL_LANGUAGE) var originalLanguage: String,
    @field:SerializedName(Constants.MOVIE_IS_VIDEO) var isVideo: Boolean,
    @field:SerializedName(Constants.MOVIE_ADULT) var isForAdult: Boolean,
    @field:SerializedName(Constants.MOVIE_GENRE_IDS) var genreIds: List<Int>,
    @field:SerializedName(Constants.MOVIE_OVERVIEW) var overview: String,
    @field:SerializedName(Constants.MOVIE_VOTE_COUNT) var voteCount: Int,
    @field:SerializedName(Constants.MOVIE_AVERAGE_VOTE) var averageVote: Float,
    @field:SerializedName(Constants.MOVIE_POPULARITY) var popularity: Double,
    @field:SerializedName(Constants.MOVIE_POSTER_PATH) var posterPath: String,
    @field:SerializedName(Constants.MOVIE_BACKDROP_PATH) var backDropPath: String,
    @field:SerializedName(Constants.MOVIE_RELEASE_DATE) var releaseDate: String
) : Serializable {
    fun getDateOfRelease(): Date = SimpleDateFormat(Constants.DATE_FORMAT, Locale.US).parse(releaseDate)

    fun isDummy(): Boolean {
        return id < 0L
    }

    val posterUrl: String
        get() = "${Constants.IMAGE_BASE_URL}$posterPath?api_key=${Constants.API_KEY}"

    val backdropUrl: String
        get() = "${Constants.IMAGE_BASE_URL}$backDropPath?api_key=${Constants.API_KEY}"

    override fun equals(other: Any?): Boolean {
        return if (other !is Movie) {
            false
        } else {
            this.id == other.id && this.originalTitle == other.originalTitle && this.title == other.title
                    && this.originalLanguage == other.originalLanguage && this.isVideo == other.isVideo
                    && this.isForAdult == other.isForAdult && this.overview == other.overview
                    && this.voteCount == other.voteCount && this.averageVote == other.averageVote
                    && this.popularity == other.popularity && this.posterPath == other.posterPath
                    && this.backDropPath == other.backDropPath && this.releaseDate == other.releaseDate
        }
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }
}