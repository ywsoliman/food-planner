package com.example.foodplanner;

import android.util.Log;

import com.example.foodplanner.auth.IAuthCallback;
import com.example.foodplanner.auth.login.view.ISyncCallback;
import com.example.foodplanner.models.Meal;
import com.example.foodplanner.models.PlannedMeal;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Completable;

public class FirebaseDataManager {

    private static final String TAG = "FirebaseDataManager";
    private static final String FAV_MEALS = "favMeals";
    private static final String PLANNED_MEALS = "plannedMeals";
    private final static String USERS_COLLECTION = "users";
    private final FirebaseAuth mAuth;
    private final DatabaseReference reference;
    private static FirebaseDataManager instance = null;

    private FirebaseDataManager() {
        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference(USERS_COLLECTION);
    }

    public static synchronized FirebaseDataManager getInstance() {
        if (instance == null)
            instance = new FirebaseDataManager();
        return instance;
    }

    public void backupMealsToFirebase(List<Meal> favMeals, List<PlannedMeal> plannedMeals) {
        Log.i(TAG, "backupMealsToFirebase: ");
        for (Meal meal : favMeals) {
            Log.i(TAG, "backupMealsToFirebase: Favorite Meal id = " + meal.getIdMeal());
        }
        for (PlannedMeal meal : plannedMeals) {
            Log.i(TAG, "backupMealsToFirebase: Planned Meal id = " + meal.getMeal().getIdMeal());
        }
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            return;
        }
        String userId = currentUser.getUid();
        reference.child(userId).removeValue();
        for (Meal meal : favMeals) {
            reference.child(userId)
                    .child(FAV_MEALS)
                    .child(meal.getIdMeal())
                    .setValue(meal);
        }
        for (PlannedMeal meal : plannedMeals) {
            reference.child(userId)
                    .child(PLANNED_MEALS)
                    .child(meal.getMeal().getIdMeal())
                    .setValue(meal);
        }
    }

    public void loginAsGuest(IAuthCallback callback) {
        mAuth.signInAnonymously()
                .addOnCompleteListener(callback.getActivity(), task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess();
                    } else {
                        callback.onFailure("Something went wrong.");
                    }
                });
    }

    public void registerWithEmailAndPassword(IAuthCallback callback, String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(callback.getActivity(), task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess();
                    } else {
                        callback.onFailure("The email address is already in use by another account.");
                    }
                });
    }

    public void loginWithEmailAndPassword(IAuthCallback callback, String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(callback.getActivity(), task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess();
                    } else {
                        callback.onFailure("Invalid username or password");
                    }
                });
    }

    public void synchronizeMeals(ISyncCallback callback) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            return;
        }
        String userId = currentUser.getUid();
        Completable.fromAction(() -> getFavoriteMealsFromFirebase(callback, userId))
                .andThen(Completable.fromAction(() -> getPlannedMealsFromFirebase(callback, userId)))
                .subscribe();
    }

    private void getPlannedMealsFromFirebase(ISyncCallback callback, String userId) {
        reference.child(userId).child(PLANNED_MEALS).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e("firebase", "Error getting data", task.getException());
            } else {
                List<PlannedMeal> plannedMeals = new ArrayList<>();
                for (DataSnapshot ds : task.getResult().getChildren()) {
                    PlannedMeal meal = ds.getValue(PlannedMeal.class);
                    plannedMeals.add(meal);
                }
                callback.onSuccessFetchingPlannedFromFirebase(plannedMeals);
            }
        });
    }

    private void getFavoriteMealsFromFirebase(ISyncCallback callback, String userId) {

        reference.child(userId).child(FAV_MEALS).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e("firebase", "Error getting data", task.getException());
            } else {
                List<Meal> favMeals = new ArrayList<>();
                for (DataSnapshot ds : task.getResult().getChildren()) {
                    Meal meal = ds.getValue(Meal.class);
                    favMeals.add(meal);
                }
                callback.onSuccessFetchingMealsFromFirebase(favMeals);
            }
        });
    }

    public boolean isGuest() {
        return mAuth.getCurrentUser().isAnonymous();
    }

}