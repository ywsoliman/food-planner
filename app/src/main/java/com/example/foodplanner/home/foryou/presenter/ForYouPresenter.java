package com.example.foodplanner.home.foryou.presenter;

import android.util.Log;

import com.example.foodplanner.home.foryou.view.IForYouView;
import com.example.foodplanner.models.IRepository;
import com.example.foodplanner.models.ingredients.Ingredient;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ForYouPresenter {

    private final IForYouView view;
    private final IRepository model;

    public ForYouPresenter(IForYouView view, IRepository model) {
        this.view = view;
        this.model = model;
    }

    public void getSingleRandomMeal() {
        model.getRemoteSingleMeal()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        mealsList -> view.showSingleRandomMeal(mealsList.getMeals().get(0))
                );
    }

    public void getCategories() {
        model.getRemoteCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        categoryList -> view.showCategories(categoryList.getCategories())
                );
    }

    public void getAreas() {
        model.getRemoteAreas()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        areaList -> view.showAreas(areaList.getMeals())
                );
    }

    public void getIngredients() {
        model.getRemoteIngredients()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        ingredientList -> view.showIngredients(ingredientList.getMeals())
                );
    }

    public void backupMeals() {
        model.backupMeals()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> view.onBackupSuccess(),
                        throwable -> view.onBackupFailure());
    }

    public void showFilteredIngredients(List<Ingredient> ingredients, String newText) {
        List<Ingredient> filteredList = new ArrayList<>();
        Observable.fromIterable(ingredients)
                .subscribeOn(Schedulers.io())
                .filter(ingredient -> ingredient.getStrIngredient().toLowerCase().contains(newText.toLowerCase()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        filteredList::add,
                        error -> Log.i("TAG", "onQueryTextChange: " + error.getMessage()),
                        () -> view.showFilteredIngredients(filteredList)
                );
    }
}
