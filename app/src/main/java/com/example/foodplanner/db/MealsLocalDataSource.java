package com.example.foodplanner.db;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.foodplanner.models.Meal;
import com.example.foodplanner.models.PlannedMeal;

import java.util.Calendar;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MealsLocalDataSource implements IMealsLocalDataSource {

    private static final String TAG = "MealsLocalDataSource";
    private final FavoriteMealsDAO favDAO;
    private final PlannedMealsDao plannedDAO;
    private final Flowable<List<Meal>> localFavMeals;
    private Flowable<List<PlannedMeal>> localPlannedMeals;
    private static MealsLocalDataSource instance = null;

    private MealsLocalDataSource(Context context) {

        favDAO = MealsDatabase.getInstance(context).getFavoriteMealsDao();
        localFavMeals = favDAO.getFavoriteMeals();

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
        favDAO.insert(meal)
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    @Override
    public void deleteFavoriteMeal(Meal meal) {
        new Thread(() -> favDAO.delete(meal)).start();
    }

    @Override
    public Flowable<List<Meal>> getLocalFavMeals() {
        return localFavMeals;
    }

    @Override
    public Flowable<List<PlannedMeal>> getLocalPlannedMeals(int year, int month, int dayOfMonth) {
        localPlannedMeals = plannedDAO.getPlannedMeals(year, month, dayOfMonth);
        return localPlannedMeals;
    }

    @Override
    public void insertPlannedMeal(PlannedMeal plannedMeal) {
        plannedDAO.insert(plannedMeal)
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    @Override
    public void deletePlannedMeal(PlannedMeal plannedMeal) {
        new Thread(() -> plannedDAO.delete(plannedMeal)).start();
    }
}
