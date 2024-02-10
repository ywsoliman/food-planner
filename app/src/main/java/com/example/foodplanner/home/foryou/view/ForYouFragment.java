package com.example.foodplanner.home.foryou.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.foodplanner.R;
import com.example.foodplanner.home.foryou.presenter.ForYouPresenter;
import com.example.foodplanner.home.foryou.searchbycategory.OnCategoryClickListener;
import com.example.foodplanner.home.foryou.searchbycategory.view.CategoryAdapter;
import com.example.foodplanner.models.Meal;
import com.example.foodplanner.models.Repository;
import com.example.foodplanner.models.category.Category;
import com.example.foodplanner.network.MealsRemoteDataSource;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class ForYouFragment extends Fragment implements IForYouView, OnCategoryClickListener {

    private TextView trendingMealName;
    private ImageView trendingMealImage;
    private RecyclerView recyclerView;
    private CategoryAdapter categoryAdapter;
    private ForYouPresenter forYouPresenter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_for_you, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initUI(view);

        GridLayoutManager gridLayoutManager
                = new GridLayoutManager(getContext(), 2, GridLayoutManager.HORIZONTAL, false);
        categoryAdapter = new CategoryAdapter(getContext(), new ArrayList<>(), this);

        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(categoryAdapter);

        forYouPresenter = new ForYouPresenter(this,
                Repository.getInstance(FirebaseAuth.getInstance(),
                        MealsRemoteDataSource.getInstance(requireContext())));
        forYouPresenter.getSingleRandomMeal();
        forYouPresenter.getCategories();
    }

    private void initUI(View view) {
        trendingMealName = view.findViewById(R.id.trendingMealName);
        trendingMealImage = view.findViewById(R.id.trendingMealImage);
        recyclerView = view.findViewById(R.id.rvCategories);
    }

    @Override
    public void showSingleRandomMeal(Meal meal) {
        trendingMealName.setText(meal.getStrMeal());
        Glide.with(this)
                .load(meal.getStrMealThumb())
                .apply(new RequestOptions().override(0, 200))
                .into(trendingMealImage);
    }

    @Override
    public void showCategories(List<Category> categories) {
        categoryAdapter.setList(categories);
    }

    @Override
    public void onCategoryItemClicked(String categoryName) {
        ForYouFragmentDirections.ActionForYouFragmentToMealsFragment action = ForYouFragmentDirections.actionForYouFragmentToMealsFragment(categoryName);
        Navigation.findNavController(requireView()).navigate(action);
    }
}