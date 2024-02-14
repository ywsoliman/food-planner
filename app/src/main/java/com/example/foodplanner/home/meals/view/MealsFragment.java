package com.example.foodplanner.home.meals.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodplanner.R;
import com.example.foodplanner.db.MealsLocalDataSource;
import com.example.foodplanner.home.foryou.view.Type;
import com.example.foodplanner.home.meals.presenter.MealsPresenter;
import com.example.foodplanner.models.Meal;
import com.example.foodplanner.models.Repository;
import com.example.foodplanner.network.MealsRemoteDataSource;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MealsFragment extends Fragment implements IMealView, OnMealClickListener {

    private RecyclerView recyclerView;
    private MealsAdapter adapter;
    private MealsPresenter presenter;
    private SearchView searchView;
    private List<Meal> meals;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_meals, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initUI(view);

        adapter = new MealsAdapter(requireContext(), new ArrayList<>(), this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(gridLayoutManager);

        presenter = new MealsPresenter(this, Repository.getInstance(
                FirebaseAuth.getInstance(),
                MealsRemoteDataSource.getInstance(requireContext()),
                MealsLocalDataSource.getInstance(getContext())
        ));

        Type type = MealsFragmentArgs.fromBundle(getArguments()).getType();
        String query = MealsFragmentArgs.fromBundle(getArguments()).getQuery();
        switch (type) {
            case CATEGORY:
                presenter.getMealsByCategory(query);
                break;
            case AREA:
                presenter.getMealsByArea(query);
                break;
            case INGREDIENT:
                presenter.getMealsByIngredient(query.replaceAll(" ", "_").toLowerCase());
                break;
        }

        addSearchViewListener();
    }

    private void addSearchViewListener() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @SuppressLint("CheckResult")
            @Override
            public boolean onQueryTextChange(String newText) {
                Log.i("TAG", "onQueryTextChange: " + newText);
                if (newText.isEmpty())
                    adapter.setList(meals);
                else {
                    List<Meal> filteredList = new ArrayList<>();
                    Observable.fromIterable(meals)
                            .subscribeOn(Schedulers.io())
                            .filter(meal -> meal.getStrMeal().toLowerCase().contains(newText.toLowerCase()))
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    filteredList::add,
                                    error -> Log.i("TAG", "onQueryTextChange: " + error.getMessage()),
                                    () -> adapter.setList(filteredList)
                            );
                    Log.i("TAG", "onQueryTextChange Filtered Meals: " + filteredList);
                }
                return true;
            }
        });
    }

    private void initUI(View view) {
        recyclerView = view.findViewById(R.id.rvMealsByCategory);
        searchView = view.findViewById(R.id.searchView);
    }

    @Override
    public void showMeals(List<Meal> mealList) {
        adapter.setList(mealList);
        this.meals = mealList;
    }

    @Override
    public void onMealItemClicked(String mealID) {
        MealsFragmentDirections.ActionMealsFragmentToMealDetailsFragment action = MealsFragmentDirections.actionMealsFragmentToMealDetailsFragment(mealID);
        Navigation.findNavController(requireView()).navigate(action);
    }

}