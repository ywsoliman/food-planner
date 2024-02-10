package com.example.foodplanner.home.search.presenter;

import com.example.foodplanner.models.Meal;

import java.util.List;

public interface SearchedMealsCallback {

    void onSuccess(List<Meal> searchedMeals);
    void onFailure(String errorMsg);

}
