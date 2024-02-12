package com.example.foodplanner.db;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.foodplanner.models.Meal;

import java.util.List;

public class MealsLocalDataSource implements IMealsLocalDataSource {

    private final MealsDAO dao;
    private final LiveData<List<Meal>> localMeals;
    private static MealsLocalDataSource instance = null;

    private MealsLocalDataSource(Context context) {
        dao = MealsDatabase.getInstance(context).getMealsDao();
        localMeals = dao.getAllMeals();
    }

    public static synchronized MealsLocalDataSource getInstance(Context context) {
        if (instance == null)
            instance = new MealsLocalDataSource(context);
        return instance;
    }

    @Override
    public void insert(Meal meal) {
        new Thread(() -> dao.insert(meal)).start();
    }

    @Override
    public void delete(Meal meal) {
        new Thread(() -> dao.delete(meal)).start();
    }

    @Override
    public LiveData<List<Meal>> getLocalMeals() {
        return localMeals;
    }

}
