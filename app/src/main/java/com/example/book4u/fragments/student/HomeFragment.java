package com.example.book4u.fragments.student;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.book4u.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeFragment extends Fragment {

    public HomeFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Button btnExploreBooks = view.findViewById(R.id.btnExploreBooks);
        Button btnMyHistory = view.findViewById(R.id.btnMyHistory);

        btnExploreBooks.setOnClickListener(v -> {
            BottomNavigationView bottomNav = requireActivity().findViewById(R.id.bottomNavStudent);
            if (bottomNav != null) {
                bottomNav.setSelectedItemId(R.id.nav_books);
            }
        });

        btnMyHistory.setOnClickListener(v -> {
            BottomNavigationView bottomNav = requireActivity().findViewById(R.id.bottomNavStudent);
            if (bottomNav != null) {
                bottomNav.setSelectedItemId(R.id.nav_notifications);
            }
        });

        return view;
    }
}