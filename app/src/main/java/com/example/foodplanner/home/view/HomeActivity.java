package com.example.foodplanner.home.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.foodplanner.R;
import com.example.foodplanner.home.presenter.ForYouPresenter;
import com.example.foodplanner.models.Meal;
import com.example.foodplanner.models.Repository;
import com.example.foodplanner.network.MealsRemoteDataSource;

public class HomeActivity extends AppCompatActivity implements IHomeView {

    private TextView trendingMealName;
    private ImageView trendingMealImage;
    private ForYouPresenter forYouPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initUI();
        forYouPresenter = new ForYouPresenter(this, Repository.getInstance(MealsRemoteDataSource.getInstance()));
        forYouPresenter.getSingleRandomMeal();

    }

    private void initUI() {
        trendingMealName = findViewById(R.id.trendingMealName);
        trendingMealImage = findViewById(R.id.trendingMealImage);
    }

    @Override
    public void showSingleRandomMeal(Meal meal) {
        trendingMealName.setText(meal.getStrMeal());
        Glide.with(this)
                .load(meal.getStrMealThumb())
                .apply(new RequestOptions().override(0, 200))
                .into(trendingMealImage);
    }
}