package com.example.book4u.storage;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "book4u_session";

    private static final String KEY_TOKEN = "token";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_ROLE = "role";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";

    private final SharedPreferences preferences;
    private final SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public void saveSession(String token, String userId, String name, String email, String role) {
        editor.putString(KEY_TOKEN, token);
        editor.putString(KEY_USER_ID, userId);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_ROLE, role);
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return preferences.getBoolean(KEY_IS_LOGGED_IN, false)
                && getToken() != null
                && !getToken().isEmpty();
    }

    public String getToken() {
        return preferences.getString(KEY_TOKEN, "");
    }

    public String getUserId() {
        return preferences.getString(KEY_USER_ID, "");
    }

    public String getName() {
        return preferences.getString(KEY_NAME, "");
    }

    public String getEmail() {
        return preferences.getString(KEY_EMAIL, "");
    }

    public String getRole() {
        return preferences.getString(KEY_ROLE, "");
    }

    public void clearSession() {
        editor.clear();
        editor.apply();
    }
}