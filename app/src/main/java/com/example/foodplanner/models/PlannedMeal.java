package com.example.foodplanner.models;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;

@Entity(tableName = "planned_meals_table", primaryKeys = {"year", "month", "day_of_month", "id"})
public class PlannedMeal {

    @Embedded
    @NonNull
    private Meal meal;
    private int year;
    private int month;
    private int day_of_month;

    public PlannedMeal(@NonNull Meal meal) {
        this.meal = meal;
    }

    @NonNull
    public Meal getMeal() {
        return meal;
    }

    public void setMeal(@NonNull Meal meal) {
        this.meal = meal;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay_of_month() {
        return day_of_month;
    }

    public void setDay_of_month(int day_of_month) {
        this.day_of_month = day_of_month;
    }
}
