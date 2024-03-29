package com.example.foodplanner.home.view;

import static com.example.foodplanner.auth.login.view.LoginFragment.LOGIN;
import static com.example.foodplanner.auth.login.view.LoginFragment.PREF_NAME;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.foodplanner.R;
import com.example.foodplanner.auth.AuthActivity;
import com.example.foodplanner.db.MealsLocalDataSource;
import com.example.foodplanner.home.presenter.HomePresenter;
import com.example.foodplanner.models.Repository;
import com.example.foodplanner.network.MealsRemoteDataSource;
import com.example.foodplanner.network.NetworkChangeReceiver;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity implements NetworkChangeReceiver.NetworkChangeListener, IHomeView {

    private static final String TAG = "HomeActivity";
    private NetworkChangeReceiver networkChangeReceiver;
    private NavController navController;
    private ConstraintLayout noInternetBanner;
    private BottomNavigationView bottomNavigationView;
    private HomePresenter homePresenter;
    public boolean isConnectedToInternet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        homePresenter = new HomePresenter(this, Repository.getInstance(
                MealsRemoteDataSource.getInstance(this),
                MealsLocalDataSource.getInstance(this)
        ));

        synchronizeMeals();

        rememberUser();

        noInternetBanner = findViewById(R.id.noInternetBanner);

        networkChangeReceiver = new NetworkChangeReceiver(this);
        registerReceiver(networkChangeReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        navController = Navigation.findNavController(this, R.id.home_nav_host_fragment);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        setupButtonNavigation();

    }

    @Override
    public void synchronizeMeals() {
        SharedPreferences sharedPreferences = getSharedPreferences(LOGIN, Context.MODE_PRIVATE);
        boolean isFirstLogged = sharedPreferences.getBoolean("firstLogged", false);
        if (isFirstLogged) {
            Log.i(TAG, "HomeActivity synchronizeMeals: ");
            homePresenter.synchronizeMeals();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("firstLogged", false);
            editor.apply();
        }
    }

    private void rememberUser() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("rememberMe", true);
        editor.apply();
    }

    private void setupButtonNavigation() {
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

    @Override
    public void showGuestDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sign Up for More Features");
        builder.setMessage("Add your food preferences, plan your meals and more!");
        builder.setPositiveButton("SIGN UP", (dialog, which) -> navigateToSignup());
        builder.setNegativeButton("CANCEL", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    @Override
    public void showNoInternetDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enable Internet for More Features");
        builder.setMessage("Turn on Wi-Fi or cellular data to enjoy this feature.");
        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void navigateToSignup() {
        FirebaseAuth.getInstance().signOut();
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("rememberMe", false);
        editor.apply();
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
    }
}