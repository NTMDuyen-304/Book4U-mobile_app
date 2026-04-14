package com.example.book4u.fragments.admin;

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
import com.example.book4u.adapters.BorrowRequestAdapter;
import com.example.book4u.models.BorrowRequest;

import java.util.ArrayList;
import java.util.List;

public class AdminBorrowManageFragment extends Fragment {

    private RecyclerView recyclerBorrowRequests;
    private BorrowRequestAdapter borrowRequestAdapter;
    private final List<BorrowRequest> requestList = new ArrayList<>();

    public AdminBorrowManageFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_borrow_manage, container, false);

        recyclerBorrowRequests = view.findViewById(R.id.recyclerBorrowRequests);
        recyclerBorrowRequests.setLayoutManager(new LinearLayoutManager(requireContext()));

        setupMockRequests();

        borrowRequestAdapter = new BorrowRequestAdapter(requestList);
        recyclerBorrowRequests.setAdapter(borrowRequestAdapter);

        return view;
    }

    private void setupMockRequests() {
        requestList.add(new BorrowRequest("Clean Code", "Nguyen Van A", "2026-04-13", "Pending"));
        requestList.add(new BorrowRequest("Atomic Habits", "Tran Thi B", "2026-04-13", "Approved"));
        requestList.add(new BorrowRequest("Design Patterns", "Le Van C", "2026-04-14", "Rejected"));
        requestList.add(new BorrowRequest("Deep Work", "Pham Thi D", "2026-04-14", "Pending"));
    }
}