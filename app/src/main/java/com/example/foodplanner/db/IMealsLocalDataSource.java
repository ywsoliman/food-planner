package com.example.foodplanner.db;

import com.example.foodplanner.models.Meal;
import com.example.foodplanner.models.PlannedMeal;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

public interface IMealsLocalDataSource {
    Completable insertFavoriteMeal(Meal meal);

    Completable deleteFavoriteMeal(Meal meal);

    Completable insertPlannedMeal(PlannedMeal plannedMeal);

    Completable deletePlannedMeal(PlannedMeal plannedMeal);

    Completable replaceFavoriteMeals(List<Meal> meals);

    Completable replacePlannedMeals(List<PlannedMeal> plannedMeals);

    Flowable<List<Meal>> getLocalFavMeals();

    Flowable<List<PlannedMeal>> getLocalPlannedMeals();

    Flowable<List<PlannedMeal>> getLocalPlannedMeals(int year, int month, int dayOfMonth);

}
