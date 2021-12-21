package com.hebaskhail.bmi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;

import com.hebaskhail.bmi.Api.ApiClient;
import com.hebaskhail.bmi.Api.ApiUrl;
import com.hebaskhail.bmi.Model.BaseModel;
import com.hebaskhail.bmi.Model.User;
import com.hebaskhail.bmi.databinding.ActivityCompleteInfoBinding;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompleteInfoActivity extends AppCompatActivity {
    ActivityCompleteInfoBinding binding;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCompleteInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        user=new User();
        user.setEmail(getIntent().getStringExtra("email"));
        user.setName(getIntent().getStringExtra("name"));
        user.setPassword(getIntent().getStringExtra("password"));
        binding.txtDOB.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog =new DatePickerDialog(this, R.style.dialog, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    Calendar calendara=Calendar.getInstance();
                    calendara.set(Calendar.YEAR,year);
                    calendara.set(Calendar.MONTH,month);
                    calendara.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String strDate = dateFormat.format(calendara.getTime());
                    binding.txtDOB.setText(strDate);
                }
            },1990,1,1);
            datePickerDialog.show();

        });
        binding.txtMLength.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int length=Integer.parseInt( binding.editTextLength.getText().toString());
                if (length!=0) {
                    length--;
                    binding.editTextLength.setText(length+"");
                }
            }
        });
        binding.txtPlusLength.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int length=Integer.parseInt( binding.editTextLength.getText().toString());

                    length++;
                    binding.editTextLength.setText(length+"");

            }
        });
        binding.txtMWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int weight=Integer.parseInt( binding.editTextWeight.getText().toString());
                if (weight!=0) {
                    weight--;
                    binding.editTextWeight.setText(weight+"");
                }
            }
        });
        binding.txtPlusWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int weight=Integer.parseInt( binding.editTextWeight.getText().toString());

                    weight++;
                    binding.editTextWeight.setText(weight+"");

            }
        });
        binding.register.setOnClickListener(v ->{
            user.setWeight(Integer.parseInt( binding.editTextWeight.getText().toString()));
            user.setLength(Integer.parseInt( binding.editTextLength.getText().toString()));
            if (binding.radioMale.isChecked()){
                user.setGender("male");
            }else {
                user.setGender("female");
            }
            user.setDateOfBirth(binding.txtDOB.getText().toString());
            callApiRegister();
        });

    }
    private Dialog dialog;
    private void dialogWait(){
        dialog=new Dialog(this);
        dialog.setContentView(R.layout.dialog_wait);
        dialog.show();
    }
    private void callApiRegister(){
        dialogWait();
        ApiClient.getClient().create(ApiUrl.class).createUser(user).enqueue(new Callback<BaseModel<User>>() {
            @Override
            public void onResponse(Call<BaseModel<User>> call, Response<BaseModel<User>> response) {
                dialog.dismiss();
                if (response.code()==200){
                    if (response.body().getCode()==201){
                        startActivity(new Intent(CompleteInfoActivity.this,LoginActivity.class));
                        finishAffinity();
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseModel<User>> call, Throwable t) {
                dialog.dismiss();
            }
        });
    }
}