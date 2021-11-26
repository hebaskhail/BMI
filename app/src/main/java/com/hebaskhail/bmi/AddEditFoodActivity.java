package com.hebaskhail.bmi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.hebaskhail.bmi.databinding.ActivityAddFoodBinding;

public class AddEditFoodActivity extends AppCompatActivity {

    ActivityAddFoodBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityAddFoodBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}