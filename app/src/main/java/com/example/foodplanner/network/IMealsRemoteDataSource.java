package com.example.foodplanner.network;

public interface IMealsRemoteDataSource {

    void requestSingleRandomMeal(ForYouNetworkCallback networkCallback);

    void requestCategories(ForYouNetworkCallback forYouNetworkCallback);

    void requestMealsByCategory(MealsNetworkCallback networkCallback, String category);

    void requestMealDetailsByID(MealDetailsNetworkCallback networkCallback, String mealID);
}
