package com.hebaskhail.bmi;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.hebaskhail.bmi.Api.ApiClient;
import com.hebaskhail.bmi.Api.ApiUrl;
import com.hebaskhail.bmi.Model.BaseModel;
import com.hebaskhail.bmi.Model.LocalData;
import com.hebaskhail.bmi.Model.User;
import com.hebaskhail.bmi.databinding.ActivityLoginBinding;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.signUp.setOnClickListener((View v) -> {
            Intent activityChange = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(activityChange);
        });
        binding.logIn.setOnClickListener(v -> {
            callApiLogin(binding.userName.getText().toString(), binding.password.getText().toString());
        });

    }

    private Dialog dialog;

    private void dialogWait() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_wait);
        dialog.show();
    }

    private void callApiLogin(String email, String password) {
        dialogWait();
        ApiClient.getClient().create(ApiUrl.class).login(new User(email, password)).enqueue(new Callback<BaseModel<User>>() {
            @Override
            public void onResponse(Call<BaseModel<User>> call, Response<BaseModel<User>> response) {
                Log.d("TAGASDGEDFGDFG", "onResponse: " + new Gson().toJson(response.body()));
                dialog.dismiss();
                if (response.code() == 200) {
                    if (response.body().getCode() == 200) {
                        if (response.body() != null) {
                            new LocalData(LoginActivity.this).saveToken(response.body().getToken());
                            if (response.body().getUser() != null)
                                new LocalData(LoginActivity.this).saveName(response.body().getUser().getName());
                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                            finish();
                        }
                    }else {
                        Toast.makeText(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseModel<User>> call, Throwable t) {
                Log.d("TAGASDGEDFGDFG", "onResponse: " + t.getMessage());
                dialog.dismiss();

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}