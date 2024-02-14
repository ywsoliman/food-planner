package com.example.foodplanner.home.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.foodplanner.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        navController = Navigation.findNavController(this, R.id.home_nav_host_fragment);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.forYouFragment) {
                navigateToFragment(R.id.forYouFragment);
            } else if (itemId == R.id.favoriteFragment) {
                navigateToFragment(R.id.favoriteFragment);
            } else if (itemId == R.id.mealPlanFragment) {
                navigateToFragment(R.id.mealPlanFragment);
            } else if (itemId == R.id.searchFragment) {
                navigateToFragment(R.id.searchFragment);
            }
            return true;

        });


    }

    private void navigateToFragment(int fragmentId) {
        if (navController.getCurrentDestination().getId() != fragmentId) {
            navController.popBackStack(R.id.forYouFragment, false); // Consider the necessity based on your app's flow
            navController.navigate(fragmentId);
        }
    }

}