package com.example.foodplanner.network;

import com.example.foodplanner.models.MealsList;
import com.example.foodplanner.models.category.CategoryList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MealsAPI {

    @GET("random.php")
    Call<MealsList> getSingleRandomMeal();

    @GET("categories.php")
    Call<CategoryList> getCategories();

    //https://www.themealdb.com/api/json/v1/1/filter.php?c=Beef
    @GET("filter.php")
    Call<MealsList> getMealsByCategory(@Query("c") String category);
}
