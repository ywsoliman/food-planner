package com.example.foodplanner.db;

import com.example.foodplanner.models.Meal;
import com.example.foodplanner.models.PlannedMeal;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

public interface IMealsLocalDataSource {
    void insertFavoriteMeal(Meal meal);

    void insertAllFavoriteMeals(List<Meal> meals);

    void deleteFavoriteMeal(Meal meal);

    Flowable<List<Meal>> getLocalFavMeals();

    Flowable<List<PlannedMeal>> getLocalPlannedMeals();

    Flowable<List<PlannedMeal>> getLocalPlannedMeals(int year, int month, int dayOfMonth);

    void insertPlannedMeal(PlannedMeal plannedMeal);

    void insertAllPlannedMeals(List<PlannedMeal> meals);


    void deletePlannedMeal(PlannedMeal plannedMeal);
//
//    void deleteAllFavoriteMeals();
//
//    void deleteAllPlannedMeals();

    void replaceFavoriteMeals(List<Meal> meals);

    void replacePlannedMeals(List<PlannedMeal> plannedMeals);
}
