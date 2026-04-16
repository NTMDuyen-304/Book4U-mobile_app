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
import com.example.book4u.models.NotificationItem;
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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        recyclerNotifications = view.findViewById(R.id.recyclerNotifications);
        tvMarkAllRead = view.findViewById(R.id.tvMarkAllRead);

        notificationRepository = new NotificationRepository();
        sessionManager = new SessionManager(requireContext());

        recyclerNotifications.setLayoutManager(new LinearLayoutManager(requireContext()));

        notificationAdapter = new NotificationAdapter(notificationList, item -> {
            notificationRepository.markAsRead(requireContext(), item.getId());
            loadNotifications();

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
            loadNotifications();

            if (navigationListener != null) {
                navigationListener.refreshNotificationDot();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadNotifications();
    }

    private void loadNotifications() {
        List<NotificationItem> items =
                notificationRepository.getNotificationsByUser(requireContext(), sessionManager.getUserId());

        notificationList.clear();
        notificationList.addAll(items);
        notificationAdapter.setNotificationList(notificationList);
    }
}