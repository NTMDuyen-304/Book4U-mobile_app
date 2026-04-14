package com.example.book4u.fragments.student;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.book4u.R;
import com.example.book4u.adapters.BorrowAdapter;
import com.example.book4u.models.BorrowItem;

import java.util.ArrayList;
import java.util.List;

public class MyBorrowFragment extends Fragment {

    private RecyclerView recyclerMyBorrow;
    private BorrowAdapter borrowAdapter;
    private final List<BorrowItem> borrowItemList = new ArrayList<>();

    public MyBorrowFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_borrow, container, false);

        recyclerMyBorrow = view.findViewById(R.id.recyclerMyBorrow);
        recyclerMyBorrow.setLayoutManager(new LinearLayoutManager(requireContext()));

        setupMockBorrowItems();

        borrowAdapter = new BorrowAdapter(borrowItemList);
        recyclerMyBorrow.setAdapter(borrowAdapter);

        return view;
    }

    private void setupMockBorrowItems() {
        borrowItemList.add(new BorrowItem("Clean Code", "2026-04-01", "2026-04-15", "Borrowing"));
        borrowItemList.add(new BorrowItem("Atomic Habits", "2026-04-03", "2026-04-17", "Due Soon"));
        borrowItemList.add(new BorrowItem("Design Patterns", "2026-04-05", "2026-04-19", "Borrowing"));
    }
}