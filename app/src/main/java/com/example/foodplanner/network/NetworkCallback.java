package com.example.foodplanner.network;

import com.example.foodplanner.models.Meal;
import com.example.foodplanner.models.category.Category;

import java.util.List;

public interface NetworkCallback {
    void onSuccessSingleMeal(List<Meal> meals);

    void onFailure(String errorMsg);

    void onSuccessCategories(List<Category> categories);
}
