package com.example.foodplanner.home.mealplan.view;

import com.example.foodplanner.models.PlannedMeal;

import java.util.List;

public interface IMealPlanView {
    void showPlannedMealsByDate(List<PlannedMeal> plannedMeals);

    void onDeleteFromCalendarSuccess(PlannedMeal plannedMeal);
}
