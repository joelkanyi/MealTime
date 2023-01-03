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

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kanyideveloper.compose_ui.components.StandardToolbar
import com.kanyideveloper.compose_ui.theme.Theme
import com.kanyideveloper.mealtime.core.R
import com.ramcosta.composedestinations.annotation.Destination

interface SettingsNavigator {
    fun openSettings(showId: Long)
}

@Destination
@Composable
fun SettingsScreen(
    navigator: SettingsNavigator,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    var shouldShowSettingsDialog by remember {
        mutableStateOf(false)
    }

    Column(Modifier.fillMaxSize()) {
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

        LazyColumn(contentPadding = PaddingValues(16.dp)) {
            item {
                ThemeCard(
                    onClick = {
                        shouldShowSettingsDialog = !shouldShowSettingsDialog
                    }
                )
            }
        }
    }

    if (shouldShowSettingsDialog) {
        ThemeDialog(
            onDismiss = {
                shouldShowSettingsDialog = !shouldShowSettingsDialog
            },
            onSelectTheme = viewModel::updateTheme
        )
    }
}

@Composable
fun ThemeCard(
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            },
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.dark_mode),
                    contentDescription = null
                )
                Text(
                    text = "Change Your Theme",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Icon(
                painter = painterResource(id = R.drawable.chevron_right),
                contentDescription = null
            )
        }
    }
}

@Composable
fun ThemeDialog(
    onDismiss: () -> Unit,
    onSelectTheme: (Int) -> Unit
) {
    AlertDialog(
        containerColor = MaterialTheme.colorScheme.background,
        onDismissRequest = { onDismiss() },
        title = {
            Text(
                text = "Themes",
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                ThemeItem(
                    themeName = "Use System Settings",
                    themeValue = Theme.FOLLOW_SYSTEM.themeValue,
                    icon = R.drawable.settings_suggest,
                    onSelectTheme = onSelectTheme
                )
                ThemeItem(
                    themeName = "Light Mode",
                    themeValue = Theme.LIGHT_THEME.themeValue,
                    icon = R.drawable.light_mode,
                    onSelectTheme = onSelectTheme
                )
                ThemeItem(
                    themeName = "Dark Mode",
                    themeValue = Theme.DARK_THEME.themeValue,
                    icon = R.drawable.dark_mode,
                    onSelectTheme = onSelectTheme
                )
                ThemeItem(
                    themeName = "Material You",
                    themeValue = Theme.MATERIAL_YOU.themeValue,
                    icon = R.drawable.wallpaper,
                    onSelectTheme = onSelectTheme
                )
            }
        },
        confirmButton = {
            Text(
                text = "OK",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .clickable { onDismiss() }
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemeItem(
    themeName: String,
    themeValue: Int,
    icon: Int,
    onSelectTheme: (Int) -> Unit
) {
    Card(
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        onClick = {
            onSelectTheme(themeValue)
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null
            )
            Text(
                modifier = Modifier
                    .padding(12.dp),
                text = themeName,
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}
