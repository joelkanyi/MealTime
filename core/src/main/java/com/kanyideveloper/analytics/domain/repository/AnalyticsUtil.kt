package com.kanyideveloper.analytics.domain.repository

import org.json.JSONObject

interface AnalyticsUtil {
    fun trackUserEvent(eventName: String, eventProperties: JSONObject? = null)
    fun setUserProfile(userID: String, userProperties: JSONObject?)
}