package com.example.foodplanner.network;

public interface IMealsRemoteDataSource {

    void requestSingleRandomMeal(NetworkCallback networkCallback);

}
