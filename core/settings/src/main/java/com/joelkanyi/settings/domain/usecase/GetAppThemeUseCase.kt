package com.joelkanyi.settings.domain.usecase

import com.joelkanyi.settings.domain.MealtimeSettings
import javax.inject.Inject

class GetAppThemeUseCase @Inject constructor(
    private val mealtimeSettings: MealtimeSettings
) {
    operator fun invoke() = mealtimeSettings.getTheme()
}