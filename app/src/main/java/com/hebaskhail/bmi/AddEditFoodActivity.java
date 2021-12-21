package com.hebaskhail.bmi;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hebaskhail.bmi.Api.ApiClient;
import com.hebaskhail.bmi.Api.ApiUrl;
import com.hebaskhail.bmi.Model.BaseModel;
import com.hebaskhail.bmi.Model.Food;
import com.hebaskhail.bmi.Model.LocalData;
import com.hebaskhail.bmi.databinding.ActivityAddFoodBinding;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import droidninja.filepicker.utils.ContentUriUtils;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddEditFoodActivity extends AppCompatActivity {

    ActivityAddFoodBinding binding;
    List<String> category;
    List<String> caloryType;
    Food food;
    File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddFoodBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        category = Arrays.asList(getResources().getStringArray(R.array.category));
        caloryType = Arrays.asList(getResources().getStringArray(R.array.calory_type));
        if (getIntent().getBooleanExtra("isEdit", false)) {
            binding.titlePage.setText("Edit Food Details");
        }
        food = (Food) getIntent().getSerializableExtra("data");
        if (food == null) {
            food = new Food();
        } else {
            binding.editName.setText(food.getName());
            binding.editCalory.setText(food.getCalory() + "");
            binding.spinnerCalory.setSelection(caloryType.indexOf(food.getCalory_type()));
            binding.spinnerCategory.setSelection(category.indexOf(food.getCategory()));
            Glide.with(this).load(food.getImage()).into(binding.imageView);
        }
        binding.btnAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(
                        AddEditFoodActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) ==
                        PackageManager.PERMISSION_GRANTED) {
                    try {
                        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(galleryIntent, 500);
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(),
                                e.getMessage() + "ye show hora h",
                                Toast.LENGTH_LONG).show();
                        Log.e(e.getClass().getName(), e.getMessage(), e);
                    }
                } else {
                    // You can directly ask for the permission.
                    // The registered ActivityResultCallback gets the result of this request.
                    requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                }


            }
        });

        binding.save2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                food.setName(binding.editName.getText().toString());
                food.setCategory(binding.spinnerCategory.getSelectedItem().toString());
                food.setCalory(Integer.parseInt(binding.editCalory.getText().toString()));
                food.setCalory_type(binding.spinnerCalory.getSelectedItem().toString());
                if (getIntent().getBooleanExtra("isEdit", false)) {
                    callApiEditFood();
                } else {
                    callApiAddFood();
                }
            }
        });

    }

    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    try {
                        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(galleryIntent, 500);
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(),
                                e.getMessage() + "ye show hora h",
                                Toast.LENGTH_LONG).show();
                        Log.e(e.getClass().getName(), e.getMessage(), e);
                    }
                } else {
                }
            });

    @SuppressLint("MissingSuperCall")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 500) {
            file = new File(getRealPathFromURI(data.getData(), AddEditFoodActivity.this));
            Glide.with(this).load(file).into(binding.imageView);
        }

    }

    MediaType MEDIA_TYPE_IMAGE = MediaType.parse("image/*");
    MediaType MEDIA_TYPE_TEXT = MediaType.parse("text/plain");
    private Dialog dialog;
    private void dialogWait(){
        dialog=new Dialog(this);
        dialog.setContentView(R.layout.dialog_wait);
        dialog.show();
    }
    public void callApiEditFood() {
        dialogWait();
        RequestBody name = RequestBody.create(MEDIA_TYPE_TEXT, food.getName());
        RequestBody category = RequestBody.create(MEDIA_TYPE_TEXT, food.getCategory());
        RequestBody caloryType = RequestBody.create(MEDIA_TYPE_TEXT, food.getCalory_type());
        RequestBody image = RequestBody.create(MEDIA_TYPE_IMAGE, file);
        MultipartBody.Part fileImage = MultipartBody.Part.createFormData("image",file.getName(),image);
        ApiClient.getClient().create(ApiUrl.class).editFood(food.get_id(), new LocalData(AddEditFoodActivity.this).getToken(), name, category
                , food.getCalory(), caloryType, fileImage).enqueue(new Callback<BaseModel<String>>() {
            @Override
            public void onResponse(Call<BaseModel<String>> call, Response<BaseModel<String>> response) {
                dialog.dismiss();
                if (response.code()==200) {
                    Toast.makeText(AddEditFoodActivity.this, "Editing completed successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<BaseModel<String>> call, Throwable t) {
                dialog.dismiss();
            }
        });
    }

    public void callApiAddFood() {
        dialogWait();
        RequestBody name = RequestBody.create(MEDIA_TYPE_TEXT, food.getName());
        RequestBody category = RequestBody.create(MEDIA_TYPE_TEXT, food.getCategory());
        RequestBody caloryType = RequestBody.create(MEDIA_TYPE_TEXT, food.getCalory_type());
        RequestBody image = RequestBody.create(MEDIA_TYPE_IMAGE, file);
        MultipartBody.Part fileImage = MultipartBody.Part.createFormData("image",file.getName(),image);
        ApiClient.getClient().create(ApiUrl.class).addFood(new LocalData(AddEditFoodActivity.this).getToken(), name, category
                , food.getCalory(), caloryType, fileImage).enqueue(new Callback<BaseModel<String>>() {
            @Override
            public void onResponse(Call<BaseModel<String>> call, Response<BaseModel<String>> response) {
                dialog.dismiss();
                if (response.code()==200) {
                    Toast.makeText(AddEditFoodActivity.this, "Added completed successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }

            }

            @Override
            public void onFailure(Call<BaseModel<String>> call, Throwable t) {
                dialog.dismiss();
            }
        });
    }

    public String getRealPathFromURI(Uri contentURI, Activity context) {
        String[] projection = {MediaStore.Images.Media.DATA};
        @SuppressWarnings("deprecation")
        Cursor cursor = context.managedQuery(contentURI, projection, null,
                null, null);
        if (cursor == null)
            return null;
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        if (cursor.moveToFirst()) {
            String s = cursor.getString(column_index);
            // cursor.close();
            return s;
        }
        // cursor.close();
        return null;
    }
}