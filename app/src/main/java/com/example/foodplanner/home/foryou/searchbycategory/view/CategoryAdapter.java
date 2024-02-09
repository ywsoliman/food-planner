package com.example.foodplanner.home.foryou.searchbycategory.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.foodplanner.R;
import com.example.foodplanner.home.foryou.searchbycategory.OnCategoryClickListener;
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
        holder.categoryDesc.setText(currentCategory.getStrCategoryDescription());
        Glide.with(context)
                .load(currentCategory.getStrCategoryThumb())
                .apply(new RequestOptions().override(0, 120))
                .into(holder.categoryThumbnail);
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
        private final TextView categoryDesc;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryTitle = itemView.findViewById(R.id.categoryTitle);
            categoryDesc = itemView.findViewById(R.id.categoryDesc);
            categoryThumbnail = itemView.findViewById(R.id.categoryThumbnail);
            itemView.setOnClickListener(v -> listener.onCategoryItemClicked(categories.get(getAdapterPosition()).getStrCategory()));
        }
    }
}
