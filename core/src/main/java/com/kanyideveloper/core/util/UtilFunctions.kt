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
package com.kanyideveloper.core.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.gson.Gson
import com.kanyideveloper.core.model.ErrorResponse
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import timber.log.Timber

fun String.stringToList(): List<String> {
    return this.split("\r\n").filter { !it.matches(Regex("[0-9]+")) }.filter { !it.isNullOrBlank() }
}

fun Context.imageUriToImageBitmap(uri: Uri): Bitmap {
    return if (Build.VERSION.SDK_INT < 28) {
        MediaStore.Images
            .Media.getBitmap(contentResolver, uri)
    } else {
        val source = ImageDecoder
            .createSource(contentResolver, uri)
        ImageDecoder.decodeBitmap(source)
    }
}

suspend fun <T> safeApiCall(
    dispatcher: CoroutineDispatcher,
    apiCall: suspend () -> T
): Resource<T> {
    return withContext(dispatcher) {
        try {
            Timber.e("Success ")
            Resource.Success(apiCall.invoke())
        } catch (throwable: Throwable) {
            Timber.e(throwable)
            when (throwable) {
                is IOException -> {
                    Timber.e("IO exception occured! $throwable")
                    Resource.Error(
                        message = "Please check your internet connection and try again later",
                        throwable = throwable
                    )
                }
                is HttpException -> {
                    val stringErrorBody = errorBodyAsString(throwable)
                    Timber.e("stringErrorBody $stringErrorBody")
                    if (stringErrorBody != null) {
                        val errorResponse = convertStringErrorResponseToJsonObject(stringErrorBody)
                        Timber.e("errorResponse $errorResponse")
                        Resource.Error(
                            message = errorResponse?.message,
                            throwable = throwable
                        )
                    } else {
                        Resource.Error(
                            message = "Unknown failure occurred, please try again later",
                            throwable = throwable
                        )
                    }
                }
                else -> {
                    Timber.e("In else statement $throwable")
                    Resource.Error(
                        message = "Unknown failure occurred, please try again later",
                        throwable = throwable
                    )
                }
            }
        }
    }
}

private fun convertStringErrorResponseToJsonObject(jsonString: String): ErrorResponse? {
    val gson = Gson()
    return gson.fromJson(jsonString, ErrorResponse::class.java)
}

fun errorBodyAsString(throwable: HttpException): String? {
    val reader = throwable.response()?.errorBody()?.charStream()
    return reader?.use { it.readText() }
}

@Composable
fun LottieAnim(
    resId: Int,
    modifier: Modifier = Modifier,
    height: Dp = 300.dp
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(resId = resId))
    LottieAnimation(
        modifier = modifier
            .fillMaxWidth()
            .height(height),
        iterations = LottieConstants.IterateForever,
        composition = composition
    )
}

fun convertMinutesToHours(minutes: Int): String {
    return if (minutes >= 60) {
        val hours = minutes / 60
        val remainingMinutes = minutes % 60

        if (hours == 1) {
            if (remainingMinutes == 0) {
                "$hours hr"
            } else {
                "$hours hr $remainingMinutes mins"
            }
        } else {
            if (remainingMinutes == 0) {
                "$hours hrs"
            } else {
                "$hours hrs $remainingMinutes mins"
            }
        }
    } else {
        "$minutes mins"
    }
}

fun showDayCookMessage(): String {
    // Get the time of day
    val date = Date()
    val cal = Calendar.getInstance()
    cal.time = date

    return when (cal[Calendar.HOUR_OF_DAY]) {
        in 12..16 -> {
            "What to cook for lunch?"
        }
        in 17..20 -> {
            "What to cook for dinner?"
        }
        in 21..23 -> {
            "What to cook tonight?"
        }
        else -> {
            "What to cook for breakfast?"
        }
    }
}

fun getTodaysDate(): String {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy")
    val today = Calendar.getInstance().time
    return dateFormat.format(today)
}

/*@SuppressLint("SimpleDateFormat")
fun generateDaysAndMonths(): List<Day> {
    val calendar = Calendar.getInstance()

    val days = mutableListOf<Day>()

    val startYear = 2023
    val endYear = 2050

    // Create a SimpleDateFormat instance for formatting the fullDate field
    val dateFormat = SimpleDateFormat("dd/MM/yyyy")

    // Iterate over the years in the given range
    for (year in startYear..endYear) {
        // Set the calendar to the first day of the year
        calendar.set(Calendar.YEAR, year)

        // Iterate over the months of the year
        for (month in 0..11) {
            // Set the calendar to the first day of the month
            calendar.set(Calendar.MONTH, month)

            // Get the number of days in the month
            val numDaysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

            // Iterate over the days of the month
            for (day in 1..numDaysInMonth) {
                calendar.set(Calendar.DAY_OF_MONTH, day)
                val dayOfWeek = calendar.getDisplayName(
                    Calendar.DAY_OF_WEEK,
                    Calendar.SHORT,
                    Locale.getDefault()
                )
                val displayDate = String.format("%02d", day) // format the day to always have two digits
                val fullDate = dateFormat.format(calendar.time) // use the SimpleDateFormat to format the fullDate field
                val displayMonth = calendar.getDisplayName(
                    Calendar.MONTH,
                    Calendar.SHORT,
                    Locale.getDefault()
                )
                val year = calendar.get(Calendar.YEAR).toString()
                val day = Day(dayOfWeek, displayDate, fullDate, displayMonth, year)
                days.add(day)
            }
        }
    }

    return days
}*/
