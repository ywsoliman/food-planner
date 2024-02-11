package com.example.foodplanner.network;

import com.example.foodplanner.home.search.presenter.SearchedMealsCallback;

public interface IMealsRemoteDataSource {

    void requestSingleRandomMeal(ForYouNetworkCallback networkCallback);

    void requestCategories(ForYouNetworkCallback forYouNetworkCallback);

    void requestMealsByCategory(MealsNetworkCallback networkCallback, String category);

    void requestMealDetailsByID(MealDetailsNetworkCallback networkCallback, String mealID);

    void requestSearchedMeals(SearchedMealsCallback callback, String query);

    void requestAreas(ForYouNetworkCallback callback);

    void requestMealsByArea(MealsNetworkCallback networkCallback, String query);

    void requestIngredients(ForYouNetworkCallback networkCallback);

    void requestMealsByIngredient(MealsNetworkCallback networkCallback, String ingredient);
}
