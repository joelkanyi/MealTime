package com.joelkanyi.presentation

interface SearchNavigator {
    fun openMealDetails(mealId: String?)
    fun popBackStack()
}