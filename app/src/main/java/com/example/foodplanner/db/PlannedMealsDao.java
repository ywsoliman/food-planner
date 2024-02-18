package com.example.foodplanner.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.foodplanner.models.PlannedMeal;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface PlannedMealsDao {

    @Query("SELECT * FROM planned_meals_table")
    Flowable<List<PlannedMeal>> getAllPlannedMeals();

    @Query("SELECT * FROM planned_meals_table WHERE year = :year AND month = :month AND day_of_month = :dayOfMonth")
    Flowable<List<PlannedMeal>> getPlannedMeals(int year, int month, int dayOfMonth);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Completable insert(PlannedMeal meal);

    @Delete
    Completable delete(PlannedMeal meal);

}
