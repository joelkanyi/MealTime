/*
 * Copyright 2023 Joel Kanyi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
