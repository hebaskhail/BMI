package com.hebaskhail.bmi.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hebaskhail.bmi.Model.Food;
import com.hebaskhail.bmi.Model.Record;
import com.hebaskhail.bmi.R;
import com.hebaskhail.bmi.databinding.RowFoodBinding;
import com.hebaskhail.bmi.databinding.RowOldStatusBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AllFoodAdapter extends RecyclerView.Adapter<AllFoodAdapter.ViewHolder> {

    private ArrayList<Food> localDataSet;
    static OnClickItem onItemClick;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private RowFoodBinding binding;

        public ViewHolder(View view) {
            super(view);
            binding = RowFoodBinding.bind(view);

        }

        private void setBinding(Food food) throws ParseException {
            Glide.with(itemView.getContext()).load(food.getImage()).into(binding.imgRow);
            binding.txtNameFood.setText(food.getName());
            binding.txtCalory.setText(food.getCalory() + " " + food.getCalory_type());
            binding.txtCalory.setText(food.getCategory());
            binding.imgDelete.setOnClickListener(v -> {
                if (onItemClick != null) {
                    onItemClick.onItem(getLayoutPosition(), 1);
                }
            });
            binding.btnEdit.setOnClickListener(v -> {
                if (onItemClick != null) {
                    onItemClick.onItem(getLayoutPosition(), 0);
                }
            });

        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     *                by RecyclerView.
     */
    public AllFoodAdapter(ArrayList<Food> dataSet) {
        localDataSet = dataSet;
    }

    public void setOnItemClick(OnClickItem onItemClick) {
        this.onItemClick = onItemClick;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_food, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        try {
            viewHolder.setBinding(localDataSet.get(position));
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}