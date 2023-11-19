package com.joelkanyi.presentation

interface HomeNavigator {
    fun openHome()
    fun openMealDetails(mealId: String? = null)
    fun openAddMeal()
    fun popBackStack()
    fun onSearchClick()
    fun subscribe()
    fun openSettings()
}