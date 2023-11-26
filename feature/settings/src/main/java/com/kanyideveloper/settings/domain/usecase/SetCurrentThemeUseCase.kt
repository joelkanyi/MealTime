package com.kanyideveloper.settings.domain.usecase

import com.joelkanyi.settings.domain.MealtimeSettings
import javax.inject.Inject

class SetCurrentThemeUseCase @Inject constructor(
    private val mealtimeSettings: MealtimeSettings
) {
    suspend operator fun invoke(theme: Int) {
        mealtimeSettings.saveTheme(theme)
    }
}