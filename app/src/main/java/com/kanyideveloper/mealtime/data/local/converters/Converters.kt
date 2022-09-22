package com.kanyideveloper.mealtime.data.local.converters

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@ProvidedTypeConverter
class Converters(private val gson: Gson) {

    @TypeConverter
    fun fromIngredients(json: String): List<String> {
        return gson.fromJson<ArrayList<String>>(
            json,
            object : TypeToken<ArrayList<String>>() {}.type
        ) ?: emptyList()
    }

    @TypeConverter
    fun toIngredients(ingredients: List<String>): String {
        return gson.toJson(
            ingredients,
            object : TypeToken<ArrayList<String>>() {}.type
        ) ?: "[]"
    }
}