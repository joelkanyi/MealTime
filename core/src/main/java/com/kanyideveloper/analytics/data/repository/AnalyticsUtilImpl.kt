package com.kanyideveloper.analytics.data.repository

import com.kanyideveloper.analytics.domain.repository.AnalyticsUtil
import com.mixpanel.android.mpmetrics.MixpanelAPI
import org.json.JSONObject

class AnalyticsUtilImpl(
    private val mixpanelAPI: MixpanelAPI
) : AnalyticsUtil {
    override fun trackUserEvent(eventName: String, eventProperties: JSONObject?) {
        //if (BuildConfig.BUILD_TYPE != "release") return
        eventProperties
            ?.let { mixpanelAPI.track(eventName, eventProperties) }
            ?: mixpanelAPI.track(eventName)
    }

    override fun setUserProfile(userID: String, userProperties: JSONObject?) {
        userProperties
            ?.let {
                mixpanelAPI.identify(userID)
                mixpanelAPI.people.set(it)
            } ?: mixpanelAPI.identify(userID)
    }
}