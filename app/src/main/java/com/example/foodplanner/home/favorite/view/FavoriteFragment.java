package com.example.foodplanner.home.favorite.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodplanner.R;
import com.example.foodplanner.db.MealsLocalDataSource;
import com.example.foodplanner.home.favorite.presenter.FavoritePresenter;
import com.example.foodplanner.home.meals.details.view.OnMealButtonClickListener;
import com.example.foodplanner.home.meals.view.OnMealClickListener;
import com.example.foodplanner.models.Meal;
import com.example.foodplanner.models.Repository;
import com.example.foodplanner.network.MealsRemoteDataSource;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class FavoriteFragment extends Fragment implements IFavoriteView, OnMealClickListener, OnMealButtonClickListener {

    private FavoritePresenter presenter;
    private FavoriteAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenter = new FavoritePresenter(this, Repository.getInstance(
                MealsRemoteDataSource.getInstance(getContext()),
                MealsLocalDataSource.getInstance(getContext())
        ));

        adapter = new FavoriteAdapter(getContext(), new ArrayList<>(), this, this);
        RecyclerView rvFavorite = view.findViewById(R.id.rvFavoriteMeals);
        rvFavorite.setLayoutManager(new LinearLayoutManager(getContext()));
        rvFavorite.setAdapter(adapter);

        presenter.getFavoriteMeals();
    }

    @Override
    public void onMealItemClicked(String mealID) {
        FavoriteFragmentDirections.ActionFavoriteFragmentToMealDetailsFragment action =
                FavoriteFragmentDirections.actionFavoriteFragmentToMealDetailsFragment(mealID);
        Navigation.findNavController(requireView()).navigate(action);
    }

    @Override
    public void onMealFABClicked(Meal meal) {
        presenter.deleteFromFavorite(meal);
    }

    @Override
    public void showFavoriteMeals(List<Meal> meals) {
        adapter.setList(meals);
    }

    @Override
    public void onDeleteFromFavoritesSuccess(Meal meal) {
        Snackbar.make(requireView(), R.string.meal_is_deleted_from_favorites, Snackbar.LENGTH_LONG)
                .setAction(R.string.undo, v -> presenter.addMealToFavorites(meal))
                .setAnchorView(R.id.bottomNavigationView)
                .show();
    }
}