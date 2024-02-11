package com.example.foodplanner.home.foryou.view;

import com.example.foodplanner.models.Meal;
import com.example.foodplanner.models.area.Area;
import com.example.foodplanner.models.category.Category;
import com.example.foodplanner.models.ingredients.Ingredient;

import java.util.List;

public interface IForYouView {

    void showSingleRandomMeal(Meal meal);

    void showCategories(List<Category> categories);

    void showAreas(List<Area> areas);

    void showIngredients(List<Ingredient> meals);
}
