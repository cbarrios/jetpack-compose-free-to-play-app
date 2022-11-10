package com.lalosapps.freetoplay.data.local.room.entity

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lalosapps.freetoplay.domain.model.Screenshot

class Converters {
    @TypeConverter
    fun fromScreenshots(list: List<Screenshot>): String {
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun toScreenshots(string: String): List<Screenshot> {
        val gson = Gson()
        val type = object : TypeToken<List<Screenshot>>() {}.type
        return gson.fromJson(string, type)
    }
}