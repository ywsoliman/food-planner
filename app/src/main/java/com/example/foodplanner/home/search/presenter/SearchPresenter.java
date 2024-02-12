package com.example.foodplanner.home.search.presenter;

import com.example.foodplanner.home.search.view.ISearchView;
import com.example.foodplanner.models.IRepository;
import com.example.foodplanner.models.Meal;

import java.util.List;

public class SearchPresenter implements SearchedMealsCallback {

    private final ISearchView view;
    private final IRepository model;

    public SearchPresenter(ISearchView view, IRepository model) {
        this.view = view;
        this.model = model;
    }

    public void getSearchedMeals(String query) {
        model.getRemoteSearchedMeals(this, query);
    }

    @Override
    public void onSuccess(List<Meal> searchedMeals) {
        if (searchedMeals != null)
            view.showSearchedMeals(searchedMeals);
    }

    @Override
    public void onFailure(String errorMsg) {

    }

    public void addMealToFavorites(Meal meal) {
        model.insert(meal);
    }
}
