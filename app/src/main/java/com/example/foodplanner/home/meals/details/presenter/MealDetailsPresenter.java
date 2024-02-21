package com.example.foodplanner.home.meals.details.presenter;

import com.example.foodplanner.home.meals.details.view.IMealDetailsView;
import com.example.foodplanner.models.IRepository;
import com.example.foodplanner.models.Meal;
import com.example.foodplanner.models.PlannedMeal;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MealDetailsPresenter {

    private final IMealDetailsView view;
    private final IRepository model;

    public MealDetailsPresenter(IMealDetailsView view, IRepository model) {
        this.view = view;
        this.model = model;
    }

    public void getMealDetails(String mealID) {
        model.getRemoteMealDetails(mealID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        mealsList -> view.showMealDetails(mealsList.getMeals().get(0))
                );
    }

    public void insertMealOnDate(PlannedMeal plannedMeal) {
        model.insertPlannedMeal(plannedMeal)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> view.onAddToCalendarSuccess());
    }

    public void insertMealToFavorites(Meal meal) {
        model.insert(meal)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> view.onAddToFavoritesSuccess());
    }
}
