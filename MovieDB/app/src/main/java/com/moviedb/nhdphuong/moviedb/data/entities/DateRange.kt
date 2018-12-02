package com.moviedb.nhdphuong.moviedb.data.entities

import com.google.gson.annotations.SerializedName
import com.moviedb.nhdphuong.moviedb.support.Constants

data class DateRange(
    @field:SerializedName(Constants.DATE_MINIMUM) var minDate: String,
    @field:SerializedName(Constants.DATE_MAXIMUM) var maxDate: String
)