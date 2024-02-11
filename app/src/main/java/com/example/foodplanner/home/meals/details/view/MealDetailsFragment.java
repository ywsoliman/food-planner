package com.example.foodplanner.home.meals.details.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.foodplanner.R;
import com.example.foodplanner.home.meals.details.presenter.MealDetailsPresenter;
import com.example.foodplanner.models.Meal;
import com.example.foodplanner.models.Repository;
import com.example.foodplanner.network.MealsRemoteDataSource;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Arrays;

public class MealDetailsFragment extends Fragment implements IMealDetailsView {

    private static final String TAG = "MealDetailsFragment";
    private MealDetailsPresenter presenter;
    private ImageView mealThumbnail;
    private TextView mealTitle;
    private TextView mealCategory;
    private TextView mealArea;
    private RecyclerView rvInstructions;
    private InstructionsAdapter instructionsAdapter;

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
                MealsRemoteDataSource.getInstance(requireContext())
        ));

        instructionsAdapter = new InstructionsAdapter(new ArrayList<>());

        rvInstructions.setAdapter(instructionsAdapter);
        rvInstructions.setLayoutManager(new LinearLayoutManager(getContext()));

        String mealID = MealDetailsFragmentArgs.fromBundle(getArguments()).getMealID();
        presenter.getMealDetails(mealID);
    }

    private void initUI(View view) {
        mealThumbnail = view.findViewById(R.id.mealThumbnail);
        mealTitle = view.findViewById(R.id.mealTitle);
        rvInstructions = view.findViewById(R.id.rvInstructions);
        mealCategory = view.findViewById(R.id.mealCategory);
        mealArea = view.findViewById(R.id.mealArea);
    }

    @Override
    public void showMealDetails(Meal meal) {

        mealTitle.setText(meal.getStrMeal());
        mealCategory.setText(meal.getStrCategory());
        mealArea.setText(meal.getStrArea());
        Glide.with(this)
                .load(meal.getStrMealThumb())
                .into(mealThumbnail);

        String instructions = meal.getStrInstructions().replaceAll("([0-9]\\.)|\\r|\\n|\\t", "");
        instructionsAdapter.setList(Arrays.asList(instructions.split("\\.")));
    }
}