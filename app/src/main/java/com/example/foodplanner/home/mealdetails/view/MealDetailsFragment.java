package com.example.foodplanner.home.mealdetails.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.foodplanner.R;
import com.example.foodplanner.home.mealdetails.presenter.MealDetailsPresenter;
import com.example.foodplanner.models.Meal;
import com.example.foodplanner.models.Repository;
import com.example.foodplanner.network.MealsRemoteDataSource;
import com.google.firebase.auth.FirebaseAuth;

public class MealDetailsFragment extends Fragment implements IMealDetailsView {

    private static final String TAG = "MealDetailsFragment";
    private MealDetailsPresenter presenter;
    private ImageView mealThumbnail;
    private TextView mealTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_meal_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initUI(view);

        presenter = new MealDetailsPresenter(this, Repository.getInstance(
                FirebaseAuth.getInstance(),
                MealsRemoteDataSource.getInstance()
        ));

        String mealID = MealDetailsFragmentArgs.fromBundle(getArguments()).getMealID();

        presenter.getMealDetails(mealID);
    }

    private void initUI(View view) {
        mealThumbnail = view.findViewById(R.id.mealThumbnail);
        mealTitle = view.findViewById(R.id.mealTitle);
    }

    @Override
    public void showMealDetails(Meal meal) {
        Log.i(TAG, "showMealDetails: " + meal.getStrMeal());
        mealTitle.setText(meal.getStrMeal());
        Glide.with(this)
                .load(meal.getStrMealThumb())
                .into(mealThumbnail);
    }
}