//package com.example.foodplanner;
//
//import android.content.Context;
//import android.util.Log;
//
//import androidx.annotation.NonNull;
//
//import com.example.foodplanner.auth.login.ISyncCallback;
//import com.example.foodplanner.db.MealsLocalDataSource;
//import com.example.foodplanner.models.Meal;
//import com.example.foodplanner.models.PlannedMeal;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//
//public class FireStoreDataManager {
//    private static final String TAG = "FirestoreDataManager";
//    private static final String FAV_MEALS = "favMeals";
//    private static final String PLANNED_MEALS = "plannedMeals";
//    private final static String USERS_COLLECTION = "users";
//    private final FirebaseAuth mAuth;
//    private final DatabaseReference reference;
//    private static FireStoreDataManager instance = null;
//    private Context context;
//
//    private FireStoreDataManager(Context context) {
//        this.context = context;
//        mAuth = FirebaseAuth.getInstance();
//        reference = FirebaseDatabase.getInstance().getReference(USERS_COLLECTION);
//    }
//
//    public static synchronized FireStoreDataManager getInstance(Context context) {
//        if (instance == null)
//            instance = new FireStoreDataManager(context);
//        return instance;
//    }
//
//    public void backupMealsToFirebase(List<Meal> favMeals, List<PlannedMeal> plannedMeals) {
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if (currentUser == null) {
//            return;
//        }
//        String userId = currentUser.getUid();
//        reference.child(userId).removeValue();
//        for (Meal meal : favMeals) {
//            Log.i(TAG, "backupMealsToFirebase: meal = " + meal.getStrMeal());
//            reference.child(userId)
//                    .child(FAV_MEALS)
//                    .child(meal.getIdMeal())
//                    .setValue(meal);
//        }
//        for (PlannedMeal meal : plannedMeals) {
//            reference.child(userId)
//                    .child(PLANNED_MEALS)
//                    .child(meal.getMeal().getIdMeal())
//                    .setValue(meal);
//        }
//    }
//
//    public void getFavoriteMealsFromFirebase(ISyncCallback callback) {
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if (currentUser != null) {
//            List<Meal> favMeals = new ArrayList<>();
//            String userId = currentUser.getUid();
//            reference.child(userId).child(FAV_MEALS).addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    Log.i(TAG, "onDataChange: getFavoriteMealsFromFirebase");
//                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                        Meal meal = dataSnapshot.getValue(Meal.class);
//                        favMeals.add(meal);
//                    }
//                    callback.onSuccessFetchingMealsFromFirebase(favMeals);
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//                }
//            });
//        }
//    }
//
//    public void getPlannedMealsFromFirebase(ISyncCallback callback) {
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if (currentUser != null) {
//            List<PlannedMeal> plannedMeals = new ArrayList<>();
//            String userId = currentUser.getUid();
//            reference.child(userId).child(PLANNED_MEALS).addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    Log.i(TAG, "onDataChange: getFPlannedMealsFromFirebase");
//                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                        PlannedMeal plannedMeal = dataSnapshot.getValue(PlannedMeal.class);
//                        plannedMeals.add(plannedMeal);
//                    }
//                    callback.onSuccessFetchingPlannedFromFirebase(plannedMeals);
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//                }
//            });
//        }
//    }
//
////    public void getPlannedMealsFromFirebase() {
////        FirebaseUser currentUser = mAuth.getCurrentUser();
////        if (currentUser == null) {
////            return;
////        }
////        String userId = currentUser.getUid();
////        reference.child(userId).child(PLANNED_MEALS).addValueEventListener(new ValueEventListener() {
////            @Override
////            public void onDataChange(@NonNull DataSnapshot snapshot) {
////                Log.i(TAG, "onDataChange: getPlannedMealsFromFirebase");
////                List<PlannedMeal> plannedMeals = new ArrayList<>();
////                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
////                    PlannedMeal meal = dataSnapshot.getValue(PlannedMeal.class);
////                    plannedMeals.add(meal);
////                }
////                if (!plannedMeals.isEmpty()) {
////                    MealsLocalDataSource.getInstance(context).deleteAllPlannedMeals();
////                    MealsLocalDataSource.getInstance(context).insertAllPlannedMeals(plannedMeals);
////                }
////            }
////
////            @Override
////            public void onCancelled(@NonNull DatabaseError error) {
////            }
////        });
////    }
//
//
//    public boolean isGuest() {
//        return mAuth.getCurrentUser().isAnonymous();
//    }
//
//}