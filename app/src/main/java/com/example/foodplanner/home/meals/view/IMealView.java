package com.example.foodplanner.home.meals.view;

import com.example.foodplanner.models.Meal;

import java.util.List;

public interface IMealView {
    void showMeals(List<Meal> mealList);

    void showFilteredMeals(List<Meal> filteredList);
}
