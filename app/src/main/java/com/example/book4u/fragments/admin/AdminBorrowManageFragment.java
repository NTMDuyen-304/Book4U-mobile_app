package com.example.book4u.fragments.admin;

import android.os.Bundle;
import android.widget.Toast;
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
import com.example.book4u.repository.BorrowRepository;

import java.util.ArrayList;
import java.util.List;

public class AdminBorrowManageFragment extends Fragment {

    private RecyclerView recyclerBorrowRequests;
    private BorrowRequestAdapter borrowRequestAdapter;
    private final List<BorrowRequest> requestList = new ArrayList<>();
    private BorrowRepository borrowRepository;

    public AdminBorrowManageFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_borrow_manage, container, false);

        recyclerBorrowRequests = view.findViewById(R.id.recyclerBorrowRequests);
        recyclerBorrowRequests.setLayoutManager(new LinearLayoutManager(requireContext()));

        borrowRepository = new BorrowRepository();

        borrowRequestAdapter = new BorrowRequestAdapter(requestList, new BorrowRequestAdapter.OnBorrowRequestActionListener() {
            @Override
            public void onApprove(BorrowRequest request) {
                borrowRepository.approveLocal(requireContext(), request.getId(), new BorrowRepository.ActionCallback() {
                    @Override
                    public void onSuccess(String message) {
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                        loadRequests();
                    }

                    @Override
                    public void onError(String message) {
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onReject(BorrowRequest request) {
                borrowRepository.rejectLocal(requireContext(), request.getId(), new BorrowRepository.ActionCallback() {
                    @Override
                    public void onSuccess(String message) {
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                        loadRequests();
                    }

                    @Override
                    public void onError(String message) {
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        recyclerBorrowRequests.setAdapter(borrowRequestAdapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadRequests();
    }

    private void loadRequests() {
        borrowRepository.getAllLocalRequests(requireContext(), requests -> {
            requestList.clear();
            requestList.addAll(requests);
            borrowRequestAdapter.setRequestList(requestList);
        });
    }
}