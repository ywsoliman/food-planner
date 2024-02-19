package com.example.foodplanner.home.favorite.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodplanner.R;
import com.example.foodplanner.home.meals.details.view.OnMealButtonClickListener;
import com.example.foodplanner.home.meals.view.OnMealClickListener;
import com.example.foodplanner.models.Meal;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.MealViewHolder> {

    private final Context context;
    private List<Meal> meals;
    private final OnMealClickListener onMealClickListener;
    private final OnMealButtonClickListener onMealButtonClickListener;

    public FavoriteAdapter(Context context, List<Meal> meals, OnMealClickListener onMealClickListener, OnMealButtonClickListener onMealButtonClickListener) {
        this.context = context;
        this.meals = meals;
        this.onMealClickListener = onMealClickListener;
        this.onMealButtonClickListener = onMealButtonClickListener;
    }

    @NonNull
    @Override
    public FavoriteAdapter.MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_meal_with_fab, parent, false);
        return new MealViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteAdapter.MealViewHolder holder, int position) {
        Meal currentMeal = meals.get(position);
        holder.mealTitle.setText(currentMeal.getStrMeal());
        Glide.with(context)
                .load(currentMeal.getStrMealThumb())
                .placeholder(R.drawable.loading_animation)
                .error(R.drawable.ic_broken_image)
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
            FloatingActionButton removeButton = itemView.findViewById(R.id.removeButton);
            removeButton.setOnClickListener(v -> onMealButtonClickListener.onMealFABClicked(meals.get(getAdapterPosition())));
            itemView.setOnClickListener(v -> onMealClickListener.onMealItemClicked(meals.get(getAdapterPosition()).getIdMeal()));
        }
    }
}
