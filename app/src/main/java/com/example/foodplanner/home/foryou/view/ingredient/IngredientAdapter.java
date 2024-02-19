package com.example.foodplanner.home.foryou.view.ingredient;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
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
        holder.ingredientText.setText(current.getStrIngredient());

        Glide.with(context)
                .load("https://www.themealdb.com/images/ingredients/" + current.getStrIngredient() + "-Small.png")
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
                        // Handle case where Glide is unable to load the image
                        holder.ingredientThumbnail.setBackgroundColor(ContextCompat.getColor(context, android.R.color.black));
                    }
                });
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
        private final TextView ingredientText;

        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredientThumbnail = itemView.findViewById(R.id.ingredientImage);
            ingredientText = itemView.findViewById(R.id.ingredientText);
            itemView.setOnClickListener(v -> listener.onIngredientItemClicked(ingredients.get(getAdapterPosition()).getStrIngredient()));
        }
    }
}
