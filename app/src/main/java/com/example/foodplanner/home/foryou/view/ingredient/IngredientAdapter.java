package com.example.foodplanner.home.foryou.view.ingredient;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodplanner.R;
import com.example.foodplanner.models.ingredients.Ingredient;

import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {

    private final Context context;
    private List<Ingredient> ingredients;
    private final OnIngredientClickListener listener;

    public IngredientAdapter(Context context, List<Ingredient> ingredients, OnIngredientClickListener listener) {
        this.context = context;
        this.ingredients = ingredients;
        this.listener = listener;
    }

    @NonNull
    @Override
    public IngredientAdapter.IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_ingredient, parent, false);
        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientAdapter.IngredientViewHolder holder, int position) {
        Ingredient current = ingredients.get(position);
        Glide.with(context)
                .load("https://www.themealdb.com/images/ingredients/" + current.getStrIngredient() + "-Small.png")
                .placeholder(R.drawable.loading_animation)
                .error(R.drawable.ic_broken_image)
                .into(holder.ingredientThumbnail);
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public void setList(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
        notifyDataSetChanged();
    }

    public class IngredientViewHolder extends RecyclerView.ViewHolder {

        private final ImageView ingredientThumbnail;

        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredientThumbnail = itemView.findViewById(R.id.ingredientImage);
            itemView.setOnClickListener(v -> listener.onIngredientItemClicked(ingredients.get(getAdapterPosition()).getStrIngredient()));
        }
    }
}
