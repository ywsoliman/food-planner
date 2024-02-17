package com.example.foodplanner.home.mealplan.view;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodplanner.R;
import com.example.foodplanner.db.MealsLocalDataSource;
import com.example.foodplanner.home.mealplan.presenter.MealPlanPresenter;
import com.example.foodplanner.home.meals.view.OnMealClickListener;
import com.example.foodplanner.models.PlannedMeal;
import com.example.foodplanner.models.Repository;
import com.example.foodplanner.network.MealsRemoteDataSource;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Calendar;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MealPlanFragment extends Fragment implements IMealPlanView, OnMealClickListener, OnPlannedMealClickListener {

    private MealPlanPresenter presenter;
    private MealPlanAdapter adapter;
    private Button dateButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_meal_plan, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dateButton = view.findViewById(R.id.dateButton);

        presenter = new MealPlanPresenter(this, Repository.getInstance(
                FirebaseAuth.getInstance(),
                MealsRemoteDataSource.getInstance(requireContext()),
                MealsLocalDataSource.getInstance(requireContext())
        ));

        adapter = new MealPlanAdapter(requireContext(), new ArrayList<>(), this, this);
        RecyclerView rvPlannedMeals = view.findViewById(R.id.rvPlannedMeals);
        rvPlannedMeals.setAdapter(adapter);
        rvPlannedMeals.setLayoutManager(new LinearLayoutManager(requireContext()));

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        presenter.getMealsByDate(year, month, dayOfMonth)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        list -> adapter.setList(list)
                );

        String currentDate = year + "/" + (month + 1) + "/" + dayOfMonth;
        dateButton.setText(currentDate);
        dateButton.setOnClickListener(v -> openDateDialog(year, month, dayOfMonth));

    }

    private void openDateDialog(int initYear, int initMonth, int initDayOfMonth) {

        Calendar calendar = Calendar.getInstance();

        // Set maximum date (a week from today)
        calendar.add(Calendar.DAY_OF_MONTH, 7);
        long maxDate = calendar.getTimeInMillis();

        DatePickerDialog dialog = new DatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String currentDate = year + "/" + (month + 1) + "/" + dayOfMonth;
                dateButton.setText(currentDate);
                presenter.getMealsByDate(year, month, dayOfMonth)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                list -> adapter.setList(list)
                        );

            }
        }, initYear, initMonth, initDayOfMonth);

        dialog.getDatePicker().setMinDate(System.currentTimeMillis());
        dialog.getDatePicker().setMaxDate(maxDate);

        dialog.show();
    }

    @Override
    public void onMealItemClicked(String mealID) {
        MealPlanFragmentDirections.ActionMealPlanFragmentToMealDetailsFragment action =
                MealPlanFragmentDirections.actionMealPlanFragmentToMealDetailsFragment(mealID);
        Navigation.findNavController(requireView()).navigate(action);
    }

    @Override
    public void onPlannedMealButtonClicked(PlannedMeal plannedMeal) {
        presenter.delete(plannedMeal);
        Snackbar.make(requireView(), R.string.meal_is_deleted_from_favorites, Snackbar.LENGTH_LONG)
                .setAnchorView(R.id.bottomNavigationView)
                .show();
    }
}