package com.example.foodplanner.home.foryou.view;

import com.example.foodplanner.models.Meal;
import com.example.foodplanner.models.category.Category;

import java.util.List;

public interface IForYouView {

    void showSingleRandomMeal(Meal meal);

    void showCategories(List<Category> categories);

}