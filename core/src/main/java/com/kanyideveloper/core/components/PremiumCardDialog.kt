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
package com.kanyideveloper.core.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kanyideveloper.mealtime.core.R

@Composable
fun PremiumCard(onDismiss: () -> Unit, onClickSubscribe: () -> Unit) {
    AlertDialog(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        containerColor = MaterialTheme.colorScheme.background,
        onDismissRequest = onDismiss,
        tonalElevation = 12.dp,
        title = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_premium),
                    contentDescription = "Premium Icon",
                    tint = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Premium",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        },
        text = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                item {
                    Text(
                        text = "Get Started with MealTime Premium",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
                items(premiumFeatures) { premium ->
                    PremiumFeaturesCard(premium = premium)
                }

                item {
                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        modifier = Modifier
                            .fillMaxWidth(),
                        onClick = onClickSubscribe,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            text = "Subscribe",
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {}
    )
}

@Composable
fun PremiumFeaturesCard(premium: Premium) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(.5.dp, MaterialTheme.colorScheme.primary.copy(alpha = .3f)),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = premium.icon),
                contentDescription = premium.title,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Column {
                Text(
                    text = premium.title,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = premium.description,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

data class Premium(
    val title: String,
    val description: String,
    val icon: Int
)

private val premiumFeatures = listOf(
    Premium(
        title = "Automatic Backup",
        description = "Automatically backup your data to the cloud so that you can access it from any device.",
        icon = R.drawable.ic_cloud
    ),
    Premium(
        title = "Kitchen Timer",
        description = "Set a timer for your meal and get notified when it's done.",
        icon = R.drawable.ic_timer
    ),
    Premium(
        title = "No Ads",
        description = "Enjoy a clean and ad-free experience.",
        icon = R.drawable.ic_ad
    ),
    Premium(
        title = "Selling Recipes",
        description = "Sell your recipes to other users and earn money. (Coming Soon)",
        icon = R.drawable.ic_sell
    ),
    Premium(
        title = "View Others People's Recipes",
        description = "View other people's recipes and get inspired. (Coming Soon)",
        icon = R.drawable.ic_meal_book
    )
)
