package com.example.foodplanner.home.foryou.view.area;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.foodplanner.R;
import com.example.foodplanner.models.area.Area;

import java.util.List;

public class AreaAdapter extends RecyclerView.Adapter<AreaAdapter.AreaViewHolder> {

    private final Context context;
    private List<Area> areas;
    private OnAreaClickListener listener;

    public AreaAdapter(Context context, List<Area> areas, OnAreaClickListener listener) {
        this.context = context;
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
        Glide.with(context)
                .load(CountryImageMap.getCountryImageResource(areas.get(position).getStrArea()))
                .apply(new RequestOptions().override(256, 256))
                .placeholder(R.drawable.loading_animation)
                .error(R.drawable.ic_broken_image)
                .into(holder.areaImage);
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

        public AreaViewHolder(@NonNull View itemView) {
            super(itemView);
            areaImage = itemView.findViewById(R.id.areaImage);
            areaImage.setOnClickListener(v -> listener.onAreaItemClicked(areas.get(getAdapterPosition()).getStrArea()));
        }
    }
}
