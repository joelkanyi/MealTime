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

import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import com.kanyideveloper.core.util.Constants.MEAL_PLAN_TABLE
import com.kanyideveloper.core.util.Constants.MEAL_TABLE
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

private const val DB_NAME = "test"

@RunWith(AndroidJUnit4::class)
class MigrationsTest {

    @get:Rule
    val helper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        MealTimeDatabase::class.java,
        listOf(
            DatabaseMigrations.Migration1To2(),
            DatabaseMigrations.Migration2To3()
        ),
        FrameworkSQLiteOpenHelperFactory()
    )

    @Test
    fun migration1To2_containsCorrectData() {
        var db = helper.createDatabase(DB_NAME, 1).apply {
            execSQL("INSERT INTO $MEAL_PLAN_TABLE VALUES('Lunch','[]','28/07/2023', '0')")
            close()
        }

        db = helper.runMigrationsAndValidate(DB_NAME, 2, true)

        db.query("SELECT * FROM $MEAL_PLAN_TABLE").apply {
            assertThat(moveToFirst()).isTrue()
            assertThat(getString(getColumnIndex("mealDate"))).isEqualTo("28/07/2023")
        }
    }

    @Test
    fun migration2To3_containsCorrectData() {
        var db = helper.createDatabase(DB_NAME, 1).apply {
            execSQL(
                "INSERT INTO $MEAL_TABLE VALUES('Ugali','https://','0','0','Category','Hard','[]','[]','1','1')"
            )
            close()
        }

        db = helper.runMigrationsAndValidate(DB_NAME, 3, true)

        db.query("SELECT * FROM $MEAL_TABLE").apply {
            assertThat(moveToFirst()).isTrue()
            assertThat(getString(getColumnIndex("name"))).isEqualTo("Ugali")
        }
    }
}
