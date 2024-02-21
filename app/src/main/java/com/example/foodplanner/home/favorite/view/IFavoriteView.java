package com.example.foodplanner.home.favorite.view;

import com.example.foodplanner.models.Meal;

import java.util.List;

public interface IFavoriteView {
    void showFavoriteMeals(List<Meal> meals);

    void onDeleteFromFavoritesSuccess(Meal meal);
}
