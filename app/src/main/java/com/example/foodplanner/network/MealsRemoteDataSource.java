package com.example.foodplanner.network;

import android.content.Context;
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

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MealsRemoteDataSource implements IMealsRemoteDataSource {

    private static final String BASE_URL = "https://www.themealdb.com/api/json/v1/1/";
    private static MealsRemoteDataSource instance = null;
    private final FirebaseDataManager firebaseDataManager;
    private final MealsAPI mealsAPI;

    private MealsRemoteDataSource(Context context) {

        int cacheSize = 10 * 1024 * 1024;
        Cache cache = new Cache(context.getCacheDir(), cacheSize);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .cache(cache)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .client(okHttpClient)
                .build();
        mealsAPI = retrofit.create(MealsAPI.class);

        firebaseDataManager = FirebaseDataManager.getInstance();
    }

    public static synchronized MealsRemoteDataSource getInstance(Context context) {
        if (instance == null)
            instance = new MealsRemoteDataSource(context);
        return instance;
    }

    @Override
    public Single<MealsList> requestSingleRandomMeal() {
        return mealsAPI.getSingleRandomMeal();
    }

    @Override
    public Single<CategoryList> requestCategories() {
        return mealsAPI.getCategories();
    }

    @Override
    public Single<MealsList> requestMealsByCategory(String category) {
        return mealsAPI.getMealsByCategory(category);
    }

    @Override
    public Single<MealsList> requestMealDetailsByID(String mealID) {
        return mealsAPI.getMealDetailsByID(mealID);
    }

    @Override
    public Single<MealsList> requestSearchedMeals(String query) {
        return mealsAPI.getMealsBySearch(query);
    }

    @Override
    public Single<AreaList> requestAreas() {
        return mealsAPI.getAreas();
    }

    @Override
    public Single<MealsList> requestMealsByArea(String query) {
        return mealsAPI.getMealsByArea(query);
    }

    @Override
    public Single<IngredientList> requestIngredients() {
        return mealsAPI.getIngredients();
    }

    @Override
    public Single<MealsList> requestMealsByIngredient(String ingredient) {
        return mealsAPI.getMealsByIngredient(ingredient);
    }

    @Override
    public Completable loginAsGuest(IAuthenticate view) {
        return firebaseDataManager.loginAsGuest(view);
    }

    @Override
    public Completable registerWithEmailAndPassword(IRegisterAuth view, String email, String password) {
        return firebaseDataManager.registerWithEmailAndPassword(view, email, password);
    }

    @Override
    public Completable loginWithEmailAndPassword(IAuthenticate view, String email, String password) {
        return firebaseDataManager.loginWithEmailAndPassword(view, email, password);
    }

    @Override
    public Single<Pair<List<Meal>, List<PlannedMeal>>> synchronizeMeals() {
        return firebaseDataManager.synchronizeMeals();
    }

    @Override
    public Completable backupMeals(List<Meal> favMeals, List<PlannedMeal> plannedMeals) {
        return firebaseDataManager.backupMealsToFirebase(favMeals, plannedMeals);
    }

}
