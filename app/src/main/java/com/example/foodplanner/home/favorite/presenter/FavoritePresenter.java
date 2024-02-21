package com.example.foodplanner.home.favorite.presenter;

import com.example.foodplanner.home.favorite.view.IFavoriteView;
import com.example.foodplanner.models.IRepository;
import com.example.foodplanner.models.Meal;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FavoritePresenter {

    private final IFavoriteView view;
    private final IRepository model;

    public FavoritePresenter(IFavoriteView view, IRepository model) {
        this.view = view;
        this.model = model;
    }

    public void getFavoriteMeals() {
        model.getLocalMeals()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(meals -> view.showFavoriteMeals(meals));
    }

    public void deleteFromFavorite(Meal meal) {
        model.delete(meal)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> view.onDeleteFromFavoritesSuccess(meal));
    }

    public void addMealToFavorites(Meal meal) {
        model.insert(meal)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }
}
