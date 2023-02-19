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
package com.kanyideveloper.mealplanner.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.kanyideveloper.mealtime.core.R
import timber.log.Timber

// @AndroidEntryPoint
class MyAlarm : BroadcastReceiver() {

    /*@Inject
    lateinit var mealPlannerRepository: MealPlannerRepository*/

    override fun onReceive(context: Context, intent: Intent?) {
        /*if ((Intent.ACTION_BOOT_COMPLETED) == intent?.action) {
            mealPlannerRepository.setAlarm()
        }*/

        val param1 = intent?.getStringExtra("MESSAGE") ?: ""
        val param2 = intent?.getStringExtra("DESCRIPTION") ?: ""

        try {
            showNotification(context = context, title = param1, description = param2)
        } catch (ex: Exception) {
            Timber.tag("Receive Ex").d("onReceive: %s", ex.printStackTrace())
        }
    }
}

private fun showNotification(context: Context, title: String, description: String) {
    val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val channelId = "message_channel"
    val channelName = "message_name"

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        manager.createNotificationChannel(channel)
    }

    val builder = NotificationCompat.Builder(context, channelId)
        .setContentTitle(title)
        .setContentText(description)
        .setSmallIcon(R.drawable.fork_knife_bold)

    manager.notify(1, builder.build())
}
