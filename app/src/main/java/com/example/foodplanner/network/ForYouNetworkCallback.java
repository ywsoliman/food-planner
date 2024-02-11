package com.example.foodplanner.network;

import com.example.foodplanner.models.Meal;
import com.example.foodplanner.models.area.Area;
import com.example.foodplanner.models.category.Category;
import com.example.foodplanner.models.ingredients.Ingredient;

import java.util.List;

public interface ForYouNetworkCallback {
    void onSuccessSingleMeal(List<Meal> meals);

    void onFailure(String errorMsg);

    void onSuccessCategories(List<Category> categories);

    void onSuccessAreas(List<Area> meals);

    void onSuccessIngredients(List<Ingredient> meals);
}
