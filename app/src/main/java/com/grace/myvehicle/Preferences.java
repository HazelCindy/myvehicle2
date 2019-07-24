package com.grace.myvehicle;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {
    private Context _context;
    public static final String PREFS_NAME = "MyVehiclePrefs";
    SharedPreferences settings;

    public Preferences(Context context) {
        _context = context;
        settings = context.getSharedPreferences(PREFS_NAME, 0);
    }
    public boolean isLoggedin(){
        return settings.getBoolean("isLoggedin", false);
    }
    public void setIsLoggedin(boolean status){
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("isLoggedin", status);
        editor.commit();
    }

    public String getName() {
        return settings.getString("name", "");
    }

    public void setName(String name) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("name", name);
        editor.commit();
    }
    public String getEmail() {
        return settings.getString("email", "");
    }

    public void setEmail(String email) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("email", email);
        editor.commit();
    }
    public int getUserId(){
        return settings.getInt("cart_id", 0);
    }
    public void setUserId(int cartID){
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("cart_id", cartID);
        editor.commit();
    }
}
