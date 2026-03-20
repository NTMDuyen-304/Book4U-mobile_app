package com.example.book4u.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.book4u.R;
import com.example.book4u.fragments.student.BooksFragment;
import com.example.book4u.fragments.student.HistoryFragment;
import com.example.book4u.fragments.student.HomeFragment;
import com.example.book4u.fragments.student.MyBorrowFragment;
import com.example.book4u.fragments.shared.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class StudentMainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);

        bottomNavigationView = findViewById(R.id.bottomNavStudent);

        loadFragment(new HomeFragment());

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            int id = item.getItemId();
            if (id == R.id.nav_home) {
                selectedFragment = new HomeFragment();
            } else if (id == R.id.nav_books) {
                selectedFragment = new BooksFragment();
            } else if (id == R.id.nav_borrow) {
                selectedFragment = new MyBorrowFragment();
            } else if (id == R.id.nav_history) {
                selectedFragment = new HistoryFragment();
            } else if (id == R.id.nav_profile) {
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
                .replace(R.id.studentFragmentContainer, fragment)
                .commit();
    }
}