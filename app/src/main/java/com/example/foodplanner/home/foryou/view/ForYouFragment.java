package com.example.foodplanner.home.foryou.view;

import static com.example.foodplanner.auth.login.view.LoginFragment.PREF_NAME;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.foodplanner.FireStoreDataManager;
import com.example.foodplanner.R;
import com.example.foodplanner.auth.AuthActivity;
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
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

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
    private ShimmerFrameLayout shimmerTrendingMeal;
    private ShimmerFrameLayout shimmerCategories;
    private ShimmerFrameLayout shimmerAreas;
    private ShimmerFrameLayout shimmerIngredients;
    private SearchView ingredientSearchView;
    private Button logoutButton;

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

        logoutButton.setOnClickListener(v -> handleLogoutButton());

        categoryAdapter = new CategoryAdapter(getContext(), new ArrayList<>(), this);
        areaAdapter = new AreaAdapter(new ArrayList<>(), this);
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

    private void handleLogoutButton() {
        FirebaseAuth.getInstance().signOut();
        setRememberMeToFalse();
//        FireStoreDataManager.getInstance(getContext()).synchronizeUserData();
        Intent intent = new Intent(requireContext(), AuthActivity.class);
        startActivity(intent);
        requireActivity().finish();
    }

    private void initUI(View view) {
        trendingMealCard = view.findViewById(R.id.trendingMealCardView);
        trendingMealName = trendingMealCard.findViewById(R.id.mealTitle);
        trendingMealImage = trendingMealCard.findViewById(R.id.mealThumbnail);
        rvCategory = view.findViewById(R.id.rvCategories);
        rvArea = view.findViewById(R.id.rvArea);
        rvIngredients = view.findViewById(R.id.rvIngredients);
        shimmerTrendingMeal = view.findViewById(R.id.shimmerTrendingMealCard);
        shimmerCategories = view.findViewById(R.id.shimmerCategories);
        shimmerAreas = view.findViewById(R.id.shimmerAreas);
        shimmerIngredients = view.findViewById(R.id.shimmerIngredients);
        ingredientSearchView = view.findViewById(R.id.ingredientSearchView);
        logoutButton = view.findViewById(R.id.logoutButton);
    }

    @Override
    public void showSingleRandomMeal(Meal meal) {

        hideShimmer(shimmerTrendingMeal);
        trendingMealCard.setVisibility(View.VISIBLE);

        trendingMealName.setText(meal.getStrMeal());
        Glide.with(this)
                .load(meal.getStrMealThumb())
                .apply(new RequestOptions().override(0, 200))
                .into(trendingMealImage);
        trendingMealCard.setOnClickListener(v -> onMealItemClicked(meal.getIdMeal()));
    }

    @Override
    public void showCategories(List<Category> categories) {
        hideShimmer(shimmerCategories);
        rvCategory.setVisibility(View.VISIBLE);
        categoryAdapter.setList(categories);
    }

    @Override
    public void showAreas(List<Area> areas) {
        hideShimmer(shimmerAreas);
        rvArea.setVisibility(View.VISIBLE);
        areaAdapter.setList(areas);
    }

    @Override
    public void showIngredients(List<Ingredient> ingredients) {
        hideShimmer(shimmerIngredients);
        rvIngredients.setVisibility(View.VISIBLE);
        ingredientsAdapter.setList(ingredients);
        addIngredientSearchViewListener(ingredients);
    }

    private void addIngredientSearchViewListener(List<Ingredient> ingredients) {
        ingredientSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @SuppressLint("CheckResult")
            @Override
            public boolean onQueryTextChange(String newText) {
                Log.i("TAG", "onQueryTextChange: " + newText);
                if (newText.isEmpty())
                    ingredientsAdapter.setList(ingredients);
                else {
                    List<Ingredient> filteredList = new ArrayList<>();
                    Observable.fromIterable(ingredients)
                            .subscribeOn(Schedulers.io())
                            .filter(ingredient -> ingredient.getStrIngredient().toLowerCase().contains(newText.toLowerCase()))
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    filteredList::add,
                                    error -> Log.i("TAG", "onQueryTextChange: " + error.getMessage()),
                                    () -> ingredientsAdapter.setList(filteredList)
                            );
                    Log.i("TAG", "onQueryTextChange Filtered Meals: " + filteredList);
                }
                return true;
            }
        });
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

    private void hideShimmer(ShimmerFrameLayout shimmer) {
        shimmer.stopShimmer();
        shimmer.setVisibility(View.GONE);
    }

    private void setRememberMeToFalse() {
        SharedPreferences sharedPreferences =
                requireActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("rememberMe", false);
        editor.apply();
    }

}