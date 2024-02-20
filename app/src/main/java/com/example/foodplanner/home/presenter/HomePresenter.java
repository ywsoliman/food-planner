package com.example.foodplanner.home.presenter;

import com.example.foodplanner.auth.login.view.ISyncCallback;
import com.example.foodplanner.home.view.IHomeView;
import com.example.foodplanner.models.IRepository;
import com.example.foodplanner.models.Meal;
import com.example.foodplanner.models.PlannedMeal;

import java.util.List;

public class HomePresenter implements ISyncCallback {

    private IHomeView view;
    private IRepository model;

    public HomePresenter(IHomeView view, IRepository model) {
        this.view = view;
        this.model = model;
    }


    public void synchronizeMeals() {
        model.synchronizeMeals(this);
    }

    @Override
    public void onSuccessFetchingMealsFromFirebase(List<Meal> meals) {
        view.onSuccessFetchingMealsFromFirebase(meals);
    }

    @Override
    public void onSuccessFetchingPlannedFromFirebase(List<PlannedMeal> plannedMeals) {
        view.onSuccessFetchingPlannedFromFirebase(plannedMeals);
    }

    public void replaceFavoriteMeals(List<Meal> meals) {
        model.replaceFavoriteMeals(meals);
    }

    public void replacePlannedMeals(List<PlannedMeal> plannedMeals) {
        model.replacePlannedMeals(plannedMeals);
    }
}
