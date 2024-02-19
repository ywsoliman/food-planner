package com.example.foodplanner.home.meals.details.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.foodplanner.R;

import java.util.List;

public class IngredientMealAdapter extends RecyclerView.Adapter<IngredientMealAdapter.IngredientMealViewHolder> {

    private final Context context;
    private List<Pair<String, String>> ingredients;

    public IngredientMealAdapter(Context context, List<Pair<String, String>> ingredients) {
        this.context = context;
        this.ingredients = ingredients;
    }


    public void setList(List<Pair<String, String>> ingredients) {
        this.ingredients = ingredients;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public IngredientMealAdapter.IngredientMealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_meal_ingredient, parent, false);
        return new IngredientMealViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientMealAdapter.IngredientMealViewHolder holder, int position) {
        Pair<String, String> current = ingredients.get(position);
        holder.ingredientName.setText(current.first);
        holder.ingredientMeasure.setText(current.second);
        Glide.with(context)
                .load("https://www.themealdb.com/images/ingredients/" + current.first + "-Small.png")
                .apply(new RequestOptions()
                        .placeholder(R.drawable.loading_animation)
                        .error(R.drawable.ic_broken_image))
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        holder.ingredientThumbnail.setImageDrawable(resource);

                        Bitmap bitmap = Bitmap.createBitmap(resource.getIntrinsicWidth(), resource.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                        Canvas canvas = new Canvas(bitmap);
                        resource.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                        resource.draw(canvas);

                        Palette.from(bitmap).generate(palette -> {
                            int dominantColor = palette.getDominantColor(ContextCompat.getColor(context, android.R.color.black));
                            holder.ingredientThumbnail.setBackgroundColor(dominantColor);
                        });
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        holder.ingredientThumbnail.setBackgroundColor(ContextCompat.getColor(context, android.R.color.black));
                    }
                });
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public class IngredientMealViewHolder extends RecyclerView.ViewHolder {

        private final ImageView ingredientThumbnail;
        private final TextView ingredientName;
        private final TextView ingredientMeasure;

        public IngredientMealViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredientThumbnail = itemView.findViewById(R.id.ingredientImage);
            ingredientName = itemView.findViewById(R.id.ingredientText);
            ingredientMeasure = itemView.findViewById(R.id.ingredientMeasures);
        }
    }

}
