package com.example.foodplanner.models;

import android.app.Activity;
import android.telecom.Call;

import com.example.foodplanner.auth.IAuthenticate;
import com.example.foodplanner.models.category.Category;
import com.example.foodplanner.network.IMealsRemoteDataSource;
import com.example.foodplanner.network.NetworkCallback;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class Repository implements IRepository {

    private static Repository repository = null;
    private final IMealsRemoteDataSource remoteDataSource;
    private final FirebaseAuth firebaseAuth;

    private Repository(FirebaseAuth firebaseAuth, IMealsRemoteDataSource remoteDataSource) {

        this.remoteDataSource = remoteDataSource;
        this.firebaseAuth = firebaseAuth;
    }

    public static synchronized Repository getInstance(FirebaseAuth firebaseAuth, IMealsRemoteDataSource remoteDataSource) {
        if (repository == null)
            repository = new Repository(firebaseAuth, remoteDataSource);
        return repository;
    }

    @Override
    public void getRemoteProducts(NetworkCallback networkCallback) {
        remoteDataSource.requestSingleRandomMeal(networkCallback);
    }

    @Override
    public void getRemoteCategories(NetworkCallback networkCallback) {
        remoteDataSource.requestCategories(networkCallback);
    }

    @Override
    public void loginWithEmailAndPassword(IAuthenticate view, String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) view, task -> {
                    if (task.isSuccessful()) {
                        view.onSuccess();
                    } else {
                        // If sign in fails, display a message to the user.
                        view.onFailure("Invalid username or password");
                    }
                });
    }

}
