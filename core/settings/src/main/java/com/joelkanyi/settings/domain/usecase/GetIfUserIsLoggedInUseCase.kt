package com.joelkanyi.settings.domain.usecase

import com.joelkanyi.settings.domain.MealtimeSettings
import javax.inject.Inject

class GetIfUserIsLoggedInUseCase @Inject constructor(
    private val mealtimeSettings: MealtimeSettings
) {
    operator fun invoke() = mealtimeSettings.getUserId()
}