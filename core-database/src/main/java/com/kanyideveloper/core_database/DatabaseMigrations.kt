/*
 * Copyright 2023 Joel Kanyi.
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
package com.kanyideveloper.core_database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.kanyideveloper.core.util.Constants.FAVORITES_TABLE
import com.kanyideveloper.core.util.Constants.MEAL_PLAN_TABLE
import com.kanyideveloper.core.util.Constants.MEAL_TABLE

object DatabaseMigrations {
    val migration_1_3 = object : Migration(1, 3) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // Migrate FavoriteEntity table
            database.execSQL("ALTER TABLE $FAVORITES_TABLE RENAME TO temp_table")
            database.execSQL(
                "CREATE TABLE $FAVORITES_TABLE (id INTEGER PRIMARY KEY, onlineMealId TEXT, localMealId TEXT, isOnline INTEGER NOT NULL, mealName TEXT NOT NULL, mealImageUrl TEXT NOT NULL, isFavorite INTEGER NOT NULL)"
            )
            database.execSQL(
                "INSERT INTO $FAVORITES_TABLE (id, onlineMealId, localMealId, isOnline, mealName, mealImageUrl, isFavorite) SELECT id, onlineMealId, CAST(localMealId AS TEXT), isOnline, mealName, mealImageUrl, isFavorite FROM temp_table"
            )
            database.execSQL("DROP TABLE temp_table")

            // Migrate MealEntity table
            database.execSQL("ALTER TABLE $MEAL_TABLE RENAME TO temp_table")
            database.execSQL(
                "CREATE TABLE $MEAL_TABLE (name TEXT NOT NULL, imageUrl TEXT NOT NULL, cookingTime INTEGER NOT NULL, servingPeople INTEGER NOT NULL, category TEXT NOT NULL, cookingDifficulty TEXT NOT NULL, ingredients TEXT NOT NULL, cookingInstructions TEXT NOT NULL, isFavorite INTEGER NOT NULL, id TEXT PRIMARY KEY NOT NULL)"
            )
            database.execSQL(
                "INSERT INTO $MEAL_TABLE (name, imageUrl, cookingTime, servingPeople, category, cookingDifficulty, ingredients, cookingInstructions, isFavorite, id) SELECT name, imageUrl, cookingTime, servingPeople, category, cookingDifficulty, ingredients, cookingInstructions, isFavorite, CAST(id AS TEXT) FROM temp_table"
            )
            database.execSQL("DROP TABLE temp_table")

            // Migrate MealPlanEntity table
            database.execSQL("ALTER TABLE $MEAL_PLAN_TABLE RENAME TO temp_table")
            database.execSQL(
                "CREATE TABLE $MEAL_PLAN_TABLE (mealTypeName TEXT NOT NULL, meals BLOB NOT NULL, mealDate TEXT NOT NULL, id TEXT PRIMARY KEY NOT NULL)"
            )
            database.execSQL(
                "INSERT INTO $MEAL_PLAN_TABLE (mealTypeName, meals, mealDate, id) SELECT mealTypeName, meals, mealDate, CAST(id AS TEXT) FROM temp_table"
            )
            database.execSQL("DROP TABLE temp_table")
        }
    }
}
