package com.example.foodplanner.network;

import com.example.foodplanner.models.MealsList;
import com.example.foodplanner.models.area.AreaList;
import com.example.foodplanner.models.category.CategoryList;
import com.example.foodplanner.models.ingredients.IngredientList;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MealsAPI {

    @GET("random.php")
    Single<MealsList> getSingleRandomMeal();

    @GET("categories.php")
    Single<CategoryList> getCategories();

    @GET("filter.php")
    Single<MealsList> getMealsByCategory(@Query("c") String category);

    @GET("lookup.php")
    Single<MealsList> getMealDetailsByID(@Query("i") String mealID);

    @GET("search.php")
    Single<MealsList> getMealsBySearch(@Query("s") String query);

    @GET("list.php?a=list")
    Single<AreaList> getAreas();

    @GET("filter.php")
    Single<MealsList> getMealsByArea(@Query("a") String area);

    @GET("list.php?i=list")
    Single<IngredientList> getIngredients();

    @GET("filter.php")
    Single<MealsList> getMealsByIngredient(@Query("i") String ingredient);
}