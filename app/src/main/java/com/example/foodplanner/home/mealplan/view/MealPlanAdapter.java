package com.example.foodplanner.home.mealplan.view;

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
import com.example.foodplanner.models.PlannedMeal;

import java.util.List;

public class MealPlanAdapter extends RecyclerView.Adapter<MealPlanAdapter.MealViewHolder> {

    private final Context context;
    private List<PlannedMeal> meals;
    private OnMealClickListener onMealClickListener;
    private OnPlannedMealClickListener onPlannedMealClickListener;

    public MealPlanAdapter(Context context, List<PlannedMeal> meals, OnMealClickListener onMealClickListener, OnPlannedMealClickListener onPlannedMealClickListener) {
        this.context = context;
        this.meals = meals;
        this.onMealClickListener = onMealClickListener;
        this.onPlannedMealClickListener = onPlannedMealClickListener;
    }

    @NonNull
    @Override
    public MealPlanAdapter.MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_meal_with_fab, parent, false);
        return new MealPlanAdapter.MealViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MealPlanAdapter.MealViewHolder holder, int position) {
        Meal currentMeal = meals.get(position).getMeal();
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

    public void setList(List<PlannedMeal> meals) {
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
            Button removeButton = itemView.findViewById(R.id.removeButton);
            removeButton.setOnClickListener(v -> onPlannedMealClickListener.onPlannedMealButtonClicked(meals.get(getAdapterPosition())));
            itemView.setOnClickListener(v -> onMealClickListener.onMealItemClicked(meals.get(getAdapterPosition()).getMeal().getIdMeal()));
        }
    }
}
