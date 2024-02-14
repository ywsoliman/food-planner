package com.example.foodplanner.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.foodplanner.models.Meal;
import com.example.foodplanner.models.PlannedMeal;

import java.util.List;

@Dao
public interface PlannedMealsDao {

    @Query("SELECT * FROM planned_meals_table WHERE year = :year AND month = :month AND day_of_month = :dayOfMonth")
    LiveData<List<PlannedMeal>> getPlannedMeals(int year, int month, int dayOfMonth);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(PlannedMeal meal);

    @Delete
    void delete(PlannedMeal meal);

}