package com.example.foodplanner.auth.login.view;

import com.example.foodplanner.models.Meal;
import com.example.foodplanner.models.PlannedMeal;

import java.util.List;

public interface ISyncCallback {
    void onSuccessFetchingMealsFromFirebase(List<Meal> meals);

    void onSuccessFetchingPlannedFromFirebase(List<PlannedMeal> plannedMeals);
}
