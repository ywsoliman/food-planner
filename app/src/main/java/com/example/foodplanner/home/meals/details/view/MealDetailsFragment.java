package com.example.foodplanner.home.meals.details.view;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodplanner.R;
import com.example.foodplanner.db.MealsLocalDataSource;
import com.example.foodplanner.home.meals.details.presenter.MealDetailsPresenter;
import com.example.foodplanner.home.view.HomeActivity;
import com.example.foodplanner.models.Meal;
import com.example.foodplanner.models.PlannedMeal;
import com.example.foodplanner.models.Repository;
import com.example.foodplanner.network.MealsRemoteDataSource;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class MealDetailsFragment extends Fragment implements IMealDetailsView {

    private MealDetailsPresenter presenter;
    private TextView mealTitle;
    private ImageView mealThumbnail;
    private TextView mealCategory;
    private TextView mealArea;
    private RecyclerView rvInstructions;
    private InstructionsAdapter instructionsAdapter;
    private RecyclerView rvMealIngredients;
    private IngredientMealAdapter ingredientMealAdapter;
    private FloatingActionButton addToCalendarButton;
    private FloatingActionButton addToFavButton;
    private Meal meal;
    private PlannedMeal plannedMeal;
    private YouTubePlayerView youtubeVideo;

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
                MealsRemoteDataSource.getInstance(requireContext()),
                MealsLocalDataSource.getInstance(getContext())
        ));

        instructionsAdapter = new InstructionsAdapter(new ArrayList<>());
        ingredientMealAdapter = new IngredientMealAdapter(getContext(), new ArrayList<>());

        rvInstructions.setAdapter(instructionsAdapter);
        rvInstructions.setLayoutManager(new LinearLayoutManager(getContext()));

        rvMealIngredients.setAdapter(ingredientMealAdapter);
        rvMealIngredients.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        String mealID = MealDetailsFragmentArgs.fromBundle(getArguments()).getMealID();
        presenter.getMealDetails(mealID);

        addToFavButton.setOnClickListener(v -> handleAddToFavorite());
        addToCalendarButton.setOnClickListener(v -> handleAddToCalendar());

    }

    private void initUI(View view) {
        mealThumbnail = view.findViewById(R.id.mealThumbnail);
        mealTitle = view.findViewById(R.id.mealTitle);
        rvInstructions = view.findViewById(R.id.rvInstructions);
        rvMealIngredients = view.findViewById(R.id.rvMealIngredients);
        mealCategory = view.findViewById(R.id.mealCategory);
        mealArea = view.findViewById(R.id.mealArea);
        addToFavButton = view.findViewById(R.id.addToFavButton);
        addToCalendarButton = view.findViewById(R.id.addToCalendarButton);
        youtubeVideo = view.findViewById(R.id.youtubeVideo);
    }

    private void handleAddToFavorite() {
        if (((HomeActivity) requireActivity()).isGuest()) {
            ((HomeActivity) requireActivity()).showGuestDialog();
            return;
        }
        meal.setEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        presenter.insertMealToFavorites(meal);
        Snackbar.make(requireView(), R.string.meal_is_added_to_favorites_successfully, Snackbar.LENGTH_SHORT)
                .setAnchorView(R.id.bottomNavigationView)
                .show();
    }

    private void handleAddToCalendar() {
        if (((HomeActivity) requireActivity()).isGuest()) {
            ((HomeActivity) requireActivity()).showGuestDialog();
            return;
        }
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        // Set maximum date (a week from today)
        calendar.add(Calendar.DAY_OF_MONTH, 7);
        long maxDate = calendar.getTimeInMillis();

        DatePickerDialog dialog = new DatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                plannedMeal.setYear(year);
                plannedMeal.setMonth(month);
                plannedMeal.setDayOfMonth(dayOfMonth);
                plannedMeal.getMeal().setEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                presenter.insertMealOnDate(plannedMeal);
                Snackbar.make(requireView(), R.string.meal_added_to_calendar_successfully, Snackbar.LENGTH_SHORT)
                        .setAnchorView(R.id.bottomNavigationView)
                        .show();
            }
        }, year, month, dayOfMonth);

        dialog.getDatePicker().setMinDate(System.currentTimeMillis());
        dialog.getDatePicker().setMaxDate(maxDate);

        dialog.show();
    }

    @Override
    public void showMealDetails(Meal meal) {

        this.meal = meal;
        plannedMeal = new PlannedMeal(meal);

        mealTitle.setText(meal.getStrMeal());
        mealCategory.setText(meal.getStrCategory());
        mealArea.setText(meal.getStrArea());
        Glide.with(this)
                .load(meal.getStrMealThumb())
                .into(mealThumbnail);

        List<Pair<String, String>> pairList = getIngredientNameWithMeasure(meal);
        ingredientMealAdapter.setList(pairList);

        String instructions = meal.getStrInstructions().replaceAll("([0-9]\\.)|\\r|\\n|\\t", "");
        instructionsAdapter.setList(Arrays.asList(instructions.trim().split("\\.")));

        setupYoutubeVideo(meal);

    }

    private void setupYoutubeVideo(Meal meal) {
        getLifecycle().addObserver(youtubeVideo);
        String mealVideoId = meal.getStrYoutube().substring(meal.getStrYoutube().indexOf('=') + 1);
        youtubeVideo.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                super.onReady(youTubePlayer);
                youTubePlayer.cueVideo(mealVideoId, 0);
            }
        });
    }

    private List<Pair<String, String>> getIngredientNameWithMeasure(Meal meal) {
        List<Pair<String, String>> pairList = new ArrayList<>();
        try {
            for (int i = 1; i <= 20; i++) {
                Field ingredientField = Meal.class.getDeclaredField("strIngredient" + i);
                Field measureField = Meal.class.getDeclaredField("strMeasure" + i);
                ingredientField.setAccessible(true);
                measureField.setAccessible(true);

                String ingredient = (String) ingredientField.get(meal);
                String measure = (String) measureField.get(meal);

                if (ingredient != null && !ingredient.isEmpty()) {
                    measure = (measure != null && !measure.isEmpty() && !measure.trim().equals("")) ? measure : "N/A";
                    pairList.add(Pair.create(ingredient, measure));
                }
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        return pairList;
    }
}