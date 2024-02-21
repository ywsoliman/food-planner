package com.example.foodplanner.home.search.presenter;

import android.widget.SearchView;

import com.example.foodplanner.home.search.view.ISearchView;
import com.example.foodplanner.models.IRepository;
import com.jakewharton.rxbinding4.widget.RxSearchView;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SearchPresenter {

    private final ISearchView view;
    private final IRepository model;

    public SearchPresenter(ISearchView view, IRepository model) {
        this.view = view;
        this.model = model;
    }

    public void getSearchedMeals(String query) {
        model.getRemoteSearchedMeals(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        mealsList -> view.showSearchedMeals(mealsList.getMeals())
                );
    }

    public void searchForMealByQuery(SearchView searchView, String newText) {
        RxSearchView.queryTextChanges(searchView)
                .debounce(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        text -> getSearchedMeals(text.toString())
                );
    }
}
