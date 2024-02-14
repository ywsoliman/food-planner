package com.example.foodplanner.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.foodplanner.models.Meal;

import java.util.List;

@Dao
public interface FavoriteMealsDAO {

    @Query("SELECT * FROM meals_table")
    LiveData<List<Meal>> getFavoriteMeals();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Meal meal);

    @Delete
    void delete(Meal meal);
    
}
