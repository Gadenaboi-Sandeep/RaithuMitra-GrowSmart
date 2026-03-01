package com.raithumitra.data.local;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "RaithuMitraSession";
    private static final String KEY_TOKEN = "jwt_token";
    private static final String KEY_ROLE = "user_role";
    private static final String KEY_USER_Name = "user_name";

    private static final String KEY_PHONE = "user_phone";
    private static final String KEY_ADDRESS = "user_address";

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void saveAuthToken(String token) {
        editor.putString(KEY_TOKEN, token);
        editor.apply();
    }

    public String getAuthToken() {
        return prefs.getString(KEY_TOKEN, null);
    }

    public void saveUserRole(String role) {
        editor.putString(KEY_ROLE, role);
        editor.apply();
    }

    public String getUserRole() {
        return prefs.getString(KEY_ROLE, "FARMER"); // Default to Farmer
    }

    public void saveUserName(String name) {
        editor.putString(KEY_USER_Name, name);
        editor.apply();
    }

    public String getUserName() {
        return prefs.getString(KEY_USER_Name, "User");
    }

    public void saveUserProfile(String name, String phone, String address) {
        editor.putString(KEY_USER_Name, name);
        editor.putString(KEY_PHONE, phone);
        editor.putString(KEY_ADDRESS, address);
        editor.apply();
    }

    public String getMobileNumber() {
        return prefs.getString(KEY_PHONE, "");
    }

    public String getUserPhone() {
        return prefs.getString(KEY_PHONE, "");
    }

    public String getAddress() {
        return prefs.getString(KEY_ADDRESS, "");
    }

    public void clearSession() {
        editor.clear();
        editor.apply();
    }
}
