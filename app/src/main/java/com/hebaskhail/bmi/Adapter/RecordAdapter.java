package com.hebaskhail.bmi.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.hebaskhail.bmi.Model.Record;
import com.hebaskhail.bmi.R;
import com.hebaskhail.bmi.databinding.RowOldStatusBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder> {

    private final ArrayList<Record> localDataSet;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final RowOldStatusBinding binding;
        public ViewHolder(View view) {
            super(view);
            binding=RowOldStatusBinding.bind(view);

        }
        private void setBinding(Record record) throws ParseException {
            binding.txtKG.setText(record.getWeight()+"kg");
            binding.txtCM.setText(record.getLength()+"cm");
            binding.txtStatus.setText(record.getStatus());

            Date date=new SimpleDateFormat("yyyy-MM-dd").parse(record.getDate());
            binding.txtDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(date));
        }

    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     *                by RecyclerView.
     */
    public RecordAdapter(ArrayList<Record> dataSet) {
        localDataSet = dataSet;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_old_status, viewGroup, false);

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