package com.example.foodplanner.home.presenter;

import com.example.foodplanner.home.view.IHomeView;
import com.example.foodplanner.models.IRepository;
import com.example.foodplanner.models.Meal;
import com.example.foodplanner.network.NetworkCallback;

import java.util.List;

public class ForYouPresenter implements NetworkCallback {

    private IHomeView view;
    private IRepository model;

    public ForYouPresenter(IHomeView view, IRepository model) {
        this.view = view;
        this.model = model;
    }

    public void getSingleRandomMeal() {
        model.getRemoteProducts(this);
    }

    @Override
    public void onSuccess(List<Meal> meals) {
        view.showSingleRandomMeal(meals.get(0));
    }

    @Override
    public void onFailure(String errorMsg) {

    }
}
