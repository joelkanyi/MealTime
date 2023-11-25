package com.joelkanyi.presentation.mealplanner

interface MealPlannerNavigator {
    fun popBackStack()
    fun openAllergiesScreen(editMealPlanPreference: Boolean = false)
    fun openNoOfPeopleScreen(allergies: String, editMealPlanPreference: Boolean = false)
    fun openMealTypesScreen(
        allergies: String,
        noOfPeople: String,
        editMealPlanPreference: Boolean = false
    )

    fun openMealPlanner()
    fun openMealDetails(mealId: String?)
    fun navigateToSettings()
}