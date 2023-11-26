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
package com.joelkanyi.auth.presentation.landing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joelkanyi.auth.domain.usecase.SignInWithGoogleUseCase
import com.joelkanyi.analytics.domain.repository.AnalyticsUtil
import com.joelkanyi.analytics.domain.usecase.SetUserProfileUseCase
import com.joelkanyi.analytics.domain.usecase.TrackUserEventUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class LandingPageViewModel @Inject constructor(
    private val trackUserEventUseCase: com.joelkanyi.analytics.domain.usecase.TrackUserEventUseCase,
    private val setUserProfileUseCase: com.joelkanyi.analytics.domain.usecase.SetUserProfileUseCase,
    private val signInWithGoogleUseCase: SignInWithGoogleUseCase,
) : ViewModel() {
    fun trackUserEvent(eventName: String) {
        viewModelScope.launch {
            trackUserEventUseCase.invoke(eventName)
        }
    }

    fun setUserProfile(
        userId: String,
        userProperties: JSONObject?
    ) {
        viewModelScope.launch {
            setUserProfileUseCase(
                userID = userId,
                userProperties = userProperties
            )
        }
    }

    fun signInWithGoogle(idToken: String) {
        viewModelScope.launch {
            signInWithGoogleUseCase(
                idToken = idToken
            )
        }
    }
}
