package com.kanyideveloper.analytics.domain.usecase

import com.kanyideveloper.analytics.domain.repository.AnalyticsUtil
import org.json.JSONObject
import javax.inject.Inject

class SetUserProfileUseCase @Inject constructor(
    private val analyticsUtil: AnalyticsUtil
) {
    operator fun invoke(userID: String, userProperties: JSONObject?) =
        analyticsUtil.setUserProfile(userID, userProperties)
}