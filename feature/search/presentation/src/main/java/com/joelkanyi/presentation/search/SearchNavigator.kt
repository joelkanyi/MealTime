package com.joelkanyi.presentation.search

interface SearchNavigator {
    fun openMealDetails(mealId: String?)
    fun popBackStack()
}