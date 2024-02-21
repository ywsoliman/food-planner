package com.example.foodplanner.models;

import android.util.Pair;

import com.example.foodplanner.auth.IAuthenticate;
import com.example.foodplanner.auth.register.view.IRegisterAuth;
import com.example.foodplanner.db.IMealsLocalDataSource;
import com.example.foodplanner.models.area.AreaList;
import com.example.foodplanner.models.category.CategoryList;
import com.example.foodplanner.models.ingredients.IngredientList;
import com.example.foodplanner.network.IMealsRemoteDataSource;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public class Repository implements IRepository {

    private static Repository repository = null;
    private final IMealsRemoteDataSource remoteDataSource;
    private final IMealsLocalDataSource localDataSource;

    private Repository(IMealsRemoteDataSource remoteDataSource, IMealsLocalDataSource localDataSource) {
        this.remoteDataSource = remoteDataSource;
        this.localDataSource = localDataSource;
    }

    public static synchronized Repository getInstance(IMealsRemoteDataSource remoteDataSource, IMealsLocalDataSource localDataSource) {
        if (repository == null)
            repository = new Repository(remoteDataSource, localDataSource);
        return repository;
    }

    @Override
    public Single<MealsList> getRemoteSingleMeal() {
        return remoteDataSource.requestSingleRandomMeal();
    }

    @Override
    public Single<CategoryList> getRemoteCategories() {
        return remoteDataSource.requestCategories();
    }

    @Override
    public Single<MealsList> getRemoteMealsByCategory(String category) {
        return remoteDataSource.requestMealsByCategory(category);
    }

    @Override
    public Single<MealsList> getRemoteMealDetails(String mealID) {
        return remoteDataSource.requestMealDetailsByID(mealID);
    }

    @Override
    public Single<MealsList> getRemoteSearchedMeals(String query) {
        return remoteDataSource.requestSearchedMeals(query);
    }

    @Override
    public Single<AreaList> getRemoteAreas() {
        return remoteDataSource.requestAreas();
    }

    @Override
    public Single<MealsList> getRemoteMealsByArea(String query) {
        return remoteDataSource.requestMealsByArea(query);
    }

    @Override
    public Single<IngredientList> getRemoteIngredients() {
        return remoteDataSource.requestIngredients();
    }

    @Override
    public Single<MealsList> getRemoteMealsByIngredient(String ingredient) {
        return remoteDataSource.requestMealsByIngredient(ingredient);
    }

    @Override
    public Completable insert(Meal meal) {
        return localDataSource.insertFavoriteMeal(meal);
    }

    @Override
    public Flowable<List<Meal>> getLocalMeals() {
        return localDataSource.getLocalFavMeals();
    }

    @Override
    public Completable delete(Meal meal) {
        return localDataSource.deleteFavoriteMeal(meal);
    }

    @Override
    public Flowable<List<PlannedMeal>> getLocalPlannedMeals(int year, int month, int dayOfMonth) {
        return localDataSource.getLocalPlannedMeals(year, month, dayOfMonth);
    }

    @Override
    public Completable insertPlannedMeal(PlannedMeal plannedMeal) {
        return localDataSource.insertPlannedMeal(plannedMeal);
    }

    @Override
    public Completable deletePlannedMeal(PlannedMeal plannedMeal) {
        return localDataSource.deletePlannedMeal(plannedMeal);
    }

    @Override
    public Completable addPlannedMeal(PlannedMeal plannedMeals) {
        return localDataSource.insertPlannedMeal(plannedMeals);
    }

    @Override
    public Completable loginAsGuest(IAuthenticate view) {
        return remoteDataSource.loginAsGuest(view);
    }

    @Override
    public Single<Pair<List<Meal>, List<PlannedMeal>>> synchronizeMeals() {
        return remoteDataSource.synchronizeMeals();
    }

    @Override
    public Completable replaceFavoriteMeals(List<Meal> meals) {
        return localDataSource.replaceFavoriteMeals(meals);
    }

    @Override
    public Completable replacePlannedMeals(List<PlannedMeal> plannedMeals) {
        return localDataSource.replacePlannedMeals(plannedMeals);
    }

    @Override
    public Completable backupMeals() {
        return remoteDataSource.backupMeals(localDataSource.getLocalFavMeals().blockingFirst(), localDataSource.getLocalPlannedMeals().blockingFirst());
    }

    @Override
    public Completable registerWithEmailAndPassword(IRegisterAuth view, String email, String password) {
        return remoteDataSource.registerWithEmailAndPassword(view, email, password);
    }


    @Override
    public Completable loginWithEmailAndPassword(IAuthenticate view, String email, String password) {
        return remoteDataSource.loginWithEmailAndPassword(view, email, password);
    }

}
