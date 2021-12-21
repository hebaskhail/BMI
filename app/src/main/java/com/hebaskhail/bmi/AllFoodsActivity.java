package com.hebaskhail.bmi;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.hebaskhail.bmi.Adapter.AllFoodAdapter;
import com.hebaskhail.bmi.Adapter.PaginationRecyclerView;
import com.hebaskhail.bmi.Api.ApiClient;
import com.hebaskhail.bmi.Api.ApiUrl;
import com.hebaskhail.bmi.Model.AllFoodApi;
import com.hebaskhail.bmi.Model.BaseModel;
import com.hebaskhail.bmi.Model.Food;
import com.hebaskhail.bmi.Model.LocalData;
import com.hebaskhail.bmi.databinding.ActivityAllFoodsBinding;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllFoodsActivity extends AppCompatActivity {
    ActivityAllFoodsBinding binding;
    ArrayList<Food> foods = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAllFoodsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.recyclerFoods.setLayoutManager(new LinearLayoutManager(this));
        AllFoodAdapter adapter = new AllFoodAdapter(foods);
        binding.recyclerFoods.setAdapter(adapter);
        adapter.setOnItemClick((pos, type) -> {
            if (type == 1) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AllFoodsActivity.this);
                builder.setMessage("Are you sure to delete this item?");
                builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        callApiDeleteFoods(foods.get(pos).get_id());
                    }
                });

                builder.setNegativeButton(android.R.string.no, (dialog, which) -> dialog.dismiss());
                builder.show();

            }
            if (type==0){
                Intent intent =new Intent(AllFoodsActivity.this,AddEditFoodActivity.class);
                intent.putExtra("isEdit",true);
                intent.putExtra("data",foods.get(pos));
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        callApiAllFoods(1);
    }
    private Dialog dialog;
    private void dialogWait(){
        dialog=new Dialog(this);
        dialog.setContentView(R.layout.dialog_wait);
        dialog.show();
    }
    private void callApiAllFoods(int page) {
        dialogWait();
        ApiClient.getClient().create(ApiUrl.class).getAllFoods(new LocalData(this).getToken(), page,15).enqueue(new Callback<BaseModel<AllFoodApi>>() {
            @Override
            public void onResponse(Call<BaseModel<AllFoodApi>> call, Response<BaseModel<AllFoodApi>> response) {
                dialog.dismiss();
                if (response.code() == 200) {
                    Log.d("TAGASDASFFGDFG", "onResponse: " + response.body().getMessage());
                    if (response.body() != null)
                        if (response.body().getData() != null) {

                            if (page == 1) {
                                foods.clear();
                            }
                            foods.addAll(response.body().getData().getFoods());
                            if (page == 1)
                                fCount = foods.size();
                            binding.recyclerFoods.getAdapter().notifyDataSetChanged();
                            mangePage(foods.size(), fCount, page, response.body().getTotal_pages());
                        }
                }
            }

            @Override
            public void onFailure(Call<BaseModel<AllFoodApi>> call, Throwable t) {
                dialog.dismiss();
            }
        });
    }

    private void callApiDeleteFoods(String id) {
        dialogWait();
        ApiClient.getClient().create(ApiUrl.class).deleteFoods(id,new LocalData(this).getToken()).enqueue(new Callback<BaseModel<AllFoodApi>>() {
            @Override
            public void onResponse(Call<BaseModel<AllFoodApi>> call, Response<BaseModel<AllFoodApi>> response) {
                Log.d("TAGASDASFFGDFGasdasdasd", "onResponse: " + response.code());
                dialog.dismiss();
                if (response.code() == 200) {
                    Log.d("TAGASDASFFGDFGasdasdasd", "onResponse: " + response.body().getMessage());
                    callApiAllFoods(1);
                }
            }

            @Override
            public void onFailure(Call<BaseModel<AllFoodApi>> call, Throwable t) {
                Log.d("TAGASDASFFGDFGasdasdasd", "onResponse: " + t.getMessage());
                dialog.dismiss();
            }
        });
    }

    int fCount = 0;

    private void mangePage(int courtCount, int firsCount, int page, int lastPage) {
        PaginationRecyclerView.getInstance(binding.recyclerFoods, courtCount, firsCount, page, lastPage, (page1, currentItem) -> callApiAllFoods(page1));
    }
}