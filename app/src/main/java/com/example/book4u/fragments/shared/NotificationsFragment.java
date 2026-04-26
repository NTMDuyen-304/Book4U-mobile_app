package com.example.book4u.fragments.shared;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.book4u.R;
import com.example.book4u.adapters.NotificationAdapter;
import com.example.book4u.models.Borrow;
import com.example.book4u.models.NotificationItem;
import com.example.book4u.repository.BorrowRepository;
import com.example.book4u.repository.NotificationRepository;
import com.example.book4u.storage.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class NotificationsFragment extends Fragment {

    public interface NotificationNavigationListener {
        void openBorrowTarget(String targetType, String targetId);
        void refreshNotificationDot();
    }

    private RecyclerView recyclerNotifications;
    private TextView tvMarkAllRead;

    private NotificationAdapter notificationAdapter;
    private final List<NotificationItem> notificationList = new ArrayList<>();

    private NotificationRepository notificationRepository;
    private BorrowRepository borrowRepository;
    private SessionManager sessionManager;
    private NotificationNavigationListener navigationListener;

    public NotificationsFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof NotificationNavigationListener) {
            navigationListener = (NotificationNavigationListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        navigationListener = null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        recyclerNotifications = view.findViewById(R.id.recyclerNotifications);
        tvMarkAllRead = view.findViewById(R.id.tvMarkAllRead);

        notificationRepository = new NotificationRepository();
        borrowRepository = new BorrowRepository();
        sessionManager = new SessionManager(requireContext());

        recyclerNotifications.setLayoutManager(new LinearLayoutManager(requireContext()));

        notificationAdapter = new NotificationAdapter(notificationList, item -> {
            notificationRepository.markAsRead(requireContext(), item.getId());
            loadLocalNotifications();

            if (navigationListener != null) {
                navigationListener.refreshNotificationDot();
            }

            if (!TextUtils.isEmpty(item.getTargetType())) {
                if (navigationListener != null) {
                    navigationListener.openBorrowTarget(item.getTargetType(), item.getTargetId());
                }
            } else {
                Toast.makeText(requireContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });

        recyclerNotifications.setAdapter(notificationAdapter);

        tvMarkAllRead.setOnClickListener(v -> {
            notificationRepository.markAllAsRead(requireContext(), sessionManager.getUserId());
            loadLocalNotifications();

            if (navigationListener != null) {
                navigationListener.refreshNotificationDot();
            }
        });

        loadNotificationsFromBorrowApi();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadNotificationsFromBorrowApi();
    }

    private void loadNotificationsFromBorrowApi() {
        String token = sessionManager.getToken();

        if (token == null || token.trim().isEmpty()) {
            Toast.makeText(requireContext(), "Bạn chưa đăng nhập", Toast.LENGTH_SHORT).show();
            loadLocalNotifications();
            return;
        }

        String role = sessionManager.getRole();

        if ("admin".equalsIgnoreCase(role)) {
            borrowRepository.getAllBorrows(token)
                    .enqueue(new retrofit2.Callback<List<Borrow>>() {
                        @Override
                        public void onResponse(@NonNull retrofit2.Call<List<Borrow>> call,
                                               @NonNull retrofit2.Response<List<Borrow>> response) {
                            if (!isAdded()) return;

                            if (response.isSuccessful() && response.body() != null) {
                                generateAdminNotifications(response.body());
                            } else {
                                Toast.makeText(
                                        requireContext(),
                                        "Không tải được thông báo. Code: " + response.code(),
                                        Toast.LENGTH_SHORT
                                ).show();
                            }

                            loadLocalNotifications();
                        }

                        @Override
                        public void onFailure(@NonNull retrofit2.Call<List<Borrow>> call,
                                              @NonNull Throwable t) {
                            if (!isAdded()) return;

                            Toast.makeText(
                                    requireContext(),
                                    "Lỗi kết nối BE: " + t.getMessage(),
                                    Toast.LENGTH_LONG
                            ).show();

                            loadLocalNotifications();
                        }
                    });
        } else {
            borrowRepository.getMyBorrows(token)
                    .enqueue(new retrofit2.Callback<List<Borrow>>() {
                        @Override
                        public void onResponse(@NonNull retrofit2.Call<List<Borrow>> call,
                                               @NonNull retrofit2.Response<List<Borrow>> response) {
                            if (!isAdded()) return;

                            if (response.isSuccessful() && response.body() != null) {
                                generateStudentNotifications(response.body());
                            } else {
                                Toast.makeText(
                                        requireContext(),
                                        "Không tải được thông báo. Code: " + response.code(),
                                        Toast.LENGTH_SHORT
                                ).show();
                            }

                            loadLocalNotifications();
                        }

                        @Override
                        public void onFailure(@NonNull retrofit2.Call<List<Borrow>> call,
                                              @NonNull Throwable t) {
                            if (!isAdded()) return;

                            Toast.makeText(
                                    requireContext(),
                                    "Lỗi kết nối BE: " + t.getMessage(),
                                    Toast.LENGTH_LONG
                            ).show();

                            loadLocalNotifications();
                        }
                    });
        }
    }

    private void generateStudentNotifications(List<Borrow> borrows) {
        String userId = sessionManager.getUserId();

        for (Borrow borrow : borrows) {
            String status = borrow.getStatus();
            String bookTitle = borrow.getBookTitle();
            String borrowId = borrow.getId();

            if (status == null || borrowId == null || borrowId.isEmpty()) {
                continue;
            }

            String createdAt = borrow.getBorrowDate();

            if ("Pending Approval".equalsIgnoreCase(status) || "Pending".equalsIgnoreCase(status)) {
                notificationRepository.addNotificationIfNotExists(
                        requireContext(),
                        userId,
                        "Yêu cầu mượn đang chờ duyệt",
                        "Bạn đã gửi yêu cầu mượn sách \"" + bookTitle + "\".",
                        createdAt,
                        "borrow",
                        borrowId + "_student_pending"
                );
            } else if ("Borrowing".equalsIgnoreCase(status)) {
                notificationRepository.addNotificationIfNotExists(
                        requireContext(),
                        userId,
                        "Yêu cầu mượn đã được duyệt",
                        "Sách \"" + bookTitle + "\" đã được admin duyệt. Hạn trả: " + borrow.getDueDate(),
                        createdAt,
                        "borrow",
                        borrowId + "_student_approved"
                );
            } else if ("Returned".equalsIgnoreCase(status)) {
                notificationRepository.addNotificationIfNotExists(
                        requireContext(),
                        userId,
                        "Đã trả sách thành công",
                        "Bạn đã trả sách \"" + bookTitle + "\" thành công.",
                        createdAt,
                        "history",
                        borrowId + "_student_returned"
                );
            } else if ("Rejected".equalsIgnoreCase(status)) {
                notificationRepository.addNotificationIfNotExists(
                        requireContext(),
                        userId,
                        "Yêu cầu mượn bị từ chối",
                        "Yêu cầu mượn sách \"" + bookTitle + "\" đã bị từ chối.",
                        createdAt,
                        "history",
                        borrowId + "_student_rejected"
                );
            } else if ("Overdue".equalsIgnoreCase(status)) {
                notificationRepository.addNotificationIfNotExists(
                        requireContext(),
                        userId,
                        "Sách đã quá hạn",
                        "Sách \"" + bookTitle + "\" đã quá hạn trả. Vui lòng trả sách sớm.",
                        createdAt,
                        "borrow",
                        borrowId + "_student_overdue"
                );
            }
        }
    }

    private void generateAdminNotifications(List<Borrow> borrows) {
        String adminId = sessionManager.getUserId();

        for (Borrow borrow : borrows) {
            String status = borrow.getStatus();
            String bookTitle = borrow.getBookTitle();
            String borrowId = borrow.getId();

            if (status == null || borrowId == null || borrowId.isEmpty()) {
                continue;
            }

            String borrowerName = borrow.getBorrowerName();
            String createdAt = borrow.getBorrowDate();

            if ("Pending Approval".equalsIgnoreCase(status) || "Pending".equalsIgnoreCase(status)) {
                notificationRepository.addNotificationIfNotExists(
                        requireContext(),
                        adminId,
                        "Có yêu cầu mượn mới",
                        borrowerName + " muốn mượn sách \"" + bookTitle + "\".",
                        createdAt,
                        "borrow",
                        borrowId + "_admin_pending"
                );
            } else if ("Borrowing".equalsIgnoreCase(status)) {
                notificationRepository.addNotificationIfNotExists(
                        requireContext(),
                        adminId,
                        "Yêu cầu đã được duyệt",
                        "Sách \"" + bookTitle + "\" đang được mượn bởi " + borrowerName + ".",
                        createdAt,
                        "borrow",
                        borrowId + "_admin_borrowing"
                );
            } else if ("Returned".equalsIgnoreCase(status)) {
                notificationRepository.addNotificationIfNotExists(
                        requireContext(),
                        adminId,
                        "Sách đã được trả",
                        borrowerName + " đã trả sách \"" + bookTitle + "\".",
                        createdAt,
                        "borrow",
                        borrowId + "_admin_returned"
                );
            } else if ("Rejected".equalsIgnoreCase(status)) {
                notificationRepository.addNotificationIfNotExists(
                        requireContext(),
                        adminId,
                        "Yêu cầu đã bị từ chối",
                        "Yêu cầu mượn sách \"" + bookTitle + "\" của " + borrowerName + " đã bị từ chối.",
                        createdAt,
                        "borrow",
                        borrowId + "_admin_rejected"
                );
            } else if ("Overdue".equalsIgnoreCase(status)) {
                notificationRepository.addNotificationIfNotExists(
                        requireContext(),
                        adminId,
                        "Có sách quá hạn",
                        borrowerName + " đang mượn quá hạn sách \"" + bookTitle + "\".",
                        createdAt,
                        "borrow",
                        borrowId + "_admin_overdue"
                );
            }
        }
    }

    private void loadLocalNotifications() {
        List<NotificationItem> items =
                notificationRepository.getNotificationsByUser(requireContext(), sessionManager.getUserId());

        notificationList.clear();
        notificationList.addAll(items);
        notificationAdapter.setNotificationList(notificationList);

        if (navigationListener != null) {
            navigationListener.refreshNotificationDot();
        }
    }
}