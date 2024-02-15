package com.example.foodplanner.models;

import androidx.lifecycle.LiveData;

import com.example.foodplanner.auth.IAuthCallback;
import com.example.foodplanner.db.IMealsLocalDataSource;
import com.example.foodplanner.home.search.presenter.SearchedMealsCallback;
import com.example.foodplanner.network.ForYouNetworkCallback;
import com.example.foodplanner.network.IMealsRemoteDataSource;
import com.example.foodplanner.network.MealDetailsNetworkCallback;
import com.example.foodplanner.network.MealsNetworkCallback;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

public class Repository implements IRepository {

    private static Repository repository = null;
    private final IMealsRemoteDataSource remoteDataSource;
    private final IMealsLocalDataSource localDataSource;
    private final FirebaseAuth firebaseAuth;

    private Repository(FirebaseAuth firebaseAuth, IMealsRemoteDataSource remoteDataSource, IMealsLocalDataSource localDataSource) {
        this.remoteDataSource = remoteDataSource;
        this.firebaseAuth = firebaseAuth;
        this.localDataSource = localDataSource;
    }

    public static synchronized Repository getInstance(FirebaseAuth firebaseAuth, IMealsRemoteDataSource remoteDataSource, IMealsLocalDataSource localDataSource) {
        if (repository == null)
            repository = new Repository(firebaseAuth, remoteDataSource, localDataSource);
        return repository;
    }

    @Override
    public void getRemoteProducts(ForYouNetworkCallback forYouNetworkCallback) {
        remoteDataSource.requestSingleRandomMeal(forYouNetworkCallback);
    }

    @Override
    public void getRemoteCategories(ForYouNetworkCallback forYouNetworkCallback) {
        remoteDataSource.requestCategories(forYouNetworkCallback);
    }

    @Override
    public void getRemoteMealsByCategory(MealsNetworkCallback networkCallback, String category) {
        remoteDataSource.requestMealsByCategory(networkCallback, category);
    }

    @Override
    public void getRemoteMealDetails(MealDetailsNetworkCallback networkCallback, String mealID) {
        remoteDataSource.requestMealDetailsByID(networkCallback, mealID);
    }

    @Override
    public void getRemoteSearchedMeals(SearchedMealsCallback callback, String query) {
        remoteDataSource.requestSearchedMeals(callback, query);
    }

    @Override
    public void getRemoteAreas(ForYouNetworkCallback callback) {
        remoteDataSource.requestAreas(callback);
    }

    @Override
    public void getRemoteMealsByArea(MealsNetworkCallback networkCallback, String query) {
        remoteDataSource.requestMealsByArea(networkCallback, query);
    }

    @Override
    public void getRemoteIngredients(ForYouNetworkCallback networkCallback) {
        remoteDataSource.requestIngredients(networkCallback);
    }

    @Override
    public void getRemoteMealsByIngredient(MealsNetworkCallback networkCallback, String ingredient) {
        remoteDataSource.requestMealsByIngredient(networkCallback, ingredient);
    }

    @Override
    public void insert(Meal meal) {
        localDataSource.insertFavoriteMeal(meal);
    }

    @Override
    public Flowable<List<Meal>> getLocalMeals() {
        return localDataSource.getLocalFavMeals();
    }

    @Override
    public void delete(Meal meal) {
        localDataSource.deleteFavoriteMeal(meal);
    }

    @Override
    public Flowable<List<PlannedMeal>> getLocalPlannedMeals(int year, int month, int dayOfMonth) {
        return localDataSource.getLocalPlannedMeals(year, month, dayOfMonth);
    }

    @Override
    public void insertPlannedMeal(PlannedMeal plannedMeal) {
        localDataSource.insertPlannedMeal(plannedMeal);
    }

    @Override
    public void deletePlannedMeal(PlannedMeal plannedMeal) {
        localDataSource.deletePlannedMeal(plannedMeal);
    }

    @Override
    public void addPlannedMeal(PlannedMeal plannedMeals) {
        localDataSource.insertPlannedMeal(plannedMeals);
    }

    @Override
    public void registerWithEmailAndPassword(IAuthCallback callback, String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(callback.getActivity(), task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess();
                    } else {
                        callback.onFailure("The email address is already in use by another account.");
                    }
                });
    }


    @Override
    public void loginWithEmailAndPassword(IAuthCallback callback, String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(callback.getActivity(), task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess();
                    } else {
                        callback.onFailure("Invalid username or password");
                    }
                });
    }

}
