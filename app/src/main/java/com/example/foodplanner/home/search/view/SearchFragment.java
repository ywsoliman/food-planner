package com.example.foodplanner.home.search.view;

import static android.content.Context.INPUT_METHOD_SERVICE;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodplanner.R;
import com.example.foodplanner.db.MealsLocalDataSource;
import com.example.foodplanner.home.meals.view.MealsAdapter;
import com.example.foodplanner.home.meals.view.OnMealClickListener;
import com.example.foodplanner.home.search.presenter.SearchPresenter;
import com.example.foodplanner.models.Meal;
import com.example.foodplanner.models.Repository;
import com.example.foodplanner.network.MealsRemoteDataSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SearchFragment extends Fragment implements ISearchView, OnMealClickListener {

    private static final String TAG = "SearchFragment";
    private SearchView searchView;
    private SearchPresenter searchPresenter;
    private RecyclerView rvSearchedMeals;
    private MealsAdapter mealsAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate: ");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.i(TAG, "onCreateView: ");
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.i(TAG, "onViewCreated: ");

        searchPresenter = new SearchPresenter(this,
                Repository.getInstance(
                        MealsRemoteDataSource.getInstance(getContext()),
                        MealsLocalDataSource.getInstance(getContext())
                ));

        initUI(view);

        searchView.requestFocus();
        new Handler().postDelayed(this::showKeyboard, 10);

        mealsAdapter = new MealsAdapter(requireContext(), new ArrayList<>(), this);
        rvSearchedMeals.setAdapter(mealsAdapter);
        rvSearchedMeals.setLayoutManager(new GridLayoutManager(requireContext(), 2));

        addSearchViewListener();
    }

    private void addSearchViewListener() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty())
                    showSearchedMeals(Collections.emptyList());
                else
                    searchPresenter.searchForMealByQuery(searchView, newText);
                return true;
            }
        });
    }

    private void initUI(@NonNull View view) {
        searchView = view.findViewById(R.id.searchView);
        rvSearchedMeals = view.findViewById(R.id.rvSearchMeals);
    }

    @Override
    public void showSearchedMeals(List<Meal> searchedMeals) {
        mealsAdapter.setList(searchedMeals);
    }

    @Override
    public void onMealItemClicked(String mealID) {
        SearchFragmentDirections.ActionSearchFragmentToMealDetailsFragment action = SearchFragmentDirections.actionSearchFragmentToMealDetailsFragment(mealID);
        Navigation.findNavController(requireView()).navigate(action);
    }

    private void showKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) requireContext().getSystemService(INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.showSoftInput(searchView, InputMethodManager.SHOW_IMPLICIT);
        }
    }
}