package com.example.foodplanner.home.foryou.presenter;

import com.example.foodplanner.home.foryou.view.IForYouView;
import com.example.foodplanner.models.IRepository;
import com.example.foodplanner.models.Meal;
import com.example.foodplanner.models.area.Area;
import com.example.foodplanner.models.category.Category;
import com.example.foodplanner.models.ingredients.Ingredient;
import com.example.foodplanner.network.ForYouNetworkCallback;

import java.util.List;

public class ForYouPresenter implements ForYouNetworkCallback {

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

    @Override
    public void onSuccessAreas(List<Area> areas) {
        view.showAreas(areas);
    }

    @Override
    public void onSuccessIngredients(List<Ingredient> meals) {
        view.showIngredients(meals);
    }

    public void getAreas() {
        model.getRemoteAreas(this);
    }

    public void getIngredients() {
        model.getRemoteIngredients(this);
    }
}
