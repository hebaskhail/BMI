package com.hebaskhail.bmi.Api;

import com.hebaskhail.bmi.Model.AddMeal;
import com.hebaskhail.bmi.Model.AllFoodApi;
import com.hebaskhail.bmi.Model.BaseModel;
import com.hebaskhail.bmi.Model.Food;
import com.hebaskhail.bmi.Model.Home;
import com.hebaskhail.bmi.Model.Record;
import com.hebaskhail.bmi.Model.User;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiUrl {
    @POST("register")
    Call<BaseModel<User>> createUser(@Body User user);

    @POST("login")
    Call<BaseModel<User>> login(@Body User user);

    @GET("home")
    Call<BaseModel<Home>> getHome(@Header("Authorization") String token, @Query("page") int page);

    @GET("food-list")
    Call<BaseModel<AllFoodApi>> getAllFoods(@Header("Authorization") String token, @Query("page") int page, @Query("limit") int limit);

    @DELETE("delete-food/{idDelete}")
    Call<BaseModel<AllFoodApi>> deleteFoods(@Path("idDelete") String id, @Header("Authorization") String token);

    @POST("add-record")
    Call<BaseModel<String>> addRecord(@Header("Authorization") String token, @Body Record record);

    @POST("add-meal")
    Call<BaseModel<String>> addMeal(@Header("Authorization") String token, @Body AddMeal addMeal);

    @Multipart
    @POST("add-food")
    Call<BaseModel<String>> addFood(@Header("Authorization") String token, @Part("name") RequestBody name,
                                    @Part("category") RequestBody category, @Part("calory") int calory,
                                    @Part("calory_type") RequestBody calory_type, @Part MultipartBody.Part image);

    @Multipart
    @PUT("edit-food/{food_id}")
    Call<BaseModel<String>> editFood(@Path("food_id") String food_id, @Header("Authorization") String token, @Part("name") RequestBody name,
                                     @Part("category") RequestBody category, @Part("calory") int calory,
                                     @Part("calory_type") RequestBody calory_type, @Part MultipartBody.Part image);

}
