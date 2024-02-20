package com.example.foodplanner.home.meals.presenter;

import com.example.foodplanner.home.meals.view.IMealView;
import com.example.foodplanner.models.IRepository;
import com.example.foodplanner.models.Meal;
import com.example.foodplanner.network.MealsNetworkCallback;

import java.util.List;

public class MealsPresenter implements MealsNetworkCallback {
    private final IMealView view;
    private final IRepository model;

    public MealsPresenter(IMealView view, IRepository model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void onSuccess(List<Meal> meals) {
        view.showMeals(meals);
    }

    @Override
    public void onFailure(String errorMsg) {

    }

    public void getMealsByCategory(String category) {
        model.getRemoteMealsByCategory(this, category);
    }

    public void getMealsByArea(String query) {
        model.getRemoteMealsByArea(this, query);
    }

    public void getMealsByIngredient(String ingredient) {
        model.getRemoteMealsByIngredient(this, ingredient);
    }
}
