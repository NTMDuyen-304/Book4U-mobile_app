package com.example.book4u.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.book4u.R;
import com.example.book4u.fragments.admin.AdminBooksFragment;
import com.example.book4u.fragments.admin.AdminBorrowManageFragment;
import com.example.book4u.fragments.admin.AdminDashboardFragment;
import com.example.book4u.fragments.shared.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminMainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        bottomNavigationView = findViewById(R.id.bottomNavAdmin);

        loadFragment(new AdminDashboardFragment());

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            int id = item.getItemId();
            if (id == R.id.nav_admin_dashboard) {
                selectedFragment = new AdminDashboardFragment();
            } else if (id == R.id.nav_admin_books) {
                selectedFragment = new AdminBooksFragment();
            } else if (id == R.id.nav_admin_borrow) {
                selectedFragment = new AdminBorrowManageFragment();
            } else if (id == R.id.nav_admin_profile) {
                selectedFragment = new ProfileFragment();
            }

            if (selectedFragment != null) {
                loadFragment(selectedFragment);
                return true;
            }
            return false;
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.adminFragmentContainer, fragment)
                .commit();
    }
}