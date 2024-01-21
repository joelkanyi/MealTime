package com.kanyideveloper.settings.presentation.settings

import com.joelkanyi.common.state.TextFieldState

data class SettingsUiState(
    val shouldShowThemesDialog: Boolean = false,
    val shouldShowFeedbackDialog: Boolean = false,
    val feedbackState: TextFieldState = TextFieldState(),
)
