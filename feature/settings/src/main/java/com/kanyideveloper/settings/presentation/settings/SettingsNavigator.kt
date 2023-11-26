package com.kanyideveloper.settings.presentation.settings

interface SettingsNavigator {
    fun openAllergiesScreen(editMealPlanPreference: Boolean)
    fun subscribe()
    fun logout()
}
