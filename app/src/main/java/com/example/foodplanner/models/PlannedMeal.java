package com.example.foodplanner.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;

@Entity(tableName = "planned_meals_table", primaryKeys = {"year", "month", "day_of_month", "id"})
public class PlannedMeal {

    @Embedded
    @NonNull
    private Meal meal;
    private int year;
    private int month;
    @ColumnInfo(name = "day_of_month")
    private int dayOfMonth;

    public PlannedMeal() {
    }

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

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }
}
