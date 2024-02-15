package com.example.foodplanner.home.meals.details.presenter;

import com.example.foodplanner.home.meals.details.view.IMealDetailsView;
import com.example.foodplanner.models.IRepository;
import com.example.foodplanner.models.Meal;
import com.example.foodplanner.models.PlannedMeal;
import com.example.foodplanner.network.MealDetailsNetworkCallback;

import io.reactivex.rxjava3.core.Completable;

public class MealDetailsPresenter implements MealDetailsNetworkCallback {

    private final IMealDetailsView view;
    private final IRepository model;

    public MealDetailsPresenter(IMealDetailsView view, IRepository model) {
        this.view = view;
        this.model = model;
    }

    public void getMealDetails(String mealID) {
        model.getRemoteMealDetails(this, mealID);
    }

    @Override
    public void onSuccess(Meal meal) {
        view.showMealDetails(meal);
    }

    @Override
    public void onFailure(String errorMsg) {

    }

    public void insertMealOnDate(PlannedMeal plannedMeal) {
        model.insertPlannedMeal(plannedMeal);
    }

    public void insertMealToFavorites(Meal meal) {
        model.insert(meal);
    }
}
