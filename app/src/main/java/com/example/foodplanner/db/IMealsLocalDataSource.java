package com.example.foodplanner.db;

import androidx.lifecycle.LiveData;

import com.example.foodplanner.models.Meal;

import java.util.List;

public interface IMealsLocalDataSource {
    void insert(Meal meal);

    void delete(Meal meal);

    LiveData<List<Meal>> getLocalMeals();
}
