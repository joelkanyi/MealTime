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
package com.joelkanyi.auth.presentation.signin

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joelkanyi.auth.domain.repository.AuthRepository
import com.joelkanyi.auth.presentation.state.LoginState
import com.kanyideveloper.core.state.TextFieldState
import com.kanyideveloper.core.util.Resource
import com.kanyideveloper.core.util.UiEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _loginState = mutableStateOf(LoginState())
    val loginState: State<LoginState> = _loginState

    private val _emailState = mutableStateOf(TextFieldState())
    val emailState: State<TextFieldState> = _emailState
    fun setUsername(value: String) {
        _emailState.value = emailState.value.copy(text = value)
    }

    private val _passwordState = mutableStateOf(TextFieldState())
    val passwordState: State<TextFieldState> = _passwordState
    fun setPassword(value: String) {
        _passwordState.value = _passwordState.value.copy(text = value)
    }

    private val _eventFlow = MutableSharedFlow<UiEvents>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun loginUser() {
        viewModelScope.launch {
            if (emailState.value.text.isEmpty()) {
                _eventFlow.emit(
                    UiEvents.SnackbarEvent(message = "Please input your email")
                )
                return@launch
            }

            if (passwordState.value.text.isEmpty()) {
                _eventFlow.emit(
                    UiEvents.SnackbarEvent(message = "Please input your password")
                )
                return@launch
            }

            _loginState.value = loginState.value.copy(isLoading = true)

            when (
                val result = authRepository.loginUser(
                    email = emailState.value.text.trim(),
                    password = passwordState.value.text.trim()
                )
            ) {
                is Resource.Error -> {
                    _loginState.value = loginState.value.copy(
                        isLoading = false,
                        error = result.message ?: "Unknown Error Occurred"
                    )

                    _eventFlow.emit(
                        UiEvents.SnackbarEvent(message = result.message ?: "Unknown Error Occurred")
                    )
                }

                is Resource.Success -> {
                    _loginState.value = loginState.value.copy(
                        isLoading = false,
                        data = result.data
                    )
                    _eventFlow.emit(
                        UiEvents.SnackbarEvent(message = "Login Successfully")
                    )
                    _eventFlow.emit(
                        UiEvents.NavigationEvent("")
                    )
                }

                else -> {
                    loginState
                }
            }
        }
    }
}
