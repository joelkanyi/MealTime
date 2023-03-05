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
package com.joelkanyi.kitchen_timer.domain.repository

import androidx.lifecycle.LiveData
import com.joelkanyi.kitchen_timer.domain.model.KitchenTimer

interface KitchenTimerRepository {
    val remainingTimerValue: LiveData<KitchenTimer>
    val isTimerRunning: LiveData<Boolean>
    val isPaused: LiveData<Boolean>
    var timeRemaining: Long
    var percentage: LiveData<Float>
    fun startTimer()
    fun stopTimer()
    fun pauseTimer()
}