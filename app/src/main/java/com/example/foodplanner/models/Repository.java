package com.example.foodplanner.models;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.foodplanner.auth.IAuthCallback;
import com.example.foodplanner.auth.IAuthenticate;
import com.example.foodplanner.auth.register.view.IRegisterAuth;
import com.example.foodplanner.db.IMealsLocalDataSource;
import com.example.foodplanner.home.meals.presenter.MealsPresenter;
import com.example.foodplanner.home.search.presenter.SearchedMealsCallback;
import com.example.foodplanner.network.MealDetailsNetworkCallback;
import com.example.foodplanner.network.MealsNetworkCallback;
import com.example.foodplanner.network.IMealsRemoteDataSource;
import com.example.foodplanner.network.ForYouNetworkCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

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
        localDataSource.insert(meal);
    }

    @Override
    public LiveData<List<Meal>> getLocalMeals() {
        return localDataSource.getLocalMeals();
    }

    @Override
    public void delete(Meal meal) {
        localDataSource.delete(meal);
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
