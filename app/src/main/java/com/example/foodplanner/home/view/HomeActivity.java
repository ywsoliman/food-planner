package com.example.foodplanner.home.view;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.foodplanner.R;
import com.example.foodplanner.auth.AuthActivity;
import com.example.foodplanner.network.MealsRemoteDataSource;
import com.example.foodplanner.network.NetworkChangeReceiver;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity implements NetworkChangeReceiver.NetworkChangeListener {

    private static final String TAG = "HomeActivity";
    private NetworkChangeReceiver networkChangeReceiver;
    private NavController navController;
    private ConstraintLayout noInternetBanner;
    private boolean isConnectedToInternet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Log.i(TAG, "onCreate: Is Firebase Signed? " + FirebaseAuth.getInstance().getCurrentUser());

        noInternetBanner = findViewById(R.id.noInternetBanner);

        networkChangeReceiver = new NetworkChangeReceiver(this);
        registerReceiver(networkChangeReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        navController = Navigation.findNavController(this, R.id.home_nav_host_fragment);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (!isConnectedToInternet) {
                if (itemId == R.id.mealPlanFragment || itemId == R.id.favoriteFragment) {
                    navigateToFragment(itemId);
                } else {
                    showNoInternetDialog();
                    return false;
                }
            } else if (isGuest()) {
                if (itemId == R.id.forYouFragment || itemId == R.id.searchFragment) {
                    navigateToFragment(itemId);
                } else {
                    showGuestDialog();
                    return false;
                }
            } else {
                navigateToFragment(itemId);
            }
            return true;
        });

//        bottomNavigationView.setOnItemSelectedListener(item -> {
//            int itemId = item.getItemId();
//            if (itemId == R.id.forYouFragment) {
//                navigateToFragment(R.id.forYouFragment);
//            } else if (itemId == R.id.favoriteFragment) {
//                navigateToFragment(R.id.favoriteFragment);
//            } else if (itemId == R.id.mealPlanFragment) {
//                navigateToFragment(R.id.mealPlanFragment);
//            } else if (itemId == R.id.searchFragment) {
//                navigateToFragment(R.id.searchFragment);
//            }
//            return true
//                    ;
//        });


    }

    public boolean isGuest() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return user != null && user.isAnonymous();
    }

    private void navigateToFragment(int fragmentId) {
        if (navController.getCurrentDestination().getId() != fragmentId) {
            if (fragmentId == R.id.forYouFragment)
                navController.popBackStack(R.id.forYouFragment, true); // Consider the necessity based on your app's flow
            else
                navController.popBackStack(R.id.forYouFragment, false);
            navController.navigate(fragmentId);
        }
    }

    public void showGuestDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sign Up for More Features");
        builder.setMessage("Add your food preferences, plan your meals and more!");
        builder.setPositiveButton("SIGN UP", (dialog, which) -> navigateToSignup());
        builder.setNegativeButton("CANCEL", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    public void showNoInternetDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enable Internet for More Features");
        builder.setMessage("Turn on Wi-Fi or cellular data to enjoy this feature.");
        builder.setPositiveButton("SIGN UP", (dialog, which) -> navigateToSignup());
        builder.setNegativeButton("CANCEL", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    public void navigateToSignup() {
        Intent intent = new Intent(this, AuthActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onNetworkChanged(boolean isConnected) {
        this.isConnectedToInternet = isConnected;
        if (isConnected)
            noInternetBanner.setVisibility(View.GONE);
        else
            noInternetBanner.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: ");
        unregisterReceiver(networkChangeReceiver);
        MealsRemoteDataSource.getInstance(this).dispose();
    }
}