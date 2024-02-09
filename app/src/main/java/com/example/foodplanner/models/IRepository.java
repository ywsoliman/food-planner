package com.example.foodplanner.models;

import com.example.foodplanner.auth.IAuthenticate;
import com.example.foodplanner.network.MealDetailsNetworkCallback;
import com.example.foodplanner.network.MealsNetworkCallback;
import com.example.foodplanner.network.ForYouNetworkCallback;

public interface IRepository {
    void loginWithEmailAndPassword(IAuthenticate view, String email, String pass);

    void getRemoteProducts(ForYouNetworkCallback forYouNetworkCallback);

    void getRemoteCategories(ForYouNetworkCallback forYouNetworkCallback);

    void getRemoteMealsByCategory(MealsNetworkCallback networkCallback, String category);

    void getRemoteMealDetails(MealDetailsNetworkCallback networkCallback, String mealID);
}
