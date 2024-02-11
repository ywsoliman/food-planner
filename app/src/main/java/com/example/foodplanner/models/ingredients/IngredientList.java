package com.example.foodplanner.models.ingredients;

import com.example.foodplanner.models.area.Area;

import java.util.List;

public class IngredientList {
    private List<Ingredient> meals;

    public List<Ingredient> getMeals() {
        return meals;
    }

    public void setMeals(List<Ingredient> meals) {
        this.meals = meals;
    }
}
