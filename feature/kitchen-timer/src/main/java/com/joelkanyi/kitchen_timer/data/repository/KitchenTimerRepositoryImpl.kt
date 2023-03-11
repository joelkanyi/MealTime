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
package com.joelkanyi.kitchen_timer.data.repository

import android.Manifest
import android.app.Notification
import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.CountDownTimer
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.joelkanyi.kitchen_timer.domain.model.KitchenTimer
import com.joelkanyi.kitchen_timer.domain.repository.KitchenTimerRepository
import com.kanyideveloper.mealtime.kitchen_timer.R
import kotlin.random.Random

class KitchenTimerRepositoryImpl(
    private val applicationContext: Context,
) : KitchenTimerRepository {

    private lateinit var countDownTimer: CountDownTimer
    private val _timeLeft = MutableLiveData<KitchenTimer>()
    private val finished = MutableLiveData<Boolean>()
    private val _isTimerRunning = MutableLiveData(false)
    private val _isPaused = MutableLiveData(false)
    private var originalTime: Long = 0L
    private var _percentage = MutableLiveData(0f)

    override val remainingTimerValue: LiveData<KitchenTimer> = _timeLeft
    override val isTimerRunning: LiveData<Boolean> = _isTimerRunning
    override val isPaused: LiveData<Boolean> = _isPaused

    private var _timeRemaining: Long = 0L
    override var timeRemaining: Long
        get() = _timeRemaining
        set(value) {
            originalTime = value
            _timeRemaining = value
        }
    override var percentage: LiveData<Float>
        get() = _percentage
        set(value) {
            _percentage.value = value.value
        }

    override fun startTimer() {

        removeBrokenChannel()
        initNotificationChannel()

        countDownTimer = object : CountDownTimer(timeRemaining, 1000) {
            override fun onFinish() {
                finished.value = true
                _isTimerRunning.value = false
                _timeRemaining = 0L
                _timeLeft.value = KitchenTimer()
                _percentage.value = 0f

                // When the timer finishes ...
                showNotification()
            }

            override fun onTick(millisUntilFinished: Long) {

                _isTimerRunning.value = true

                var seconds = (millisUntilFinished / 1000).toInt()
                val hours = seconds / (60 * 60)
                val tempMint = seconds - hours * 60 * 60
                val minutes = tempMint / 60
                seconds = tempMint - minutes * 60

                _timeLeft.value = KitchenTimer(
                    hour = String.format("%02d", hours),
                    minute = String.format("%02d", minutes),
                    second = String.format("%02d", seconds),
                    totalMilliseconds = timeRemaining,
                    elapsedTime = timeRemaining - millisUntilFinished,
                    remainingTime = millisUntilFinished,
                    sweepAnglePercentage = ((originalTime - millisUntilFinished) / (originalTime.toFloat()) * 360f)
                )

                _percentage.value = _timeLeft.value?.sweepAnglePercentage ?: 0f
            }

        }.start()
    }

    override fun stopTimer() {
        countDownTimer.cancel()
        _isTimerRunning.value = false
        _timeRemaining = 0L
        _timeLeft.value = KitchenTimer()
        _percentage.value = 0f
    }

    override fun pauseTimer() {
        countDownTimer.cancel()
        _isTimerRunning.value = false
        _timeRemaining = _timeLeft.value?.remainingTime ?: 0L
        _percentage.value = _timeLeft.value?.sweepAnglePercentage ?: 0f
    }

    private fun showNotification() {
        val builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(com.kanyideveloper.mealtime.core.R.drawable.fork_knife_bold)
            .setContentTitle("Kitchen Timer").setContentText("Time's up!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSound(Uri.parse("${ContentResolver.SCHEME_ANDROID_RESOURCE}://${applicationContext.packageName}/${R.raw.kitchen_timer_ringtone}"))
        val notificationManager = NotificationManagerCompat.from(applicationContext)
        if (ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        notificationManager.notify(Random.nextInt(), builder.build())
    }

    private fun initNotificationChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val channelName = "General"
        val channelDescription = "General notifications"
        val importance = NotificationManagerCompat.IMPORTANCE_HIGH
        val channel = NotificationChannelCompat.Builder(CHANNEL_ID, importance).apply {
            setName(channelName)
            setDescription(channelDescription)
            setSound(
                Uri.parse("${ContentResolver.SCHEME_ANDROID_RESOURCE}://${applicationContext.packageName}/raw/kitchen_timer_ringtone"),
                Notification.AUDIO_ATTRIBUTES_DEFAULT
            )
        }
        NotificationManagerCompat.from(applicationContext)
            .createNotificationChannel(channel.build())
    }

    private fun removeBrokenChannel() {
        NotificationManagerCompat.from(applicationContext)
            .deleteNotificationChannel(BROKEN_CHANNEL_ID)
    }

    companion object {
        const val BROKEN_CHANNEL_ID: String = "general_channel"
        const val CHANNEL_ID: String = "general_channel_new"
    }
}