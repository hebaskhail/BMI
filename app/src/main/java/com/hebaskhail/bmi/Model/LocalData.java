package com.hebaskhail.bmi.Model;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

public class LocalData {

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    public LocalData(Context context) {
        preferences = context.getSharedPreferences("FindUp", MODE_PRIVATE);
        editor = preferences.edit();
    }

    public void saveToken(String token) {
        editor.putString("token", "Bearer " + token).apply();
    }

    public String getToken() {
        return preferences.getString("token", null);
    }

    public void saveName(String name) {
        editor.putString("name", "Hi, " + name).apply();
    }

    public String getName() {
        return preferences.getString("name", null);
    }

    public void logout() {
        editor.clear().apply();
    }
}
