package com.example.foodplanner.home.meals.details.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodplanner.R;

import java.util.List;

public class InstructionsAdapter extends RecyclerView.Adapter<InstructionsAdapter.InstructionViewHolder> {

    List<String> instructions;

    public InstructionsAdapter(List<String> instructions) {
        this.instructions = instructions;
    }

    @NonNull
    @Override
    public InstructionsAdapter.InstructionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_step, parent, false);
        return new InstructionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InstructionsAdapter.InstructionViewHolder holder, int position) {
        String currentStep = instructions.get(position);
        String stepCounter = "Step " + (position + 1) + ": ";
        holder.stepCounter.setText(stepCounter);
        holder.stepInstruction.setText(currentStep.trim());
    }

    @Override
    public int getItemCount() {
        return instructions.size();
    }

    public void setList(List<String> instructions) {
        this.instructions = instructions;
        notifyDataSetChanged();
    }

    public class InstructionViewHolder extends RecyclerView.ViewHolder {

        private final TextView stepCounter;
        private final TextView stepInstruction;

        public InstructionViewHolder(@NonNull View itemView) {
            super(itemView);
            stepCounter = itemView.findViewById(R.id.stepCounterText);
            stepInstruction = itemView.findViewById(R.id.stepInstructions);
        }
    }
}
