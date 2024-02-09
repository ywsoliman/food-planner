package com.example.foodplanner.home.foryou.presenter;

import com.example.foodplanner.home.view.IForYouView;
import com.example.foodplanner.models.IRepository;
import com.example.foodplanner.models.Meal;
import com.example.foodplanner.models.category.Category;
import com.example.foodplanner.network.NetworkCallback;

import java.util.List;

public class ForYouPresenter implements NetworkCallback {

    private final IForYouView view;
    private final IRepository model;

    public ForYouPresenter(IForYouView view, IRepository model) {
        this.view = view;
        this.model = model;
    }

    public void getSingleRandomMeal() {
        model.getRemoteProducts(this);
    }

    public void getCategories() {
        model.getRemoteCategories(this);
    }

    @Override
    public void onSuccessSingleMeal(List<Meal> meals) {
        view.showSingleRandomMeal(meals.get(0));
    }

    @Override
    public void onFailure(String errorMsg) {

    }

    @Override
    public void onSuccessCategories(List<Category> categories) {
        view.showCategories(categories);
    }
}
