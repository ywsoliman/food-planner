package com.example.foodplanner.home.foryou.view.category;

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
import com.example.foodplanner.models.category.Category;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private List<Category> categories;
    private final Context context;
    private final OnCategoryClickListener listener;

    public CategoryAdapter(Context context, List<Category> categories, OnCategoryClickListener listener) {
        this.context = context;
        this.categories = categories;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category currentCategory = categories.get(position);
        holder.categoryTitle.setText(currentCategory.getStrCategory());
        Glide.with(context)
                .load(currentCategory.getStrCategoryThumb())
                .apply(new RequestOptions()
                        .placeholder(R.drawable.loading_animation)
                        .error(R.drawable.ic_broken_image))
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        holder.categoryThumbnail.setImageDrawable(resource);

                        Bitmap bitmap = Bitmap.createBitmap(resource.getIntrinsicWidth(), resource.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                        Canvas canvas = new Canvas(bitmap);
                        resource.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                        resource.draw(canvas);

                        Palette.from(bitmap).generate(palette -> {
                            int dominantColor = palette.getDominantColor(ContextCompat.getColor(context, android.R.color.black));
                            holder.categoryThumbnail.setBackgroundColor(dominantColor);
                        });
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        // Handle case where Glide is unable to load the image
                        holder.categoryThumbnail.setBackgroundColor(ContextCompat.getColor(context, android.R.color.black));
                    }
                });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public void setList(List<Category> categories) {
        this.categories = categories;
        notifyDataSetChanged();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {

        private final ImageView categoryThumbnail;
        private final TextView categoryTitle;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryTitle = itemView.findViewById(R.id.categoryTitle);
            categoryThumbnail = itemView.findViewById(R.id.categoryThumbnail);
            itemView.setOnClickListener(v -> listener.onCategoryItemClicked(categories.get(getAdapterPosition()).getStrCategory()));
        }
    }
}
