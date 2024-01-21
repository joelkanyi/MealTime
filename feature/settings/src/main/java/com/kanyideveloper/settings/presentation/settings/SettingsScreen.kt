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

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import com.joelkanyi.common.util.UiEvents
import com.joelkanyi.common.util.getAppVersionName
import com.kanyideveloper.mealtime.settings.R
import com.kanyideveloper.settings.domain.model.Setting
import com.kanyideveloper.settings.presentation.settings.components.FeedbackDialog
import com.kanyideveloper.settings.presentation.settings.components.SettingCard
import com.kanyideveloper.settings.presentation.settings.components.ThemesDialog
import com.ramcosta.composedestinations.annotation.Destination

@OptIn(ExperimentalComposeUiApi::class)
@Destination
@Composable
fun SettingsScreen(navigator: SettingsNavigator, viewModel: SettingsViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val snackbarHostState = remember { SnackbarHostState() }
    val settingsUiState by viewModel.settingsUiState.collectAsState()

    LaunchedEffect(key1 = true) {
        viewModel.eventsFlow.collect { event ->
            when (event) {
                is UiEvents.SnackbarEvent -> {
                    snackbarHostState.showSnackbar(message = event.message)
                }

                is UiEvents.NavigationEvent -> {
                    navigator.logout()
                }

                else -> {}
            }
        }
    }


    SettingsScreenContent(
        state = settingsUiState,
        onEvent = { event ->
            when (event) {
                SettingsUiEvents.ChangeThemeClicked -> {
                    viewModel.trackUserEvent("Themes Dialog Opened")
                    viewModel.setShowThemesDialogState()
                }

                SettingsUiEvents.ReportOrSuggestClicked -> {
                    viewModel.trackUserEvent("Feedback Dialog Opened")
                    viewModel.setShowFeedbackDialogState()
                }

                SettingsUiEvents.LogoutClicked -> {
                    viewModel.trackUserEvent("Logout Clicked")
                    viewModel.logoutUser()
                }

                SettingsUiEvents.RateUsClicked -> {
                    rateAppIntent(viewModel, context)
                }

                SettingsUiEvents.ShareAppClicked -> {
                    shareAppIntent(viewModel, context)
                }

                is SettingsUiEvents.SendFeedbackClicked -> {
                    keyboardController?.hide()
                    viewModel.setFeedbackState(
                        value = event.feedback,
                        error = if (event.feedback.isEmpty()) {
                            "Feedback cannot be empty"
                        } else {
                            null
                        }
                    )

                    if (event.feedback.isEmpty()) {
                        return@SettingsScreenContent
                    }

                    sendFeedbackIntent(event, viewModel, context)
                }

                is SettingsUiEvents.OnFeedbackChanged -> {
                    viewModel.setFeedbackState(event.feedback)
                }

                SettingsUiEvents.OnDismissThemesDialog -> {
                    viewModel.trackUserEvent("Themes Dialog Closed")
                    viewModel.setShowThemesDialogState()
                }

                is SettingsUiEvents.OnSelectTheme -> {
                    viewModel.trackUserEvent("Theme Selected: ${event.themeValue}")
                    viewModel.updateTheme(event.themeValue)
                }

                SettingsUiEvents.OnDismissFeedbackDialog -> {
                    viewModel.setShowFeedbackDialogState()
                }
            }
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        }
    )

}

