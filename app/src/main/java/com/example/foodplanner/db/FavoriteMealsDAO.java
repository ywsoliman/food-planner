package com.example.foodplanner.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.foodplanner.models.Meal;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface FavoriteMealsDAO {

    @Query("SELECT * FROM meals_table")
    Flowable<List<Meal>> getFavoriteMeals();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Completable insert(Meal meal);

    @Delete
    Completable delete(Meal meal);

}
