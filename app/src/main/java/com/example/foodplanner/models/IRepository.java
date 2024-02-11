package com.example.foodplanner.models;

import com.example.foodplanner.auth.IAuthCallback;
import com.example.foodplanner.auth.IAuthenticate;
import com.example.foodplanner.auth.register.view.IRegisterAuth;
import com.example.foodplanner.home.foryou.presenter.ForYouPresenter;
import com.example.foodplanner.home.meals.presenter.MealsPresenter;
import com.example.foodplanner.home.search.presenter.SearchPresenter;
import com.example.foodplanner.home.search.presenter.SearchedMealsCallback;
import com.example.foodplanner.network.MealDetailsNetworkCallback;
import com.example.foodplanner.network.MealsNetworkCallback;
import com.example.foodplanner.network.ForYouNetworkCallback;

public interface IRepository {
    void loginWithEmailAndPassword(IAuthCallback callback, String email, String pass);

    void getRemoteProducts(ForYouNetworkCallback forYouNetworkCallback);

    void getRemoteCategories(ForYouNetworkCallback forYouNetworkCallback);

    void getRemoteMealsByCategory(MealsNetworkCallback networkCallback, String category);

    void getRemoteMealDetails(MealDetailsNetworkCallback networkCallback, String mealID);

    void registerWithEmailAndPassword(IAuthCallback callback, String email, String password);

    void getRemoteSearchedMeals(SearchedMealsCallback callback, String query);

    void getRemoteAreas(ForYouNetworkCallback callback);

    void getRemoteMealsByArea(MealsNetworkCallback networkCallback, String query);
}
