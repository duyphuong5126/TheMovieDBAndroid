package com.moviedb.nhdphuong.moviedb.network

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.moviedb.nhdphuong.moviedb.support.Constants
import java.lang.reflect.Type
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class GsonUTCDateAdapter : JsonDeserializer<Date> {

    private val dateFormat: SimpleDateFormat = SimpleDateFormat(Constants.DATE_FORMAT, Locale.US)

    init {
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
    }

    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Date {
        return try {
            dateFormat.parse(json.asString)
        } catch (e: ParseException) {
            throw JsonParseException(e)
        }
    }
}