package com.example.foodplanner.network;

import com.example.foodplanner.models.MealsList;
import com.example.foodplanner.models.category.CategoryList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface MealsAPI {

    @GET("random.php")
    Call<MealsList> getSingleRandomMeal();

    @GET("categories.php")
    Call<CategoryList> getCategories();
}
