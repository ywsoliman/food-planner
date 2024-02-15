package com.example.foodplanner.home.foryou.view.area;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.foodplanner.R;
import com.example.foodplanner.models.area.Area;

import java.util.List;

public class AreaAdapter extends RecyclerView.Adapter<AreaAdapter.AreaViewHolder> {

    private List<Area> areas;
    private OnAreaClickListener listener;

    public AreaAdapter(List<Area> areas, OnAreaClickListener listener) {
        this.areas = areas;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AreaAdapter.AreaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_area, parent, false);
        return new AreaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AreaAdapter.AreaViewHolder holder, int position) {
        holder.areaTitle.setText(areas.get(position).getStrArea());
        holder.areaImage.setImageResource(CountryImageMap.getCountryImageResource(areas.get(position).getStrArea()));
    }

    @Override
    public int getItemCount() {
        return areas.size();
    }

    public void setList(List<Area> areas) {
        this.areas = areas;
        notifyDataSetChanged();
    }

    public class AreaViewHolder extends RecyclerView.ViewHolder {

        private final ImageView areaImage;
        private final TextView areaTitle;

        public AreaViewHolder(@NonNull View itemView) {
            super(itemView);
            areaImage = itemView.findViewById(R.id.areaImage);
            areaTitle = itemView.findViewById(R.id.areaTitle);
            areaImage.setOnClickListener(v -> listener.onAreaItemClicked(areas.get(getAdapterPosition()).getStrArea()));
        }
    }
}
