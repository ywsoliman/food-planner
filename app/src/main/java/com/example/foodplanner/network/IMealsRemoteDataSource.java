package com.example.foodplanner.network;

import android.util.Pair;

import com.example.foodplanner.auth.IAuthenticate;
import com.example.foodplanner.auth.register.view.IRegisterAuth;
import com.example.foodplanner.models.Meal;
import com.example.foodplanner.models.MealsList;
import com.example.foodplanner.models.PlannedMeal;
import com.example.foodplanner.models.area.AreaList;
import com.example.foodplanner.models.category.CategoryList;
import com.example.foodplanner.models.ingredients.IngredientList;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public interface IMealsRemoteDataSource {

    Single<MealsList> requestSingleRandomMeal();

    Single<CategoryList> requestCategories();

    Single<MealsList> requestMealsByCategory(String category);

    Single<MealsList> requestMealDetailsByID(String mealID);

    Single<MealsList> requestSearchedMeals(String query);

    Single<AreaList> requestAreas();

    Single<MealsList> requestMealsByArea(String query);

    Single<IngredientList> requestIngredients();

    Single<MealsList> requestMealsByIngredient(String ingredient);

    Completable loginAsGuest(IAuthenticate view);

    Completable registerWithEmailAndPassword(IRegisterAuth view, String email, String password);

    Completable loginWithEmailAndPassword(IAuthenticate view, String email, String password);

    Single<Pair<List<Meal>, List<PlannedMeal>>> synchronizeMeals();

    Completable backupMeals(List<Meal> favMeals, List<PlannedMeal> plannedMeals);
}
