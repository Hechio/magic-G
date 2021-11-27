package com.stevehechio.apps.magictheg.data.local.convertors

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Created by stevehechio on 11/27/21
 */
object ColorListTypeConvertor {
    @TypeConverter
    fun fromStringToColorList(value: String?): List<String>? {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromColorListToString(list: List<String>?): String? {
        val gson = Gson()
        return gson.toJson(list)
    }
}