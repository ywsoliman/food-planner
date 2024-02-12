package com.example.foodplanner.home.favorite.presenter;

import androidx.lifecycle.LiveData;

import com.example.foodplanner.home.favorite.view.IFavoriteView;
import com.example.foodplanner.models.IRepository;
import com.example.foodplanner.models.Meal;

import java.util.List;

public class FavoritePresenter {

    private final IFavoriteView view;
    private final IRepository model;

    public FavoritePresenter(IFavoriteView view, IRepository model) {
        this.view = view;
        this.model = model;
    }

    public LiveData<List<Meal>> getFavoriteMeals() {
        return model.getLocalMeals();
    }

    public void deleteFromFavorite(Meal meal) {
        model.delete(meal);
    }

    public void addMealToFavorites(Meal meal) {
        model.insert(meal);
    }
}
