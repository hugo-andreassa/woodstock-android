package com.hyperdrive.woodstock.persistence;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.hyperdrive.woodstock.models.UserModel;

public class SharedPreferencesUtil {

    private final String PREFERENCES_FILE = "PreferencesFile";
    private SharedPreferences sharedPreferences;

    public SharedPreferencesUtil(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFERENCES_FILE, 0);
    }

    public void setAuthentication(String auth) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("authentication", auth);
        editor.commit();
    }

    public String getAuthentication() {
        String auth = sharedPreferences.getString("authentication", "");

        return auth;
    }

    public void setUser(UserModel user) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(user);
        editor.putString("User", json);
        editor.commit();
    }

    public UserModel getUser() {
        Gson gson = new Gson();
        String json = sharedPreferences.getString("User", "");
        UserModel obj = gson.fromJson(json, UserModel.class);

        return obj;
    }

    public void clearSharedPreferences() {
        sharedPreferences.edit().clear().apply();
    }
}
