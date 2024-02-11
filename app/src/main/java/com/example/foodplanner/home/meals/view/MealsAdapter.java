package com.example.foodplanner.home.meals.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodplanner.R;
import com.example.foodplanner.models.Meal;

import java.util.List;

public class MealsAdapter extends RecyclerView.Adapter<MealsAdapter.MealViewHolder> {

    private final Context context;
    private List<Meal> meals;
    private final OnMealClickListener listener;

    public MealsAdapter(Context context, List<Meal> meals, OnMealClickListener listener) {
        this.context = context;
        this.meals = meals;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MealsAdapter.MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_meal, parent, false);
        return new MealViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MealsAdapter.MealViewHolder holder, int position) {
        Meal currentMeal = meals.get(position);
        holder.mealTitle.setText(currentMeal.getStrMeal());
        Glide.with(context)
                .load(currentMeal.getStrMealThumb() + "/preview")
                .into(holder.mealThumbnail);
    }

    @Override
    public int getItemCount() {
        return meals.size();
    }

    public void setList(List<Meal> meals) {
        this.meals = meals;
        notifyDataSetChanged();
    }

    public class MealViewHolder extends RecyclerView.ViewHolder {

        private final ImageView mealThumbnail;
        private final TextView mealTitle;

        public MealViewHolder(@NonNull View itemView) {
            super(itemView);
            mealThumbnail = itemView.findViewById(R.id.mealThumbnail);
            mealTitle = itemView.findViewById(R.id.mealTitle);
            itemView.setOnClickListener(v -> listener.onMealItemClicked(meals.get(getAdapterPosition()).getIdMeal()));
        }
    }
}
