package com.example.foodplanner.home.meals.view;

import com.example.foodplanner.models.Meal;

import java.util.List;

public interface IMealView {

    void showMealsOfCategory(List<Meal> mealList);

}