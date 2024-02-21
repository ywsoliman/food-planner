package com.example.foodplanner.models;

import android.util.Pair;

import com.example.foodplanner.auth.IAuthenticate;
import com.example.foodplanner.auth.register.view.IRegisterAuth;
import com.example.foodplanner.models.area.AreaList;
import com.example.foodplanner.models.category.CategoryList;
import com.example.foodplanner.models.ingredients.IngredientList;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public interface IRepository {
    Single<MealsList> getRemoteSingleMeal();

    Single<CategoryList> getRemoteCategories();

    Single<MealsList> getRemoteMealsByCategory(String category);

    Single<MealsList> getRemoteMealDetails(String mealID);

    Single<MealsList> getRemoteSearchedMeals(String query);

    Single<AreaList> getRemoteAreas();

    Single<MealsList> getRemoteMealsByArea(String query);

    Single<IngredientList> getRemoteIngredients();

    Single<MealsList> getRemoteMealsByIngredient(String ingredient);

    Completable insert(Meal meal);

    Completable delete(Meal meal);

    Flowable<List<Meal>> getLocalMeals();

    Flowable<List<PlannedMeal>> getLocalPlannedMeals(int year, int month, int dayOfMonth);

    Completable insertPlannedMeal(PlannedMeal plannedMeal);

    Completable deletePlannedMeal(PlannedMeal plannedMeal);

    Completable addPlannedMeal(PlannedMeal plannedMeals);

    Completable registerWithEmailAndPassword(IRegisterAuth view, String email, String password);

    Completable loginWithEmailAndPassword(IAuthenticate view, String email, String pass);

    Completable loginAsGuest(IAuthenticate view);

    Single<Pair<List<Meal>, List<PlannedMeal>>> synchronizeMeals();

    Completable replaceFavoriteMeals(List<Meal> meals);

    Completable replacePlannedMeals(List<PlannedMeal> plannedMeals);

    Completable backupMeals();

}
