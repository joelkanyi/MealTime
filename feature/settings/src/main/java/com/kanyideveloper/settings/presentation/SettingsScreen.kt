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
package com.kanyideveloper.settings.presentation

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import com.kanyideveloper.compose_ui.components.StandardToolbar
import com.kanyideveloper.core.util.UiEvents
import com.kanyideveloper.mealtime.core.R
import com.kanyideveloper.settings.presentation.components.FeedbackDialog
import com.kanyideveloper.settings.presentation.components.SettingCard
import com.kanyideveloper.settings.presentation.components.ThemesDialog
import com.ramcosta.composedestinations.annotation.Destination

interface SettingsNavigator {
    fun openSettings(showId: Long)
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Destination
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {
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
                else -> {}
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier.fillMaxSize(),
        topBar = {
            StandardToolbar(
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
                    viewModel.setShowThemesDialogState(!viewModel.shouldShowThemesDialog.value)
                },
                onSelectTheme = viewModel::updateTheme
            )
        }

        if (shouldShowFeedbackDialog) {
            FeedbackDialog(
                currentFeedbackString = viewModel.feedback.value.text,
                isError = viewModel.feedback.value.error != null,
                error = viewModel.feedback.value.error,
                onDismiss = {
                    viewModel.setShowFeedbackDialogState(!viewModel.shouldShowFeedbackDialog.value)
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
                        }
                        context.startActivity(intent)

                        viewModel.setShowFeedbackDialogState(false)
                        viewModel.setFeedbackState("")
                    } catch (e: Exception) {
                        Toast.makeText(context, "No Email Application Found", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            )
        }

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
                                viewModel.setShowThemesDialogState(
                                    !viewModel.shouldShowThemesDialog.value
                                )
                            }
                            "Suggest or Report Anything" -> {
                                viewModel.setShowFeedbackDialogState(
                                    !viewModel.shouldShowFeedbackDialog.value
                                )
                            }
                            "Rate Us on Play Store" -> {
                                val rateIntent = Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("market://details?id=" + context.packageName)
                                )
                                startActivity(context, rateIntent, null)
                            }
                            "Share the App with Friends" -> {
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
        icon = R.drawable.dark_mode
    ),
    Setting(
        title = "Suggest or Report Anything",
        icon = R.drawable.ic_feedback
    ),
    Setting(
        title = "Rate Us on Play Store",
        icon = R.drawable.ic_star
    ),
    Setting(
        title = "Share the App with Friends",
        icon = R.drawable.ic_share
    )
)