package com.example.foodplanner.network;

import android.content.Context;
import android.util.Log;
import android.util.Pair;

import com.example.foodplanner.FirebaseDataManager;
import com.example.foodplanner.auth.IAuthCallback;
import com.example.foodplanner.auth.login.view.ISyncCallback;
import com.example.foodplanner.db.MealsLocalDataSource;
import com.example.foodplanner.home.foryou.view.IBackupCallback;
import com.example.foodplanner.home.search.presenter.SearchedMealsCallback;

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MealsRemoteDataSource implements IMealsRemoteDataSource {

    private static final String TAG = "MealsRemoteDataSource";
    private static final String BASE_URL = "https://www.themealdb.com/api/json/v1/1/";
    private static MealsRemoteDataSource instance = null;
    private final FirebaseDataManager firebaseDataManager;
    private final MealsAPI mealsAPI;
    private Context context;

    private MealsRemoteDataSource(Context context) {

        this.context = context;

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
    public void requestSingleRandomMeal(ForYouNetworkCallback networkCallback) {

        mealsAPI.getSingleRandomMeal()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        meals -> {
                            networkCallback.onSuccessSingleMeal(meals.getMeals());
                            Log.i(TAG, "requestSingleRandomMeal: ");
                        },
                        error -> networkCallback.onFailure(error.getMessage())
                );

    }

    @Override
    public void requestCategories(ForYouNetworkCallback networkCallback) {

        mealsAPI.getCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        categories -> networkCallback.onSuccessCategories(categories.getCategories()),
                        error -> networkCallback.onFailure(error.getMessage())
                );

    }

    @Override
    public void requestMealsByCategory(MealsNetworkCallback networkCallback, String category) {

        mealsAPI.getMealsByCategory(category)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        meals -> networkCallback.onSuccess(meals.getMeals()),
                        error -> networkCallback.onFailure(error.getMessage())
                );

    }

    @Override
    public void requestMealDetailsByID(MealDetailsNetworkCallback networkCallback, String mealID) {
        mealsAPI.getMealDetailsByID(mealID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        meal -> networkCallback.onSuccess(meal.getMeals().get(0)),
                        error -> networkCallback.onFailure(error.getMessage())
                );
    }

    @Override
    public void requestSearchedMeals(SearchedMealsCallback callback, String query) {
        mealsAPI.getMealsBySearch(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        list -> callback.onSuccess(list.getMeals()),
                        error -> callback.onFailure(error.getMessage())
                );
    }

    @Override
    public void requestAreas(ForYouNetworkCallback networkCallback) {
        mealsAPI.getAreas()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        areas -> networkCallback.onSuccessAreas(areas.getMeals()),
                        error -> networkCallback.onFailure(error.getMessage())
                );
    }

    @Override
    public void requestMealsByArea(MealsNetworkCallback networkCallback, String query) {
        mealsAPI.getMealsByArea(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        areas -> networkCallback.onSuccess(areas.getMeals()),
                        error -> networkCallback.onFailure(error.getMessage())
                );
    }

    @Override
    public void requestIngredients(ForYouNetworkCallback networkCallback) {
        mealsAPI.getIngredients()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        list -> networkCallback.onSuccessIngredients(list.getMeals()),
                        error -> networkCallback.onFailure(error.getMessage())
                );
    }

    @Override
    public void requestMealsByIngredient(MealsNetworkCallback networkCallback, String ingredient) {
        mealsAPI.getMealsByIngredient(ingredient)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        list -> networkCallback.onSuccess(list.getMeals()),
                        error -> networkCallback.onFailure(error.getMessage())
                );
    }

    @Override
    public void loginAsGuest(IAuthCallback callback) {
        firebaseDataManager.loginAsGuest(callback);
    }

    @Override
    public void registerWithEmailAndPassword(IAuthCallback callback, String email, String password) {
        firebaseDataManager.registerWithEmailAndPassword(callback, email, password);
    }

    @Override
    public void loginWithEmailAndPassword(IAuthCallback callback, String email, String password) {
        firebaseDataManager.loginWithEmailAndPassword(callback, email, password);
    }

    @Override
    public void synchronizeMeals(ISyncCallback callback) {
        firebaseDataManager.synchronizeMeals(callback);
    }

    @Override
    public void backupMeals(IBackupCallback callback) {
        Flowable.zip(
                        MealsLocalDataSource.getInstance(context).getLocalFavMeals(),
                        MealsLocalDataSource.getInstance(context).getLocalPlannedMeals(),
                        Pair::create)
                .subscribe(pair -> {
                            firebaseDataManager.backupMealsToFirebase(pair.first, pair.second);
                            callback.onBackupSuccess();
                        },
                        throwable -> callback.onBackupFailure());
    }

}
