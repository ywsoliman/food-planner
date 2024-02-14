package com.example.foodplanner.db;

import androidx.lifecycle.LiveData;

import com.example.foodplanner.models.Meal;
import com.example.foodplanner.models.PlannedMeal;

import java.util.List;

public interface IMealsLocalDataSource {
    void insertFavoriteMeal(Meal meal);

    void deleteFavoriteMeal(Meal meal);

    LiveData<List<Meal>> getLocalMeals();

    LiveData<List<PlannedMeal>> getLocalPlannedMeals(int year, int month, int dayOfMonth);

    void insertPlannedMeal(PlannedMeal plannedMeal);

    void deletePlannedMeal(PlannedMeal plannedMeal);
}
