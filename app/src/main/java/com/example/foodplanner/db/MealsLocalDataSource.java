package com.example.foodplanner.db;

import android.content.Context;

import com.example.foodplanner.models.Meal;
import com.example.foodplanner.models.PlannedMeal;

import java.util.Calendar;
import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

public class MealsLocalDataSource implements IMealsLocalDataSource {

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
    public Completable insertFavoriteMeal(Meal meal) {
        return favDAO.insert(meal);
    }

    @Override
    public Completable deleteFavoriteMeal(Meal meal) {
        return favDAO.delete(meal);
    }

    @Override
    public Flowable<List<Meal>> getLocalFavMeals() {
        return localFavMeals;
    }

    @Override
    public Flowable<List<PlannedMeal>> getLocalPlannedMeals() {
        return plannedDAO.getAllPlannedMeals();
    }

    @Override
    public Flowable<List<PlannedMeal>> getLocalPlannedMeals(int year, int month, int dayOfMonth) {
        localPlannedMeals = plannedDAO.getPlannedMeals(year, month, dayOfMonth);
        return localPlannedMeals;
    }

    @Override
    public Completable insertPlannedMeal(PlannedMeal plannedMeal) {
        return plannedDAO.insert(plannedMeal);
    }

    @Override
    public Completable deletePlannedMeal(PlannedMeal plannedMeal) {
        return plannedDAO.delete(plannedMeal);
    }

    @Override
    public Completable replaceFavoriteMeals(List<Meal> meals) {
        return favDAO.deleteAllMeals()
                .andThen(favDAO.insertAll(meals));
    }

    @Override
    public Completable replacePlannedMeals(List<PlannedMeal> plannedMeals) {
        return plannedDAO.deleteAllMeals()
                .andThen(plannedDAO.insertAll(plannedMeals));
    }
}
