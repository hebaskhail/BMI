package com.hebaskhail.bmi;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.hebaskhail.bmi.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {
    ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.register.setOnClickListener(v -> {
            if (binding.inputPassword.getText().toString().equals(binding.inputConfirmPassword.getText().toString())) {
                Intent intent = new Intent(this, CompleteInfoActivity.class);
                intent.putExtra("name", binding.inputName.getText().toString());
                intent.putExtra("email", binding.inputEmail.getText().toString());
                intent.putExtra("password", binding.inputPassword.getText().toString());
                startActivity(intent);
            }else {
                binding.inputConfirmPassword.setError("Password and confirm password do not match");
            }
        });
        binding.login.setOnClickListener(v -> startActivity(new Intent(RegisterActivity.this,LoginActivity.class)));

    }
}