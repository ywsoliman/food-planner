package com.example.foodplanner.home.foryou.searchbycategory.presenter;

import com.example.foodplanner.network.MealsNetworkCallback;
import com.example.foodplanner.home.foryou.searchbycategory.view.IMealView;
import com.example.foodplanner.models.IRepository;
import com.example.foodplanner.models.Meal;

import java.util.List;

public class MealsPresenter implements MealsNetworkCallback {
    private IMealView view;
    private IRepository model;

    public MealsPresenter(IMealView view, IRepository model) {
        this.view = view;
        this.model = model;
    }


    @Override
    public void onSuccess(List<Meal> meals) {
        view.showMealsOfCategory(meals);
    }

    @Override
    public void onFailure(String errorMsg) {

    }

    public void getMealsByCategory(String category) {
        model.getRemoteMealsByCategory(this, category);
    }
}
