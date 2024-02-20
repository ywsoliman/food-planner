package com.example.foodplanner.models;

import android.content.Context;

import com.example.foodplanner.auth.IAuthCallback;
import com.example.foodplanner.home.search.presenter.SearchedMealsCallback;
import com.example.foodplanner.network.ForYouNetworkCallback;
import com.example.foodplanner.network.MealDetailsNetworkCallback;
import com.example.foodplanner.network.MealsNetworkCallback;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

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

    Flowable<List<Meal>> getLocalMeals();

    Flowable<List<PlannedMeal>> getLocalPlannedMeals(int year, int month, int dayOfMonth);

    void insertPlannedMeal(PlannedMeal plannedMeal);

    void deletePlannedMeal(PlannedMeal plannedMeal);

    void addPlannedMeal(PlannedMeal plannedMeals);

    void loginAsGuest(IAuthCallback callback);

//    void backupMeals(ForYouNetworkCallback callback, Context context);

}
