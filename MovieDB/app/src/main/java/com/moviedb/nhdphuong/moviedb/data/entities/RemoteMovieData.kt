package com.moviedb.nhdphuong.moviedb.data.entities

import com.google.gson.annotations.SerializedName
import com.moviedb.nhdphuong.moviedb.support.Constants

data class RemoteMovieData(
    @field:SerializedName(Constants.REMOTE_MOVIE_RESULTS) var results: List<Movie>,
    @field:SerializedName(Constants.REMOTE_MOVIE_PAGE) var pageNumber: Int,
    @field:SerializedName(Constants.REMOTE_MOVIE_TOTAL_RESULTS) var totalResults: Int,
    @field:SerializedName(Constants.REMOTE_MOVIE_DATES) var dateRange: DateRange,
    @field:SerializedName(Constants.REMOTE_MOVIE_TOTAL_PAGES) var totalPages: Int
)