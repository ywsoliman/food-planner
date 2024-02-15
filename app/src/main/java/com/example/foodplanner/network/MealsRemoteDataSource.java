package com.example.foodplanner.network;

import android.content.Context;

import com.example.foodplanner.home.search.presenter.SearchedMealsCallback;

import java.util.concurrent.TimeUnit;

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MealsRemoteDataSource implements IMealsRemoteDataSource {

    private static final String TAG = "MealsRemoteDataSource";
    private static final String BASE_URL = "https://www.themealdb.com/api/json/v1/1/";
    private static MealsRemoteDataSource instance = null;
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
                        meals -> networkCallback.onSuccessSingleMeal(meals.getMeals()),
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
                .subscribeOn(Schedulers.io()) // Perform the operation on background thread
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
}
