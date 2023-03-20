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
package com.joelkanyi.kitchen_timer.domain.model

data class KitchenTimer(
    val hour: String? = "00",
    val minute: String = "00",
    val second: String = "00",
    val totalMilliseconds: Long = 0L,
    val elapsedTime: Long = 0L,
    val remainingTime: Long = 0L,
    val sweepAnglePercentage: Float = 0f
)
