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
package com.joelkanyi.auth.presentation.forgotpassword

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.joelkanyi.auth.presentation.AuthNavigator
import com.joelkanyi.auth.presentation.state.LoginState
import com.joelkanyi.common.util.UiEvents
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalComposeUiApi::class)
@Destination
@Composable
fun ForgotPasswordScreen(
    navigator: AuthNavigator,
    viewModel: ForgotPasswordViewModel = hiltViewModel()
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val snackbarHostState = remember { SnackbarHostState() }
    val forgotPasswordState = viewModel.forgotPasswordState.value

    LaunchedEffect(key1 = true) {
        viewModel.trackUserEvent("forgot_password_screen")
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UiEvents.SnackbarEvent -> {
                    snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }

                is UiEvents.NavigationEvent -> {
                    navigator.popBackStack()
                }
            }
        }
    }

    ForgotPasswordScreenContent(
        snackbarHostState = snackbarHostState,
        currentEmailText = viewModel.emailState.value.text,
        forgotPasswordState = forgotPasswordState,
        onCurrentEmailTextChange = {
            viewModel.setEmailState(it)
        },
        onClickSend = {
            keyboardController?.hide()
            viewModel.sendPasswordResetLink()
        },
        onClickNavigateBack = {
            navigator.popBackStack()
        }
    )
}

@Composable
private fun ForgotPasswordScreenContent(
    snackbarHostState: SnackbarHostState,
    currentEmailText: String,
    forgotPasswordState: LoginState,
    onCurrentEmailTextChange: (String) -> Unit,
    onClickSend: () -> Unit,
    onClickNavigateBack: () -> Unit,
) {
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            IconButton(onClick = onClickNavigateBack) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = null,
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp)
        ) {
            item {
                Column {
                    Text(
                        text = "Forgot Password",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = "Please enter an email address that you had registered with, so that we can send you a password reset link",
                        style = MaterialTheme.typography.labelMedium
                    )
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
            item {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = currentEmailText,
                    onValueChange = {
                        onCurrentEmailTextChange(it)
                    },
                    label = {
                        Text(text = "Email")
                    },
                    keyboardOptions = KeyboardOptions(
                        autoCorrect = true,
                        keyboardType = KeyboardType.Email
                    )
                )
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = onClickSend,
                    shape = RoundedCornerShape(8)
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        text = "Continue",
                        textAlign = TextAlign.Center
                    )
                }
            }

            item {
                if (forgotPasswordState.isLoading) {
                    Spacer(modifier = Modifier.height(32.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier
                        )
                    }
                }
            }
        }
    }
}
