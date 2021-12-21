package com.hebaskhail.bmi;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hebaskhail.bmi.Api.ApiClient;
import com.hebaskhail.bmi.Api.ApiUrl;
import com.hebaskhail.bmi.Model.BaseModel;
import com.hebaskhail.bmi.Model.LocalData;
import com.hebaskhail.bmi.Model.Record;
import com.hebaskhail.bmi.databinding.ActivityAddRecordBinding;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddRecordActivity extends AppCompatActivity {
    ActivityAddRecordBinding binding;
    Record record = new Record();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddRecordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (getIntent().hasExtra("date")) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date date = simpleDateFormat.parse(getIntent().getStringExtra("date"));
                record.setDate(simpleDateFormat.format(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        binding.txtDate.setText(record.getDate());
        if (getIntent().hasExtra("weight"))
            binding.editWeight.setText(getIntent().getIntExtra("weight", 0) + "");
        if (getIntent().hasExtra("length"))
            binding.editLength.setText(getIntent().getIntExtra("length", 0) + "");
        binding.txtML.setOnClickListener(v -> {
            int length = Integer.parseInt(binding.editLength.getText().toString());
            if (length != 0) {
                length--;
                binding.editLength.setText(length + "");
            }
        });
        binding.txtPL.setOnClickListener(v -> {
            int length = Integer.parseInt(binding.editLength.getText().toString());
            length++;
            binding.editLength.setText(length + "");

        });
        binding.txtMW.setOnClickListener(v -> {
            int weight = Integer.parseInt(binding.editWeight.getText().toString());
            if (weight != 0) {
                weight--;
                binding.editWeight.setText(weight + "");
            }
        });
        binding.txtPW.setOnClickListener(v -> {
            int weight = Integer.parseInt(binding.editWeight.getText().toString());

            weight++;
            binding.editWeight.setText(weight + "");

        });
        binding.txtDate.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.dialog, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    Calendar calendara = Calendar.getInstance();
                    calendara.set(Calendar.YEAR, year);
                    calendara.set(Calendar.MONTH, month);
                    calendara.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String strDate = dateFormat.format(calendara.getTime());
                    binding.txtDate.setText(strDate);
                }
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();

        });
        binding.txtTime.setOnClickListener(v -> {
            TimePickerDialog datePickerDialog = new TimePickerDialog(this, R.style.dialog, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    Calendar calendara = Calendar.getInstance();
                    calendara.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendara.set(Calendar.MINUTE, minute);
                    DateFormat dateFormat = new SimpleDateFormat("hh:mm aa");
                    binding.txtTime.setText(dateFormat.format(calendara.getTime()));
                }
            }, 12, 0, false);
            datePickerDialog.show();

        });

        binding.save.setOnClickListener(v -> {
            record.setDate(binding.txtDate.getText().toString());
            record.setLength(Integer.parseInt(binding.editLength.getText().toString()));
            record.setWeight(Integer.parseInt(binding.editWeight.getText().toString()));
            callApiAddRecord();
        });
    }

    private Dialog dialog;

    private void dialogWait() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_wait);
        dialog.show();
    }

    private void callApiAddRecord() {
        dialogWait();
        ApiClient.getClient().create(ApiUrl.class).addRecord(new LocalData(this).getToken(), record).enqueue(new Callback<BaseModel<String>>() {
            @Override
            public void onResponse(Call<BaseModel<String>> call, Response<BaseModel<String>> response) {
                dialog.dismiss();
                if (response.code() == 200) {
                    if (response.body().getCode() == 201) {
                        Toast.makeText(AddRecordActivity.this, "Added completed successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseModel<String>> call, Throwable t) {
                dialog.dismiss();
            }
        });
    }
}