package com.example.book4u.repository;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.book4u.models.Book;
import com.example.book4u.models.Borrow;
import com.example.book4u.models.BorrowCreateRequest;
import com.example.book4u.models.BorrowRequest;
import com.example.book4u.models.MessageResponse;
import com.example.book4u.network.ApiService;
import com.example.book4u.network.RetrofitClient;
import com.example.book4u.storage.SessionManager;
import com.example.book4u.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;

public class BorrowRepository {
    private final ApiService apiService;

    private static final String PREF_NAME = "book4u_borrows";
    private static final String KEY_LIST = "borrow_request_list";

    private final Gson gson = new Gson();
    private final Type listType = new TypeToken<List<BorrowRequest>>() {}.getType();

    public interface ActionCallback {
        void onSuccess(String message);
        void onError(String message);
    }

    public interface RequestsCallback {
        void onResult(List<BorrowRequest> requests);
    }

    public BorrowRepository() {
        apiService = RetrofitClient.getApiService();
    }

    private String bearer(String token) {
        return Constants.BEARER_PREFIX + token;
    }

    // ===== Remote API giữ nguyên =====
    public Call<List<Borrow>> getAllBorrows(String token) {
        return apiService.getAllBorrows(bearer(token));
    }

    public Call<List<Borrow>> getMyBorrows(String token) {
        return apiService.getMyBorrows(bearer(token));
    }

    public Call<List<Borrow>> getBorrowsByUser(String token, String userId) {
        return apiService.getBorrowsByUser(bearer(token), userId);
    }

    public Call<Borrow> createBorrow(String token, String bookId) {
        return apiService.createBorrow(bearer(token), new BorrowCreateRequest(bookId));
    }

    public Call<Borrow> approveBorrow(String token, String borrowId) {
        return apiService.approveBorrow(bearer(token), borrowId);
    }

    public Call<Borrow> rejectBorrow(String token, String borrowId) {
        return apiService.rejectBorrow(bearer(token), borrowId);
    }

    public Call<Borrow> extendBorrow(String token, String borrowId) {
        return apiService.extendBorrow(bearer(token), borrowId);
    }

    public Call<Borrow> returnBorrow(String token, String borrowId) {
        return apiService.returnBorrow(bearer(token), borrowId);
    }

    public Call<MessageResponse> deleteBorrow(String token, String borrowId) {
        return apiService.deleteBorrow(bearer(token), borrowId);
    }

    // ===== Local =====
    private SharedPreferences prefs(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    private List<BorrowRequest> readAll(Context context) {
        String json = prefs(context).getString(KEY_LIST, "");
        if (json == null || json.isEmpty()) return new ArrayList<>();
        List<BorrowRequest> list = gson.fromJson(json, listType);
        return list == null ? new ArrayList<>() : list;
    }

    private void saveAll(Context context, List<BorrowRequest> list) {
        prefs(context).edit().putString(KEY_LIST, gson.toJson(list)).apply();
    }

    private String today() {
        return LocalDate.now().toString();
    }

    private String dueAfter14Days() {
        return LocalDate.now().plusDays(14).toString();
    }

    public void createBorrowLocal(Context context, Book book, SessionManager sessionManager, ActionCallback callback) {
        if (!book.isAvailable()) {
            callback.onError("Book is out of stock");
            return;
        }

        List<BorrowRequest> list = readAll(context);

        int activeApproved = 0;
        for (BorrowRequest item : list) {
            if (sessionManager.getUserId().equals(item.getUserId())
                    && "Approved".equalsIgnoreCase(item.getStatus())) {
                activeApproved++;
            }
        }

        if (activeApproved >= 3) {
            callback.onError("Bạn không thể mượn thêm vì đang mượn 3 cuốn");
            return;
        }

        BorrowRequest request = new BorrowRequest(
                UUID.randomUUID().toString(),
                book.getTitle(),
                sessionManager.getUserId(),
                sessionManager.getName(),
                today(),
                "",
                "",
                "Pending"
        );

        list.add(0, request);
        saveAll(context, list);

        NotificationRepository notificationRepository = new NotificationRepository();
        notificationRepository.addNotification(
                context,
                "admin_local",
                "Borrow request",
                sessionManager.getName() + " requested \"" + book.getTitle() + "\"",
                today()
        );

        callback.onSuccess("Borrow request sent");
    }

    public void getAllLocalRequests(Context context, RequestsCallback callback) {
        callback.onResult(readAll(context));
    }

    public void getMyLocalRequests(Context context, String userId, RequestsCallback callback) {
        List<BorrowRequest> result = new ArrayList<>();
        for (BorrowRequest item : readAll(context)) {
            if (userId.equals(item.getUserId())) {
                result.add(item);
            }
        }
        callback.onResult(result);
    }

    public void approveLocal(Context context, String borrowId, ActionCallback callback) {
        List<BorrowRequest> list = readAll(context);
        BorrowRequest target = null;

        for (BorrowRequest item : list) {
            if (borrowId.equals(item.getId())) {
                target = item;
                break;
            }
        }

        if (target == null) {
            callback.onError("Request not found");
            return;
        }

        int activeApproved = 0;
        for (BorrowRequest item : list) {
            if (target.getUserId().equals(item.getUserId())
                    && "Approved".equalsIgnoreCase(item.getStatus())) {
                activeApproved++;
            }
        }

        if (activeApproved >= 3) {
            callback.onError(target.getUserName() + " is already borrowing 3 books");
            return;
        }

        target.setStatus("Approved");
        target.setBorrowDate(today());
        target.setDueDate(dueAfter14Days());
        saveAll(context, list);

        NotificationRepository notificationRepository = new NotificationRepository();
        notificationRepository.addNotification(
                context,
                "admin_local",
                "Borrow approved",
                "Approved \"" + target.getBookTitle() + "\" for " + target.getUserName(),
                today()
        );
        notificationRepository.addNotification(
                context,
                target.getUserId(),
                "Borrow approved",
                "Your request for \"" + target.getBookTitle() + "\" was approved",
                today()
        );

        callback.onSuccess("Approved successfully");
    }

    public void rejectLocal(Context context, String borrowId, ActionCallback callback) {
        List<BorrowRequest> list = readAll(context);
        BorrowRequest target = null;

        for (BorrowRequest item : list) {
            if (borrowId.equals(item.getId())) {
                target = item;
                break;
            }
        }

        if (target == null) {
            callback.onError("Request not found");
            return;
        }

        target.setStatus("Rejected");
        saveAll(context, list);

        NotificationRepository notificationRepository = new NotificationRepository();
        notificationRepository.addNotification(
                context,
                "admin_local",
                "Borrow rejected",
                "Rejected \"" + target.getBookTitle() + "\" for " + target.getUserName(),
                today()
        );
        notificationRepository.addNotification(
                context,
                target.getUserId(),
                "Borrow rejected",
                "Your request for \"" + target.getBookTitle() + "\" was rejected",
                today()
        );

        callback.onSuccess("Rejected successfully");
    }
}