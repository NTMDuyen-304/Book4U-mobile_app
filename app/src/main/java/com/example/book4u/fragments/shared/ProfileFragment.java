package com.example.book4u.fragments.shared;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.book4u.R;
import com.example.book4u.activities.LoginActivity;
import com.example.book4u.models.NotificationItem;
import com.example.book4u.repository.NotificationRepository;
import com.example.book4u.storage.SessionManager;

import java.util.List;

public class ProfileFragment extends Fragment {

    public ProfileFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        TextView tvUserName = view.findViewById(R.id.tvUserName);
        TextView tvUserEmail = view.findViewById(R.id.tvUserEmail);
        TextView tvUserRole = view.findViewById(R.id.tvUserRole);
        Button btnLogout = view.findViewById(R.id.btnLogout);
        LinearLayout containerNotifications = view.findViewById(R.id.containerNotifications);

        SessionManager sessionManager = new SessionManager(requireContext());
        NotificationRepository notificationRepository = new NotificationRepository();

        tvUserName.setText(sessionManager.getName());
        tvUserEmail.setText(sessionManager.getEmail());
        tvUserRole.setText(sessionManager.getRole());

        List<NotificationItem> notifications =
                notificationRepository.getNotificationsByUser(requireContext(), sessionManager.getUserId());

        if (containerNotifications != null) {
            containerNotifications.removeAllViews();

            if (notifications.isEmpty()) {
                TextView emptyView = new TextView(requireContext());
                emptyView.setText(R.string.no_notifications_yet);
                containerNotifications.addView(emptyView);
            } else {
                for (NotificationItem item : notifications) {
                    TextView tv = new TextView(requireContext());
                    String text = getString(
                            R.string.notification_item_format,
                            item.getTitle(),
                            item.getMessage(),
                            item.getCreatedAt()
                    );
                    tv.setText(text);
                    tv.setPadding(0, 0, 0, 16);
                    containerNotifications.addView(tv);
                }
            }
        }

        notificationRepository.markAllAsRead(requireContext(), sessionManager.getUserId());

        btnLogout.setOnClickListener(v -> {
            sessionManager.clearSession();

            Intent intent = new Intent(requireContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        return view;
    }
}