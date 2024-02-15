package com.example.foodplanner.home.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.foodplanner.R;
import com.example.foodplanner.auth.AuthActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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
            } else if (itemId == R.id.favoriteFragment || itemId == R.id.mealPlanFragment) {
                if (!checkIsGuest())
                    navigateToFragment(itemId);
            } else if (itemId == R.id.searchFragment) {
                navigateToFragment(R.id.searchFragment);
            }
            return true;
        });


    }

    public boolean checkIsGuest() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null && user.isAnonymous()) {
            showDialog();
            return true;
        }
        return false;
    }

    private void navigateToFragment(int fragmentId) {
        if (navController.getCurrentDestination().getId() != fragmentId) {
            navController.popBackStack(R.id.forYouFragment, false); // Consider the necessity based on your app's flow
            navController.navigate(fragmentId);
        }
    }

    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sign Up for More Features");
        builder.setMessage("Add your food preferences, plan your meals and more!");
        builder.setPositiveButton("SIGN UP", (dialog, which) -> navigateToSignup());
        builder.setNegativeButton("CANCEL", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    public void navigateToSignup() {
        Intent intent = new Intent(this, AuthActivity.class);
        startActivity(intent);
        finish();
    }

}