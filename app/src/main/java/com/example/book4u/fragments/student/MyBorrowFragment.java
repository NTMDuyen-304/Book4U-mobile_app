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
import com.example.book4u.models.BorrowRequest;
import com.example.book4u.repository.BorrowRepository;
import com.example.book4u.storage.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class MyBorrowFragment extends Fragment {

    private RecyclerView recyclerMyBorrow;
    private BorrowAdapter borrowAdapter;
    private final List<BorrowItem> borrowItemList = new ArrayList<>();

    private BorrowRepository borrowRepository;
    private SessionManager sessionManager;

    public MyBorrowFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_borrow, container, false);

        recyclerMyBorrow = view.findViewById(R.id.recyclerMyBorrow);
        recyclerMyBorrow.setLayoutManager(new LinearLayoutManager(requireContext()));

        borrowRepository = new BorrowRepository();
        sessionManager = new SessionManager(requireContext());

        borrowAdapter = new BorrowAdapter(borrowItemList);
        recyclerMyBorrow.setAdapter(borrowAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadMyBorrows();
    }

    private void loadMyBorrows() {
        borrowRepository.getMyLocalRequests(requireContext(), sessionManager.getUserId(), requests -> {
            borrowItemList.clear();

            for (BorrowRequest request : requests) {
                String borrowDate = "-";
                String dueDate = "-";

                if ("Approved".equalsIgnoreCase(request.getStatus())) {
                    if (request.getBorrowDate() != null && !request.getBorrowDate().isEmpty()) {
                        borrowDate = request.getBorrowDate();
                    }
                    if (request.getDueDate() != null && !request.getDueDate().isEmpty()) {
                        dueDate = request.getDueDate();
                    }
                }

                borrowItemList.add(new BorrowItem(
                        request.getBookTitle(),
                        borrowDate,
                        dueDate,
                        request.getStatus()
                ));
            }

            borrowAdapter.setBorrowList(borrowItemList);
        });
    }
}