package com.hebaskhail.bmi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.hebaskhail.bmi.Adapter.OnLastItem;
import com.hebaskhail.bmi.Adapter.PaginationRecyclerView;
import com.hebaskhail.bmi.Adapter.RecordAdapter;
import com.hebaskhail.bmi.Api.ApiClient;
import com.hebaskhail.bmi.Api.ApiUrl;
import com.hebaskhail.bmi.Model.BaseModel;
import com.hebaskhail.bmi.Model.Home;
import com.hebaskhail.bmi.Model.LocalData;
import com.hebaskhail.bmi.Model.Record;
import com.hebaskhail.bmi.databinding.ActivityHomeBinding;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    ActivityHomeBinding binding;
    ArrayList<Record> records = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.loginWelcome.setText(new LocalData(this).getName());
        binding.recyclerStatus.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerStatus.setAdapter(new RecordAdapter(records));
        binding.btnViewFoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, AllFoodsActivity.class));
            }
        });
        binding.btnAddFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, AddEditFoodActivity.class));
            }
        });
        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LocalData(HomeActivity.this).logout();
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                finishAffinity();
            }
        });
        binding.btnAddMael.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, AddMaelActivity.class));
            }
        });
        binding.btnAddRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HomeActivity.this, AddRecordActivity.class);
                if (!records.isEmpty()) {
                    intent.putExtra("length", records.get(0).getLength());
                    intent.putExtra("weight", records.get(0).getWeight());
                    intent.putExtra("date", records.get(0).getDate());
                }
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        callApiHome(1);
    }
    private Dialog dialog;
    private void dialogWait(){
        dialog=new Dialog(this);
        dialog.setContentView(R.layout.dialog_wait);
        dialog.show();
    }
    private void callApiHome(int page) {
        dialogWait();
        ApiClient.getClient().create(ApiUrl.class).getHome(new LocalData(this).getToken(), page).enqueue(new Callback<BaseModel<Home>>() {
            @Override
            public void onResponse(Call<BaseModel<Home>> call, Response<BaseModel<Home>> response) {
                dialog.dismiss();
                if (response.code() == 200) {
                    Log.d("TAGASDASFFGDFG", "onResponse: " + response.body().getMessage());
                    if (response.body() != null)
                        if (response.body().getData() != null) {
                            String status = "";
                            switch (response.body().getData().getCurrent_status()) {
                                case "LC":
                                    status = "Little Changes";
                                    break;
                                case "SG":
                                    status = "Still Good";
                                    break;
                                case "GA":
                                    status = "Go Ahead";
                                    break;
                                case "BC":
                                    status = "Be Careful";
                                    break;
                                case "SB":
                                    status = "So Bad";
                                    break;
                            }
                            binding.txtCurrent.setText(response.body().getData().getStatus() + "(" + status + ")");
                            if (page == 1) {
                                records.clear();
                            }
                            records.addAll(response.body().getData().getRecords());
                            if (page == 1)
                                fCount = records.size();
                            binding.recyclerStatus.getAdapter().notifyDataSetChanged();
                            mangePage(records.size(), fCount, page, response.body().getTotal_pages());
                        }
                }
            }

            @Override
            public void onFailure(Call<BaseModel<Home>> call, Throwable t) {
                dialog.dismiss();

            }
        });
    }

    int fCount = 0;

    private void mangePage(int courtCount, int firsCount, int page, int lastPage) {
        PaginationRecyclerView.getInstance(binding.recyclerStatus, courtCount, firsCount, page, lastPage, (page1, currentItem) -> callApiHome(page1));
    }
}