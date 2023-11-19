package com.kanyideveloper.analytics.domain.usecase

import com.kanyideveloper.analytics.domain.repository.AnalyticsUtil
import javax.inject.Inject

class TrackUserEventUseCase @Inject constructor(
    private val analyticsUtil: AnalyticsUtil
) {
    operator fun invoke(name: String) = analyticsUtil.trackUserEvent(name)
}