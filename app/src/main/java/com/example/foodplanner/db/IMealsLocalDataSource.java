package com.example.foodplanner.db;

import androidx.lifecycle.LiveData;

import com.example.foodplanner.models.Meal;
import com.example.foodplanner.models.PlannedMeal;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

public interface IMealsLocalDataSource {
    void insertFavoriteMeal(Meal meal);

    void deleteFavoriteMeal(Meal meal);

    Flowable<List<Meal>> getLocalFavMeals();

    Flowable<List<PlannedMeal>> getLocalPlannedMeals(int year, int month, int dayOfMonth);

    void insertPlannedMeal(PlannedMeal plannedMeal);

    void deletePlannedMeal(PlannedMeal plannedMeal);
}
