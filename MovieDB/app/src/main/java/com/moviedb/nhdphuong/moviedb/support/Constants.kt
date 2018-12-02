package com.moviedb.nhdphuong.moviedb.support

class Constants {
    companion object {
        // Api constants
        const val API_BASE_URL = "http://api.themoviedb.org"
        const val API_KEY = "4df263f48a4fe2621749627f5d001bf0"

        const val MOVIE_VOTE_COUNT = "vote_count" //Integer
        const val MOVIE_ID = "id" // Long
        const val MOVIE_IS_VIDEO = "video" // Boolean
        const val MOVIE_AVERAGE_VOTE = "vote_average" // Float
        const val MOVIE_TITLE = "title" // String
        const val MOVIE_POPULARITY = "popularity" // Double
        const val MOVIE_POSTER_PATH = "poster_path" // String, path of poster
        const val MOVIE_ORIGINAL_LANGUAGE = "original_language" // String
        const val MOVIE_ORIGINAL_TITLE = "original_title" //String
        const val MOVIE_GENRE_IDS = "genre_ids" // List of integers
        const val MOVIE_BACKDROP_PATH = "backdrop_path" // String
        const val MOVIE_ADULT = "adult" //Boolean
        const val MOVIE_OVERVIEW = "overview" // String, description
        const val MOVIE_RELEASE_DATE = "release_date" //Date

        const val REMOTE_MOVIE_RESULTS = "results"
        const val REMOTE_MOVIE_PAGE = "page"
        const val REMOTE_MOVIE_TOTAL_RESULTS = "total_results"
        const val REMOTE_MOVIE_DATES = "dates"
        const val DATE_MAXIMUM = "maximum"
        const val DATE_MINIMUM = "minimum"
        const val REMOTE_MOVIE_TOTAL_PAGES = "total_pages"

        // Database constants
        const val MOVIES_DATABASE = "movies_database"
        const val TABLE_MOVIES = "movies"

        //Others
        const val DATE_FORMAT = "yyyy-MM-dd"
        const val IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w500"
    }
}