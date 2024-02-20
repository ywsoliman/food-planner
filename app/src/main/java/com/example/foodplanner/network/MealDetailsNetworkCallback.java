package com.example.foodplanner.network;

import com.example.foodplanner.models.Meal;

public interface MealDetailsNetworkCallback {
    void onSuccess(Meal meal);

    void onFailure(String errorMsg);
}
