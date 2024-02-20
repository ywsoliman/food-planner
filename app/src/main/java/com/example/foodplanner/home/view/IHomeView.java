package com.example.foodplanner.home.view;

import com.example.foodplanner.models.Meal;
import com.example.foodplanner.models.PlannedMeal;

import java.util.List;

public interface IHomeView {
    void onSuccessFetchingMealsFromFirebase(List<Meal> meals);

    void onSuccessFetchingPlannedFromFirebase(List<PlannedMeal> plannedMeals);

    void synchronizeMeals();

    void showNoInternetDialog();

    void showGuestDialog();
}
