package com.example.foodplanner.network;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.foodplanner.models.MealsList;

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

    private MealsRemoteDataSource() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mealsAPI = retrofit.create(MealsAPI.class);
    }

    public static synchronized MealsRemoteDataSource getInstance() {
        if (instance == null)
            instance = new MealsRemoteDataSource();
        return instance;
    }

    @Override
    public void requestSingleRandomMeal(NetworkCallback networkCallback) {
        Call<MealsList> call = mealsAPI.getSingleRandomMeal();
        call.enqueue(new Callback<MealsList>() {
            @Override
            public void onResponse(@NonNull Call<MealsList> call, @NonNull Response<MealsList> response) {
                if (response.isSuccessful() && response.body() != null) {
                    networkCallback.onSuccess(response.body().getMeals());
                }
            }

            @Override
            public void onFailure(@NonNull Call<MealsList> call, @NonNull Throwable t) {
                networkCallback.onFailure(t.getMessage());
            }
        });
    }
}
