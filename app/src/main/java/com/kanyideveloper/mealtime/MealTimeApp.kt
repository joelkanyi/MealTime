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
package com.kanyideveloper.mealtime

import android.app.Application
import com.kanyideveloper.core.util.Constants.QONVERSION_PROJECT_KEY
import com.qonversion.android.sdk.Qonversion
import com.qonversion.android.sdk.QonversionConfig
import com.qonversion.android.sdk.dto.QLaunchMode
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class MealTimeApp : Application() {
    override fun onCreate() {
        super.onCreate()
        setupTimber()

        // Initialize Qonversion
        val qonversionConfig = QonversionConfig.Builder(
            this,
            QONVERSION_PROJECT_KEY,
            QLaunchMode.Analytics
        ).build()
        Qonversion.initialize(qonversionConfig)
    }
}

private fun setupTimber() {
    Timber.plant(Timber.DebugTree())
}