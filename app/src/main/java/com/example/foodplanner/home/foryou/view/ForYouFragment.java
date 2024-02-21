package com.example.foodplanner.home.foryou.view;

import static com.example.foodplanner.auth.login.view.LoginFragment.LOGIN;
import static com.example.foodplanner.auth.login.view.LoginFragment.PREF_NAME;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.foodplanner.R;
import com.example.foodplanner.auth.AuthActivity;
import com.example.foodplanner.db.MealsLocalDataSource;
import com.example.foodplanner.home.foryou.presenter.ForYouPresenter;
import com.example.foodplanner.home.foryou.view.area.AreaAdapter;
import com.example.foodplanner.home.foryou.view.area.OnAreaClickListener;
import com.example.foodplanner.home.foryou.view.category.CategoryAdapter;
import com.example.foodplanner.home.foryou.view.category.OnCategoryClickListener;
import com.example.foodplanner.home.foryou.view.ingredient.IngredientAdapter;
import com.example.foodplanner.home.foryou.view.ingredient.OnIngredientClickListener;
import com.example.foodplanner.home.meals.view.OnMealClickListener;
import com.example.foodplanner.home.view.HomeActivity;
import com.example.foodplanner.models.Meal;
import com.example.foodplanner.models.Repository;
import com.example.foodplanner.models.area.Area;
import com.example.foodplanner.models.category.Category;
import com.example.foodplanner.models.ingredients.Ingredient;
import com.example.foodplanner.network.FirebaseDataManager;
import com.example.foodplanner.network.MealsRemoteDataSource;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class ForYouFragment extends Fragment implements IForYouView, OnCategoryClickListener, OnAreaClickListener, OnIngredientClickListener, OnMealClickListener {

    private static final String TAG = "ForYouFragment";
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
    private Button backupButton;

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

        Log.i(TAG, "onViewCreated: ForYouFragment");

        initUI(view);

        logoutButton.setOnClickListener(v -> handleLogoutButton());
        backupButton.setOnClickListener(v -> handleBackupButton());

        forYouPresenter = new ForYouPresenter(this,
                Repository.getInstance(MealsRemoteDataSource.getInstance(requireContext()),
                        MealsLocalDataSource.getInstance(requireContext())
                ));

        setupRecyclerViews();
        requestData();
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
        backupButton = view.findViewById(R.id.backupButton);
    }

    private void setupRecyclerViews() {
        categoryAdapter = new CategoryAdapter(getContext(), new ArrayList<>(), this);
        areaAdapter = new AreaAdapter(new ArrayList<>(), this);
        ingredientsAdapter = new IngredientAdapter(getContext(), new ArrayList<>(), this);

        rvCategory.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.HORIZONTAL, false));
        rvCategory.setAdapter(categoryAdapter);

        rvArea.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.HORIZONTAL, false));
        rvArea.setAdapter(areaAdapter);

        rvIngredients.setLayoutManager(new GridLayoutManager(getContext(), 3, GridLayoutManager.HORIZONTAL, false));
        rvIngredients.setAdapter(ingredientsAdapter);
    }

    private void requestData() {
        forYouPresenter.getSingleRandomMeal();
        forYouPresenter.getCategories();
        forYouPresenter.getAreas();
        forYouPresenter.getIngredients();
    }

    private void handleLogoutButton() {
        FirebaseAuth.getInstance().signOut();
        setRememberMeToFalse();
        navigateToLogin();
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(LOGIN, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("firstLogged", false);
        editor.apply();
    }

    private void navigateToLogin() {
        Intent intent = new Intent(requireContext(), AuthActivity.class);
        startActivity(intent);
        requireActivity().finish();
    }

    private void handleBackupButton() {
        if (FirebaseDataManager.getInstance().isGuest()) {
            Toast.makeText(requireContext(), "You can't backup meals as guest", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!((HomeActivity) requireActivity()).isConnectedToInternet) {
            ((HomeActivity) requireActivity()).showNoInternetDialog();
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Are you sure you want to backup your data?");
        builder.setPositiveButton("BACKUP", (dialog, which) -> forYouPresenter.backupMeals());
        builder.setNegativeButton("CANCEL", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    @Override
    public void showSingleRandomMeal(Meal meal) {

        hideShimmer(shimmerTrendingMeal);
        trendingMealCard.setVisibility(View.VISIBLE);

        trendingMealName.setText(meal.getStrMeal());
        try {
            Glide.with(requireContext())
                    .load(meal.getStrMealThumb())
                    .apply(new RequestOptions().override(0, 200))
                    .into(trendingMealImage);
        } catch (Exception e) {
            Log.i(TAG, "showSingleRandomMeal: Exception ");
        }
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

    @Override
    public void onBackupSuccess() {
        Snackbar.make(requireView(), R.string.backed_up_meals_successfully, Snackbar.LENGTH_SHORT)
                .setAnchorView(R.id.bottomNavigationView)
                .show();
    }

    @Override
    public void onBackupFailure() {
        Toast.makeText(requireContext(), R.string.failed_to_backup_meals, Toast.LENGTH_SHORT).show();
    }

    private void addIngredientSearchViewListener(List<Ingredient> ingredients) {
        ingredientSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.i("TAG", "onQueryTextChange: " + newText);
                if (newText.isEmpty())
                    showFilteredIngredients(ingredients);
                else {
                    forYouPresenter.showFilteredIngredients(ingredients, newText);
                }
                return true;
            }
        });
    }

    @Override
    public void showFilteredIngredients(List<Ingredient> filteredList) {
        ingredientsAdapter.setList(filteredList);
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