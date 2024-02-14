package com.example.foodplanner.models;

import androidx.lifecycle.LiveData;

import com.example.foodplanner.auth.IAuthCallback;
import com.example.foodplanner.home.search.presenter.SearchedMealsCallback;
import com.example.foodplanner.network.MealDetailsNetworkCallback;
import com.example.foodplanner.network.MealsNetworkCallback;
import com.example.foodplanner.network.ForYouNetworkCallback;

import java.util.List;

public interface IRepository {
    void loginWithEmailAndPassword(IAuthCallback callback, String email, String pass);

    void getRemoteProducts(ForYouNetworkCallback forYouNetworkCallback);

    void getRemoteCategories(ForYouNetworkCallback forYouNetworkCallback);

    void getRemoteMealsByCategory(MealsNetworkCallback networkCallback, String category);

    void getRemoteMealDetails(MealDetailsNetworkCallback networkCallback, String mealID);

    void registerWithEmailAndPassword(IAuthCallback callback, String email, String password);

    void getRemoteSearchedMeals(SearchedMealsCallback callback, String query);

    void getRemoteAreas(ForYouNetworkCallback callback);

    void getRemoteMealsByArea(MealsNetworkCallback networkCallback, String query);

    void getRemoteIngredients(ForYouNetworkCallback networkCallback);

    void getRemoteMealsByIngredient(MealsNetworkCallback networkCallback, String ingredient);

    void insert(Meal meal);

    void delete(Meal meal);

    LiveData<List<Meal>> getLocalMeals();

    LiveData<List<PlannedMeal>> getLocalPlannedMeals(int year, int month, int dayOfMonth);

    void insertPlannedMeal(PlannedMeal plannedMeal);

    void deletePlannedMeal(PlannedMeal plannedMeal);

    void addPlannedMeal(PlannedMeal plannedMeals);
}
