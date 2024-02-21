package com.example.foodplanner.network;

import android.util.Log;
import android.util.Pair;

import com.example.foodplanner.auth.IAuthenticate;
import com.example.foodplanner.auth.register.view.IRegisterAuth;
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
import io.reactivex.rxjava3.core.Single;

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

    public Completable backupMealsToFirebase(List<Meal> favMeals, List<PlannedMeal> plannedMeals) {
        return Completable.create(emitter -> {
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
            emitter.onComplete();
        });
    }

    public Completable loginAsGuest(IAuthenticate view) {
        return Completable.create(emitter -> mAuth.signInAnonymously()
                .addOnCompleteListener(view.getActivity(), task -> {
                    if (task.isSuccessful()) {
                        emitter.onComplete();
                    } else {
                        emitter.onError(new Exception("Something went wrong."));
                    }
                }));
    }

    public Completable registerWithEmailAndPassword(IRegisterAuth view, String email, String password) {
        return Completable.create(emitter -> {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(view.getActivity(), task -> {
                        if (task.isSuccessful()) {
                            emitter.onComplete();
                        } else {
                            emitter.onError(new Exception("The email address is already in use by another account."));
                        }
                    });
        });
    }

    public Completable loginWithEmailAndPassword(IAuthenticate view, String email, String password) {
        return Completable.create(emitter -> mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(view.getActivity(), task -> {
                    if (task.isSuccessful()) {
                        emitter.onComplete();
                    } else {
                        emitter.onError(new Exception("Invalid username or password"));
                    }
                }));
    }

    public Single<Pair<List<Meal>, List<PlannedMeal>>> synchronizeMeals() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            return Single.error(new IllegalStateException("User is not logged in."));
        }
        String userId = currentUser.getUid();
        return Single.zip(
                getFavoriteMealsFromFirebase(userId),
                getPlannedMealsFromFirebase(userId),
                Pair::create
        );
    }

    private Single<List<PlannedMeal>> getPlannedMealsFromFirebase(String userId) {
        return Single.create(emitter -> reference.child(userId).child(PLANNED_MEALS).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e("firebase", "Error getting data", task.getException());
            } else {
                List<PlannedMeal> plannedMeals = new ArrayList<>();
                for (DataSnapshot ds : task.getResult().getChildren()) {
                    PlannedMeal meal = ds.getValue(PlannedMeal.class);
                    plannedMeals.add(meal);
                }
                emitter.onSuccess(plannedMeals);
            }
        }));
    }

    private Single<List<Meal>> getFavoriteMealsFromFirebase(String userId) {
        return Single.create(emitter -> reference.child(userId).child(FAV_MEALS).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e("firebase", "Error getting data", task.getException());
            } else {
                List<Meal> favMeals = new ArrayList<>();
                for (DataSnapshot ds : task.getResult().getChildren()) {
                    Meal meal = ds.getValue(Meal.class);
                    favMeals.add(meal);
                }
                emitter.onSuccess(favMeals);
            }
        }));

    }

    public boolean isGuest() {
        return mAuth.getCurrentUser().isAnonymous();
    }

}