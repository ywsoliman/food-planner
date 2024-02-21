package com.example.foodplanner.home.presenter;

import com.example.foodplanner.home.view.IHomeView;
import com.example.foodplanner.models.IRepository;
import com.example.foodplanner.models.Meal;
import com.example.foodplanner.models.PlannedMeal;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HomePresenter {

    private final IHomeView view;
    private final IRepository model;

    public HomePresenter(IHomeView view, IRepository model) {
        this.view = view;
        this.model = model;
    }


    public void synchronizeMeals() {
        model.synchronizeMeals()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        pair -> {
                            replaceFavoriteMeals(pair.first);
                            replacePlannedMeals(pair.second);
                        }
                );
    }

    public void replaceFavoriteMeals(List<Meal> meals) {
        model.replaceFavoriteMeals(meals)
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    public void replacePlannedMeals(List<PlannedMeal> plannedMeals) {
        model.replacePlannedMeals(plannedMeals)
                .subscribeOn(Schedulers.io())
                .subscribe();
    }
}
