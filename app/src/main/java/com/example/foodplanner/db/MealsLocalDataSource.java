package com.example.foodplanner.db;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.foodplanner.models.Meal;
import com.example.foodplanner.models.PlannedMeal;

import java.util.Calendar;
import java.util.List;

public class MealsLocalDataSource implements IMealsLocalDataSource {

    private static final String TAG = "MealsLocalDataSource";
    private final FavoriteMealsDAO favDAO;
    private final PlannedMealsDao plannedDAO;
    private final LiveData<List<Meal>> localMeals;
    private LiveData<List<PlannedMeal>> localPlannedMeals;
    private static MealsLocalDataSource instance = null;

    private MealsLocalDataSource(Context context) {

        favDAO = MealsDatabase.getInstance(context).getFavoriteMealsDao();
        localMeals = favDAO.getFavoriteMeals();

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        plannedDAO = MealsDatabase.getInstance(context).getPlannedMealsDao();
        localPlannedMeals = plannedDAO.getPlannedMeals(year, month, dayOfMonth);
    }

    public static synchronized MealsLocalDataSource getInstance(Context context) {
        if (instance == null)
            instance = new MealsLocalDataSource(context);
        return instance;
    }

    @Override
    public void insertFavoriteMeal(Meal meal) {
        new Thread(() -> favDAO.insert(meal)).start();
    }

    @Override
    public void deleteFavoriteMeal(Meal meal) {
        new Thread(() -> favDAO.delete(meal)).start();
    }

    @Override
    public LiveData<List<Meal>> getLocalMeals() {
        return localMeals;
    }

    @Override
    public LiveData<List<PlannedMeal>> getLocalPlannedMeals(int year, int month, int dayOfMonth) {
        localPlannedMeals = plannedDAO.getPlannedMeals(year, month, dayOfMonth);
        return localPlannedMeals;
    }

    @Override
    public void insertPlannedMeal(PlannedMeal plannedMeal) {
        new Thread(() -> plannedDAO.insert(plannedMeal)).start();
    }

    @Override
    public void deletePlannedMeal(PlannedMeal plannedMeal) {
        new Thread(() -> plannedDAO.delete(plannedMeal)).start();
    }
}
