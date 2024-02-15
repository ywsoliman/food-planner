package com.example.foodplanner.home.mealplan.presenter;

import androidx.lifecycle.LiveData;

import com.example.foodplanner.home.mealplan.view.IMealPlanView;
import com.example.foodplanner.models.IRepository;
import com.example.foodplanner.models.PlannedMeal;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

public class MealPlanPresenter {

    private final IMealPlanView view;
    private final IRepository model;

    public MealPlanPresenter(IMealPlanView view, IRepository model) {
        this.view = view;
        this.model = model;
    }

    public Flowable<List<PlannedMeal>> getMealsByDate(int year, int month, int dayOfMonth) {
        return model.getLocalPlannedMeals(year, month, dayOfMonth);
    }

    public void delete(PlannedMeal plannedMeal) {
        model.deletePlannedMeal(plannedMeal);
    }

    public void insert(PlannedMeal plannedMeals) {
        model.addPlannedMeal(plannedMeals);
    }
}
