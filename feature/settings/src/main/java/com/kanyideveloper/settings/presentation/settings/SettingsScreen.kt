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
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import com.joelkanyi.common.util.UiEvents
import com.joelkanyi.common.util.getAppVersionName
import com.kanyideveloper.settings.presentation.settings.components.FeedbackDialog
import com.kanyideveloper.settings.presentation.settings.components.SettingCard
import com.kanyideveloper.settings.presentation.settings.components.ThemesDialog
import com.ramcosta.composedestinations.annotation.Destination

@OptIn(ExperimentalComposeUiApi::class)
@Destination
@Composable
fun SettingsScreen(navigator: SettingsNavigator, viewModel: SettingsViewModel = hiltViewModel()) {
    val shouldShowThemesDialog = viewModel.shouldShowThemesDialog.value
    val shouldShowFeedbackDialog = viewModel.shouldShowFeedbackDialog.value
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val snackbarHostState = remember { SnackbarHostState() }

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

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
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

        if (shouldShowThemesDialog) {
            ThemesDialog(
                onDismiss = {
                    viewModel.trackUserEvent("Themes Dialog Closed")
                    viewModel.setShowThemesDialogState(
                        !viewModel.shouldShowThemesDialog.value
                    )
                },
                onSelectTheme = {
                    viewModel.trackUserEvent("Theme Selected: $it")
                    viewModel.updateTheme(it)
                }
            )
        }

        if (shouldShowFeedbackDialog) {
            viewModel.trackUserEvent("Feedback Dialog Opened")
            FeedbackDialog(
                currentFeedbackString = viewModel.feedback.value.text,
                isError = viewModel.feedback.value.error != null,
                error = viewModel.feedback.value.error,
                onDismiss = {
                    viewModel.setShowFeedbackDialogState(
                        !viewModel.shouldShowFeedbackDialog.value
                    )
                },
                onFeedbackChange = { newValue ->
                    viewModel.setFeedbackState(newValue)
                },
                onClickSend = { feedback ->
                    keyboardController?.hide()
                    viewModel.validateFeedbackTextfield(message = feedback)

                    if (feedback.isEmpty()) {
                        return@FeedbackDialog
                    }

                    try {
                        val intent = Intent(Intent.ACTION_SENDTO).apply {
                            data = Uri.parse("mailto:")
                            putExtra(Intent.EXTRA_EMAIL, arrayOf("joelkanyi98@gmail.com"))
                            putExtra(Intent.EXTRA_SUBJECT, "MEALTIME APP FEEDBACK")
                            putExtra(Intent.EXTRA_TEXT, feedback)
                            viewModel.trackUserEvent("Feedback Sent: $feedback")
                        }
                        context.startActivity(intent)

                        viewModel.setShowFeedbackDialogState(false)
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
            )
        }


        SettingsScreenContent(
            paddingValues = paddingValues,
            navigator = navigator,
            context = context,
            logoutState = viewModel.logoutState.value,
            onChangeTheme = {
                viewModel.trackUserEvent("Theme Dialog Opened")
                viewModel.setShowThemesDialogState(
                    !viewModel.shouldShowThemesDialog.value
                )
            },
            onReportOrSuggest = {
                viewModel.trackUserEvent("Feedback Dialog Opened")
                viewModel.setShowFeedbackDialogState(
                    !viewModel.shouldShowFeedbackDialog.value
                )
            },
            logout = {
                viewModel.trackUserEvent("Logout Clicked")
                viewModel.logoutUser()
            },
            trackUserEvent = { event ->
                viewModel.trackUserEvent(event)
            }
        )
    }
}

@Composable
private fun SettingsScreenContent(
    paddingValues: PaddingValues,
    navigator: SettingsNavigator,
    context: Context,
    onChangeTheme: () -> Unit,
    onReportOrSuggest: () -> Unit,
    logout: () -> Unit,
    logoutState: LogoutState,
    trackUserEvent: (String) -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            contentPadding = paddingValues,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(settingsOptions) { setting ->
                SettingCard(
                    title = setting.title,
                    icon = setting.icon,
                    onClick = { settingsOption ->
                        when (settingsOption) {
                            "Change Your Theme" -> {
                                onChangeTheme()
                            }

                            "Edit Meal Plan Preferences" -> {
                                navigator.openAllergiesScreen(editMealPlanPreference = true)
                            }

                            "Suggest or Report Anything" -> {
                                onReportOrSuggest()
                            }

                            "Rate Us on Play Store" -> {
                                trackUserEvent("Rate Us Clicked")
                                val rateIntent = Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("market://details?id=" + context.packageName)
                                )
                                startActivity(context, rateIntent, null)
                            }

                            "Share the App with Friends" -> {
                                trackUserEvent("Share App Clicked")
                                val appPackageName = context.packageName
                                val sendIntent = Intent()
                                sendIntent.action = Intent.ACTION_SEND
                                sendIntent.putExtra(
                                    Intent.EXTRA_TEXT,
                                    "Check out MealTime App on Play Store that helps your create your own recipes, search new ones online, create meal plans for a whole day, week or even a month: https://play.google.com/store/apps/details?id=$appPackageName"
                                )
                                sendIntent.type = "text/plain"
                                context.startActivity(sendIntent)
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
                        logout()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    if (logoutState.isLoading) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 1.dp
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = com.joelkanyi.common.R.drawable.ic_logout),
                            contentDescription = "Logout",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )

                        Text(
                            modifier = Modifier.padding(8.dp),
                            text = "Sign Out"
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
                text = "App Version: ${getAppVersionName(context)}",
                modifier = Modifier,
                style = MaterialTheme.typography.titleSmall,
                fontSize = 11.sp
            )

            Text(
                text = "Made with ❤️ by Joel Kanyi 🇰🇪",
                modifier = Modifier,
                style = MaterialTheme.typography.titleSmall,
                fontSize = 12.sp
            )
        }
    }
}

data class Setting(
    val title: String,
    val icon: Int
)

private val settingsOptions = listOf(
    Setting(
        title = "Change Your Theme",
        icon = com.joelkanyi.common.R.drawable.dark_mode
    ),
    Setting(
        title = "Edit Meal Plan Preferences",
        icon = com.joelkanyi.common.R.drawable.ic_plan_edit
    ),
    Setting(
        title = "Suggest or Report Anything",
        icon = com.joelkanyi.common.R.drawable.ic_feedback
    ),
    Setting(
        title = "Upgrade to Premium",
        icon = com.joelkanyi.common.R.drawable.ic_premium
    ),
    Setting(
        title = "Rate Us on Play Store",
        icon = com.joelkanyi.common.R.drawable.ic_star
    ),
    Setting(
        title = "Share the App with Friends",
        icon = com.joelkanyi.common.R.drawable.ic_share
    )
)
