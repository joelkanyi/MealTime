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

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.CountDownTimer
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.joelkanyi.kitchen_timer.domain.model.KitchenTimer
import com.joelkanyi.kitchen_timer.domain.repository.KitchenTimerRepository


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
        countDownTimer = object : CountDownTimer(timeRemaining, 1000) {
            override fun onFinish() {
                finished.value = true
                _isTimerRunning.value = false

                // When the timer finishes, trigger the notification
                val notificationManager =
                    applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                val notificationBuilder =
                    NotificationCompat.Builder(applicationContext, "your_channel_id")
                        .setContentTitle("Timer")
                        .setContentText("Time's up!")
                        .setSmallIcon(com.kanyideveloper.mealtime.core.R.drawable.fork_knife_bold)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setCategory(NotificationCompat.CATEGORY_ALARM)
                        .setOngoing(true)
                        //.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    // Create notification channel for Android O and later
                    val channel = NotificationChannel(
                        "your_channel_id",
                        "Your Channel Name",
                        NotificationManager.IMPORTANCE_HIGH
                    )
                    notificationManager.createNotificationChannel(channel)
                }

                notificationManager.notify(0, notificationBuilder.build())
                val notification: Uri =
                    RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)

                val player: MediaPlayer = MediaPlayer.create(applicationContext, notification)
                player.isLooping = true
                player.start()
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
}