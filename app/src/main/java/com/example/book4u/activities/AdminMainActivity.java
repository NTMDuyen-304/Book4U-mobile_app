package com.example.book4u.activities;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.book4u.R;
import com.example.book4u.fragments.admin.AdminBooksFragment;
import com.example.book4u.fragments.admin.AdminBorrowManageFragment;
import com.example.book4u.fragments.admin.AdminDashboardFragment;
import com.example.book4u.fragments.shared.NotificationsFragment;
import com.example.book4u.fragments.shared.ProfileFragment;
import com.example.book4u.repository.NotificationRepository;
import com.example.book4u.storage.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminMainActivity extends AppCompatActivity
        implements NotificationsFragment.NotificationNavigationListener {

    private BottomNavigationView bottomNavigationView;
    private View viewNotificationDot;
    private NotificationRepository notificationRepository;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        bottomNavigationView = findViewById(R.id.bottomNavAdmin);
        viewNotificationDot = findViewById(R.id.viewNotificationDot);

        notificationRepository = new NotificationRepository();
        sessionManager = new SessionManager(this);

        loadFragment(new AdminDashboardFragment());
        updateNotificationDot();

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            int id = item.getItemId();
            if (id == R.id.nav_admin_dashboard) {
                selectedFragment = new AdminDashboardFragment();
            } else if (id == R.id.nav_admin_books) {
                selectedFragment = new AdminBooksFragment();
            } else if (id == R.id.nav_admin_borrow) {
                selectedFragment = new AdminBorrowManageFragment();
            } else if (id == R.id.nav_admin_notifications) {
                selectedFragment = new NotificationsFragment();
            } else if (id == R.id.nav_admin_profile) {
                selectedFragment = new ProfileFragment();
            }

            if (selectedFragment != null) {
                loadFragment(selectedFragment);
                updateNotificationDot();
                return true;
            }
            return false;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateNotificationDot();
    }

    private void updateNotificationDot() {
        int unread = notificationRepository.getUnreadCount(this, sessionManager.getUserId());
        if (viewNotificationDot != null) {
            viewNotificationDot.setVisibility(unread > 0 ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void openBorrowTarget(String targetType, String targetId) {
        bottomNavigationView.setSelectedItemId(R.id.nav_admin_borrow);
    }

    @Override
    public void refreshNotificationDot() {
        updateNotificationDot();
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.adminFragmentContainer, fragment)
                .commit();
    }
}