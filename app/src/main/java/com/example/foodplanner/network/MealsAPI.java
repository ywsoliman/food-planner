package com.example.foodplanner.network;

import com.example.foodplanner.models.MealsList;
import com.example.foodplanner.models.area.AreaList;
import com.example.foodplanner.models.category.CategoryList;
import com.example.foodplanner.models.ingredients.IngredientList;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MealsAPI {

    @GET("random.php")
    Single<MealsList> getSingleRandomMeal();

    @GET("categories.php")
    Single<CategoryList> getCategories();

    //https://www.themealdb.com/api/json/v1/1/filter.php?c=Beef
    @GET("filter.php")
    Single<MealsList> getMealsByCategory(@Query("c") String category);

    //www.themealdb.com/api/json/v1/1/lookup.php?i=52772
    @GET("lookup.php")
    Single<MealsList> getMealDetailsByID(@Query("i") String mealID);

    //https://www.themealdb.com/api/json/v1/1/search.php?s=Arrabiata
    @GET("search.php")
    Observable<MealsList> getMealsBySearch(@Query("s") String query);

    @GET("list.php?a=list")
    Single<AreaList> getAreas();

    // www.themealdb.com/api/json/v1/1/filter.php?a=Canadian
    @GET("filter.php")
    Single<MealsList> getMealsByArea(@Query("a") String area);

    @GET("list.php?i=list")
    Single<IngredientList> getIngredients();

    // www.themealdb.com/api/json/v1/1/filter.php?i=chicken_breast
    @GET("filter.php")
    Single<MealsList> getMealsByIngredient(@Query("i") String ingredient);
}