/*
 * Copyright 2022 Joel Kanyi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kanyideveloper.core_database.converters

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kanyideveloper.core.model.Ingredient
import com.kanyideveloper.core.model.Meal
import com.kanyideveloper.core.model.MealDetails

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

    @TypeConverter
    fun fromMealsDetailsJson(json: String): List<MealDetails> {
        return gson.fromJson<ArrayList<MealDetails>>(
            json,
            object : TypeToken<ArrayList<MealDetails>>() {}.type
        ) ?: emptyList()
    }

    @TypeConverter
    fun toMealsDetailsJson(meals: List<MealDetails>): String {
        return gson.toJson(
            meals,
            object : TypeToken<ArrayList<MealDetails>>() {}.type
        ) ?: "[]"
    }

    @TypeConverter
    fun fromMealsJson(json: String): List<Meal> {
        return gson.fromJson<ArrayList<Meal>>(
            json,
            object : TypeToken<ArrayList<Meal>>() {}.type
        ) ?: emptyList()
    }

    @TypeConverter
    fun toMealsJson(meals: List<Meal>): String {
        return gson.toJson(
            meals,
            object : TypeToken<ArrayList<Meal>>() {}.type
        ) ?: "[]"
    }

    @TypeConverter
    fun fromIngredientsJson(json: String): List<Ingredient> {
        return gson.fromJson<ArrayList<Ingredient>>(
            json,
            object : TypeToken<ArrayList<Ingredient>>() {}.type
        ) ?: emptyList()
    }

    @TypeConverter
    fun toIngredientsJson(ingredients: List<Ingredient>): String {
        return gson.toJson(
            ingredients,
            object : TypeToken<ArrayList<Ingredient>>() {}.type
        ) ?: "[]"
    }
}
