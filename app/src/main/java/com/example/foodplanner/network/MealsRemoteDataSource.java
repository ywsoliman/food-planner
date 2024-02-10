package com.example.foodplanner.network;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.foodplanner.models.MealsList;
import com.example.foodplanner.models.category.CategoryList;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
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
        Call<MealsList> call = mealsAPI.getSingleRandomMeal();
        call.enqueue(new Callback<MealsList>() {
            @Override
            public void onResponse(@NonNull Call<MealsList> call, @NonNull Response<MealsList> response) {
                if (response.isSuccessful() && response.body() != null) {
                    networkCallback.onSuccessSingleMeal(response.body().getMeals());
                }
            }

            @Override
            public void onFailure(@NonNull Call<MealsList> call, @NonNull Throwable t) {
                networkCallback.onFailure(t.getMessage());
            }
        });
    }

    @Override
    public void requestCategories(ForYouNetworkCallback networkCallback) {
        Call<CategoryList> call = mealsAPI.getCategories();
        call.enqueue(new Callback<CategoryList>() {
            @Override
            public void onResponse(@NonNull Call<CategoryList> call, @NonNull Response<CategoryList> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.i(TAG, "onResponse: Categories = " + response.body());
                    networkCallback.onSuccessCategories(response.body().getCategories());
                }
            }

            @Override
            public void onFailure(@NonNull Call<CategoryList> call, @NonNull Throwable t) {
                Log.i(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    @Override
    public void requestMealsByCategory(MealsNetworkCallback networkCallback, String category) {
        Call<MealsList> call = mealsAPI.getMealsByCategory(category);
        call.enqueue(new Callback<MealsList>() {
            @Override
            public void onResponse(@NonNull Call<MealsList> call, @NonNull Response<MealsList> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.i(TAG, "onResponse: requestMealsByCategory");
                    networkCallback.onSuccess(response.body().getMeals());
                }
            }

            @Override
            public void onFailure(@NonNull Call<MealsList> call, @NonNull Throwable t) {
                Log.i(TAG, "onFailure: requestMealsByCategory -> " + t.getMessage());
            }
        });
    }

    @Override
    public void requestMealDetailsByID(MealDetailsNetworkCallback networkCallback, String mealID) {
        Call<MealsList> call = mealsAPI.getMealDetailsByID(mealID);
        call.enqueue(new Callback<MealsList>() {
            @Override
            public void onResponse(@NonNull Call<MealsList> call, @NonNull Response<MealsList> response) {
                if (response.isSuccessful() && response.body() != null) {
                    networkCallback.onSuccess(response.body().getMeals().get(0));
                }
            }

            @Override
            public void onFailure(@NonNull Call<MealsList> call, @NonNull Throwable t) {
                Log.i(TAG, "onFailure: requestMealDetailsByID -> " + t.getMessage());
            }
        });
    }
}
