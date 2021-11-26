package com.hebaskhail.bmi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.hebaskhail.bmi.databinding.ActivityCompleteInfoBinding;

public class CompleteInfoActivity extends AppCompatActivity {
    ActivityCompleteInfoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCompleteInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.register.setOnClickListener(v ->{
            startActivity(new Intent(this,LoginActivity.class));
        });

    }
}