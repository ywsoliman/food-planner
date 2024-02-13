package com.example.foodplanner.home.foryou.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.foodplanner.R;
import com.example.foodplanner.db.MealsLocalDataSource;
import com.example.foodplanner.home.foryou.presenter.ForYouPresenter;
import com.example.foodplanner.home.foryou.view.area.AreaAdapter;
import com.example.foodplanner.home.foryou.view.area.OnAreaClickListener;
import com.example.foodplanner.home.foryou.view.category.OnCategoryClickListener;
import com.example.foodplanner.home.foryou.view.category.CategoryAdapter;
import com.example.foodplanner.home.foryou.view.ingredient.IngredientAdapter;
import com.example.foodplanner.home.foryou.view.ingredient.OnIngredientClickListener;
import com.example.foodplanner.home.meals.view.OnMealClickListener;
import com.example.foodplanner.models.Meal;
import com.example.foodplanner.models.Repository;
import com.example.foodplanner.models.area.Area;
import com.example.foodplanner.models.category.Category;
import com.example.foodplanner.models.ingredients.Ingredient;
import com.example.foodplanner.network.MealsRemoteDataSource;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ForYouFragment extends Fragment implements IForYouView, OnCategoryClickListener, OnAreaClickListener, OnIngredientClickListener, OnMealClickListener {

    private View trendingMealCard;
    private TextView trendingMealName;
    private ImageView trendingMealImage;
    private RecyclerView rvCategory;
    private CategoryAdapter categoryAdapter;
    private RecyclerView rvArea;
    private AreaAdapter areaAdapter;
    private RecyclerView rvIngredients;
    private IngredientAdapter ingredientsAdapter;
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

        categoryAdapter = new CategoryAdapter(getContext(), new ArrayList<>(), this);
        areaAdapter = new AreaAdapter(getContext(), new ArrayList<>(), this);
        ingredientsAdapter = new IngredientAdapter(getContext(), new ArrayList<>(), this);

        rvCategory.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.HORIZONTAL, false));
        rvCategory.setAdapter(categoryAdapter);

        rvArea.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.HORIZONTAL, false));
        rvArea.setAdapter(areaAdapter);

        rvIngredients.setLayoutManager(new GridLayoutManager(getContext(), 3, GridLayoutManager.HORIZONTAL, false));
        rvIngredients.setAdapter(ingredientsAdapter);

        forYouPresenter = new ForYouPresenter(this,
                Repository.getInstance(FirebaseAuth.getInstance(),
                        MealsRemoteDataSource.getInstance(requireContext()),
                        MealsLocalDataSource.getInstance(getContext())
                ));
        forYouPresenter.getSingleRandomMeal();
        forYouPresenter.getCategories();
        forYouPresenter.getAreas();
        forYouPresenter.getIngredients();
    }

    private void initUI(View view) {
        trendingMealCard = view.findViewById(R.id.trendingMealCardView);
        trendingMealName = trendingMealCard.findViewById(R.id.mealTitle);
        trendingMealImage = trendingMealCard.findViewById(R.id.mealThumbnail);
        rvCategory = view.findViewById(R.id.rvCategories);
        rvArea = view.findViewById(R.id.rvArea);
        rvIngredients = view.findViewById(R.id.rvIngredients);
    }

    @Override
    public void showSingleRandomMeal(Meal meal) {
        trendingMealName.setText(meal.getStrMeal());
        Glide.with(this)
                .load(meal.getStrMealThumb())
                .apply(new RequestOptions().override(0, 200))
                .into(trendingMealImage);
        trendingMealCard.setOnClickListener(v -> onMealItemClicked(meal.getIdMeal()));
        trendingMealCard.findViewById(R.id.saveFAB)
                .setOnClickListener(v -> onSaveOrDeleteButtonClicked(meal));
    }

    @Override
    public void showCategories(List<Category> categories) {
        categoryAdapter.setList(categories);
    }

    @Override
    public void showAreas(List<Area> areas) {
        areaAdapter.setList(areas);
    }

    @Override
    public void showIngredients(List<Ingredient> ingredients) {
        ingredientsAdapter.setList(ingredients);
    }

    @Override
    public void onCategoryItemClicked(String categoryName) {
        ForYouFragmentDirections.ActionForYouFragmentToMealsFragment action = ForYouFragmentDirections.actionForYouFragmentToMealsFragment(Type.CATEGORY, categoryName);
        Navigation.findNavController(requireView()).navigate(action);
    }

    @Override
    public void onAreaItemClicked(String areaName) {
        ForYouFragmentDirections.ActionForYouFragmentToMealsFragment action = ForYouFragmentDirections.actionForYouFragmentToMealsFragment(Type.AREA, areaName);
        Navigation.findNavController(requireView()).navigate(action);
    }

    @Override
    public void onIngredientItemClicked(String ingredientName) {
        ForYouFragmentDirections.ActionForYouFragmentToMealsFragment action = ForYouFragmentDirections.actionForYouFragmentToMealsFragment(Type.INGREDIENT, ingredientName);
        Navigation.findNavController(requireView()).navigate(action);
    }

    @Override
    public void onMealItemClicked(String mealID) {
        ForYouFragmentDirections.ActionForYouFragmentToMealDetailsFragment action =
                ForYouFragmentDirections.actionForYouFragmentToMealDetailsFragment(mealID);
        Navigation.findNavController(requireView()).navigate(action);
    }

    @Override
    public void onSaveOrDeleteButtonClicked(Meal meal) {
        forYouPresenter.addMealToFavorites(meal);
        Snackbar.make(requireView(), R.string.meal_is_added_to_favorites_successfully, Snackbar.LENGTH_SHORT)
                .setAnchorView(R.id.bottomNavigationView)
                .show();
    }
}