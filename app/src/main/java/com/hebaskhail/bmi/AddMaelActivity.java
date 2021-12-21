package com.hebaskhail.bmi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.hebaskhail.bmi.Api.ApiClient;
import com.hebaskhail.bmi.Api.ApiUrl;
import com.hebaskhail.bmi.Model.AddMeal;
import com.hebaskhail.bmi.Model.AllFoodApi;
import com.hebaskhail.bmi.Model.BaseModel;
import com.hebaskhail.bmi.Model.Food;
import com.hebaskhail.bmi.Model.LocalData;
import com.hebaskhail.bmi.databinding.ActivityAddModeBinding;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddMaelActivity extends AppCompatActivity {
    ActivityAddModeBinding binding;
    AddMeal addMeal = new AddMeal();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddModeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
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
        binding.btnReset.setOnClickListener(v -> {
            binding.spinnerFood.setSelection(0);
            binding.txtDate.setText("");
            binding.editAmount.setText("");
            binding.txtTime.setText("");
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
            addMeal.setAmount(Integer.parseInt(binding.editAmount.getText().toString()));
            addMeal.setDate(binding.txtDate.getText().toString());
            addMeal.setFood_id(foood_id);
            callApiAddMeal();
        });
        callApiAllFoods(1);
    }

    String foood_id = "";
    private Dialog dialog;

    private void dialogWait() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_wait);
        dialog.show();
    }

    private void callApiAllFoods(int page) {
        dialogWait();
        ApiClient.getClient().create(ApiUrl.class).getAllFoods(new LocalData(this).getToken(), page, 100).enqueue(new Callback<BaseModel<AllFoodApi>>() {
            @Override
            public void onResponse(Call<BaseModel<AllFoodApi>> call, Response<BaseModel<AllFoodApi>> response) {

                dialog.dismiss();
                if (response.code() == 200) {
                    Log.d("TAGASDASFFGDFG", "onResponse: " + response.body().getMessage());
                    if (response.body() != null)
                        if (response.body().getData() != null) {
                            binding.spinnerFood.setAdapter(new ArrayAdapter(AddMaelActivity.this, android.R.layout.simple_spinner_item, response.body().getData().getFoods()));
                            binding.spinnerFood.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    foood_id = response.body().getData().getFoods().get(position).get_id();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                        }
                }
            }

            @Override
            public void onFailure(Call<BaseModel<AllFoodApi>> call, Throwable t) {
                dialog.dismiss();
            }
        });
    }

    private void callApiAddMeal() {
        dialogWait();
        ApiClient.getClient().create(ApiUrl.class).addMeal(new LocalData(this).getToken(), addMeal).enqueue(new Callback<BaseModel<String>>() {
            @Override
            public void onResponse(Call<BaseModel<String>> call, Response<BaseModel<String>> response) {
                dialog.dismiss();
                if (response.code() == 200) {
                    if (response.body().getCode() == 200) {
                        Toast.makeText(AddMaelActivity.this, "Added completed successfully", Toast.LENGTH_SHORT).show();
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