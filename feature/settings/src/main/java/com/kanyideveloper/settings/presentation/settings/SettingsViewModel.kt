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
package com.kanyideveloper.settings.presentation.settings

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.joelkanyi.analytics.domain.usecase.TrackUserEventUseCase
import com.joelkanyi.common.state.TextFieldState
import com.joelkanyi.common.util.UiEvents
import com.kanyideveloper.settings.domain.usecase.SetCurrentThemeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val trackUserEventUseCase: TrackUserEventUseCase,
    private val setCurrentThemeUseCase: SetCurrentThemeUseCase,
) : ViewModel() {
    fun trackUserEvent(eventName: String) {
        trackUserEventUseCase.invoke(eventName)
    }

    private val _settingsUiState = MutableStateFlow(SettingsUiState())
    val settingsUiState = _settingsUiState.asStateFlow()

    private val _eventsFlow = MutableSharedFlow<UiEvents>()
    val eventsFlow = _eventsFlow

    fun setShowThemesDialogState() {
        _settingsUiState.update {
            it.copy(
                shouldShowThemesDialog = !it.shouldShowThemesDialog
            )
        }
    }

    fun setShowFeedbackDialogState() {
        _settingsUiState.update {
            it.copy(
                shouldShowFeedbackDialog = !it.shouldShowFeedbackDialog
            )
        }
    }


    fun setFeedbackState(
        value: String,
        error: String? = null
    ) {
        _settingsUiState.update {
            it.copy(
                feedbackState = it.feedbackState.copy(
                    text = value,
                    error = error
                )
            )
        }
    }

    fun updateTheme(themeValue: Int) {
        viewModelScope.launch {
            setCurrentThemeUseCase(themeValue)
            setShowThemesDialogState()
        }
    }


    private val _logoutState = mutableStateOf(LogoutState())
    val logoutState: State<LogoutState> = _logoutState
    fun logoutUser() {
        viewModelScope.launch {
            _logoutState.value = LogoutState(isLoading = true)
            Firebase.auth.signOut()
            val user = Firebase.auth.currentUser
            if (user == null) {
                _logoutState.value = LogoutState(isLoading = false)
                _eventsFlow.emit(UiEvents.NavigationEvent(""))
            }
        }
    }
}

data class LogoutState(
    val isLoading: Boolean = false
)
