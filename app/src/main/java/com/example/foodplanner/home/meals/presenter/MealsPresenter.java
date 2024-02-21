package com.example.foodplanner.home.meals.presenter;

import android.util.Log;

import com.example.foodplanner.home.meals.view.IMealView;
import com.example.foodplanner.models.IRepository;
import com.example.foodplanner.models.Meal;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MealsPresenter {
    private final IMealView view;
    private final IRepository model;

    public MealsPresenter(IMealView view, IRepository model) {
        this.view = view;
        this.model = model;
    }

    public void getMealsByCategory(String category) {
        model.getRemoteMealsByCategory(category)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        mealsList -> view.showMeals(mealsList.getMeals())
                );
    }

    public void getMealsByArea(String query) {
        model.getRemoteMealsByArea(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        mealsList -> view.showMeals(mealsList.getMeals())
                );
        ;
    }

    public void getMealsByIngredient(String ingredient) {
        model.getRemoteMealsByIngredient(ingredient)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        mealsList -> view.showMeals(mealsList.getMeals())
                );
        ;
    }

    public void filterMealsByName(List<Meal> meals, String query) {
        List<Meal> filteredList = new ArrayList<>();
        Observable.fromIterable(meals)
                .subscribeOn(Schedulers.io())
                .filter(meal -> meal.getStrMeal().toLowerCase().contains(query.toLowerCase()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        filteredList::add,
                        error -> Log.i("TAG", "onQueryTextChange: " + error.getMessage()),
                        () -> view.showFilteredMeals(filteredList)
                );
    }
}
