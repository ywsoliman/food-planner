package com.example.foodplanner.home.mealplan.presenter;

import com.example.foodplanner.home.mealplan.view.IMealPlanView;
import com.example.foodplanner.models.IRepository;
import com.example.foodplanner.models.PlannedMeal;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MealPlanPresenter {

    private final IMealPlanView view;
    private final IRepository model;

    public MealPlanPresenter(IMealPlanView view, IRepository model) {
        this.view = view;
        this.model = model;
    }

    public void getMealsByDate(int year, int month, int dayOfMonth) {
        model.getLocalPlannedMeals(year, month, dayOfMonth)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(plannedMeals -> view.showPlannedMealsByDate(plannedMeals));
    }

    public void delete(PlannedMeal plannedMeal) {
        model.deletePlannedMeal(plannedMeal)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> view.onDeleteFromCalendarSuccess(plannedMeal));
    }

    public void insert(PlannedMeal plannedMeals) {
        model.addPlannedMeal(plannedMeals)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }
}