@Composable
private fun SettingsScreenContent(
    state: SettingsUiState,
    onEvent: (SettingsUiEvents) -> Unit,
    snackbarHost: @Composable () -> Unit,
) {
    Scaffold(
        snackbarHost = { snackbarHost() },
        modifier = Modifier.fillMaxSize(),
        topBar = {
            com.joelkanyi.designsystem.components.StandardToolbar(
                navigate = {
                },
                title = {
                    Text(text = "Settings", fontSize = 16.sp)
                },
                showBackArrow = false,
                navActions = {
                }
            )
        }
    ) { paddingValues ->
        val context = LocalContext.current

        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.TopCenter),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(context.settingsOptions()) { setting ->
                    SettingCard(
                        title = setting.title,
                        icon = setting.icon,
                        onClick = { settingsOption ->
                            when (settingsOption) {
                                context.getString(R.string.change_your_theme) -> {
                                    onEvent(SettingsUiEvents.ChangeThemeClicked)
                                }

                                context.getString(R.string.suggest_or_report_anything) -> {
                                    onEvent(SettingsUiEvents.ReportOrSuggestClicked)
                                }

                                context.getString(R.string.rate_us_on_play_store) -> {
                                    onEvent(SettingsUiEvents.RateUsClicked)
                                }

                                context.getString(R.string.share_the_app_with_friends) -> {
                                    onEvent(SettingsUiEvents.ShareAppClicked)
                                }
                            }
                        }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        onClick = {
                            onEvent(SettingsUiEvents.LogoutClicked)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = com.joelkanyi.common.R.drawable.ic_logout),
                                contentDescription = stringResource(R.string.logout),
                                tint = MaterialTheme.colorScheme.onPrimary
                            )

                            Text(
                                modifier = Modifier.padding(8.dp),
                                text = stringResource(R.string.sign_out)
                            )
                        }
                    }
                }
            }

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.BottomCenter),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = stringResource(R.string.app_version, getAppVersionName(context)),
                    modifier = Modifier,
                    style = MaterialTheme.typography.titleSmall,
                    fontSize = 11.sp
                )

                Text(
                    text = stringResource(R.string.made_with_by_joel_kanyi),
                    modifier = Modifier,
                    style = MaterialTheme.typography.titleSmall,
                    fontSize = 12.sp
                )
            }
        }

        if (state.shouldShowThemesDialog) {
            ThemesDialog(
                onDismiss = {
                    onEvent(SettingsUiEvents.OnDismissThemesDialog)
                },
                onSelectTheme = {
                    onEvent(SettingsUiEvents.OnSelectTheme(it))
                }
            )
        }

        if (state.shouldShowFeedbackDialog) {
            FeedbackDialog(
                currentFeedbackString = state.feedbackState.text,
                isError = state.feedbackState.error != null,
                error = state.feedbackState.error,
                onDismiss = {
                    onEvent(SettingsUiEvents.OnDismissFeedbackDialog)
                },
                onFeedbackChange = { newValue ->
                    onEvent(SettingsUiEvents.OnFeedbackChanged(newValue))
                },
                onClickSend = { feedback ->
                    onEvent(SettingsUiEvents.SendFeedbackClicked(feedback))
                }
            )
        }
    }
}

private fun Context.settingsOptions() = listOf(
    Setting(
        title = getString(R.string.change_your_theme),
        icon = com.joelkanyi.common.R.drawable.dark_mode
    ),
    Setting(
        title = getString(R.string.suggest_or_report_anything),
        icon = com.joelkanyi.common.R.drawable.ic_feedback
    ),
    Setting(
        title = getString(R.string.rate_us_on_play_store),
        icon = com.joelkanyi.common.R.drawable.ic_star
    ),
    Setting(
        title = getString(R.string.share_the_app_with_friends),
        icon = com.joelkanyi.common.R.drawable.ic_share
    )
)

private fun rateAppIntent(
    viewModel: SettingsViewModel,
    context: Context
) {
    try {
        viewModel.trackUserEvent("Rate Us Clicked")
        val rateIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("market://details?id=" + context.packageName)
        )
        startActivity(context, rateIntent, null)
    } catch (e: Exception) {
        Toast.makeText(
            context,
            "Failed to find an app to rate with",
            Toast.LENGTH_SHORT
        )
            .show()
    }
}

private fun sendFeedbackIntent(
    event: SettingsUiEvents.SendFeedbackClicked,
    viewModel: SettingsViewModel,
    context: Context
) {
    try {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf("joelkanyi98@gmail.com"))
            putExtra(Intent.EXTRA_SUBJECT, "MEALTIME APP FEEDBACK")
            putExtra(Intent.EXTRA_TEXT, event.feedback)
            viewModel.trackUserEvent("Feedback Sent: $event.feedback")
        }
        context.startActivity(intent)

        viewModel.setShowFeedbackDialogState()
        viewModel.setFeedbackState("")
    } catch (e: Exception) {
        Toast.makeText(
            context,
            "No Email Application Found",
            Toast.LENGTH_SHORT
        )
            .show()
    }
}

private fun shareAppIntent(
    viewModel: SettingsViewModel,
    context: Context
) {
    try {
        viewModel.trackUserEvent("Share App Clicked")
        val appPackageName = context.packageName
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(
            Intent.EXTRA_TEXT,
            "Check out MealTime App on Play Store that helps your create your own recipes, search new ones online, create meal plans for a whole day, week or even a month: https://play.google.com/store/apps/details?id=$appPackageName"
        )
        sendIntent.type = "text/plain"
        context.startActivity(sendIntent)
    } catch (e: Exception) {
        Toast.makeText(
            context,
            "Failed to find an app to share with",
            Toast.LENGTH_SHORT
        )
            .show()
    }
}
