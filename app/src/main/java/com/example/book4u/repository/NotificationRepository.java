package com.example.book4u.repository;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.book4u.models.NotificationItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NotificationRepository {
    private static final String PREF_NAME = "book4u_notifications";
    private static final String KEY_LIST = "notification_list";

    private final Gson gson = new Gson();
    private final Type listType = new TypeToken<List<NotificationItem>>() {}.getType();

    private SharedPreferences prefs(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    private List<NotificationItem> readAll(Context context) {
        String json = prefs(context).getString(KEY_LIST, "");
        if (json == null || json.isEmpty()) return new ArrayList<>();
        List<NotificationItem> list = gson.fromJson(json, listType);
        return list == null ? new ArrayList<>() : list;
    }

    private void saveAll(Context context, List<NotificationItem> list) {
        prefs(context).edit().putString(KEY_LIST, gson.toJson(list)).apply();
    }

    public void addNotification(Context context, String userId, String title, String message, String createdAt) {
        List<NotificationItem> list = readAll(context);
        list.add(0, new NotificationItem(
                UUID.randomUUID().toString(),
                userId,
                title,
                message,
                createdAt,
                false
        ));
        saveAll(context, list);
    }

    public List<NotificationItem> getNotificationsByUser(Context context, String userId) {
        List<NotificationItem> result = new ArrayList<>();
        for (NotificationItem item : readAll(context)) {
            if (userId.equals(item.getUserId())) {
                result.add(item);
            }
        }
        return result;
    }

    public int getUnreadCount(Context context, String userId) {
        int count = 0;
        for (NotificationItem item : readAll(context)) {
            if (userId.equals(item.getUserId()) && !item.isRead()) {
                count++;
            }
        }
        return count;
    }

    public void markAllAsRead(Context context, String userId) {
        List<NotificationItem> list = readAll(context);

        for (NotificationItem item : list) {
            if (userId.equals(item.getUserId())) {
                item.setRead(true);
            }
        }

        saveAll(context, list);
    }
}