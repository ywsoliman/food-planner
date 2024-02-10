package com.example.foodplanner.home.search.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.foodplanner.R;
import com.example.foodplanner.home.foryou.searchbycategory.OnMealClickListener;
import com.example.foodplanner.home.foryou.searchbycategory.view.MealsAdapter;
import com.example.foodplanner.home.search.presenter.SearchPresenter;
import com.example.foodplanner.models.Meal;
import com.example.foodplanner.models.Repository;
import com.example.foodplanner.network.MealsRemoteDataSource;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment implements ISearchView, OnMealClickListener {

    private SearchView searchView;
    private SearchPresenter searchPresenter;
    private RecyclerView rvSearchedMeals;
    private MealsAdapter mealsAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        searchPresenter = new SearchPresenter(this,
                Repository.getInstance(
                        FirebaseAuth.getInstance(),
                        MealsRemoteDataSource.getInstance(requireContext())
                ));

        initUI(view);

        mealsAdapter = new MealsAdapter(requireContext(), new ArrayList<>(), this);
        rvSearchedMeals.setAdapter(mealsAdapter);
        rvSearchedMeals.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchPresenter.getSearchedMeals(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
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
}