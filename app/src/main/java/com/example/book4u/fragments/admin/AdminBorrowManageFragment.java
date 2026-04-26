package com.example.book4u.fragments.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.book4u.R;
import com.example.book4u.adapters.BorrowRequestAdapter;
import com.example.book4u.models.Borrow;
import com.example.book4u.models.BorrowRequest;
import com.example.book4u.repository.BorrowRepository;
import com.example.book4u.storage.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class AdminBorrowManageFragment extends Fragment {

    private RecyclerView recyclerBorrowRequests;
    private BorrowRequestAdapter borrowRequestAdapter;

    private final List<BorrowRequest> requestList = new ArrayList<>();

    private BorrowRepository borrowRepository;
    private SessionManager sessionManager;

    public AdminBorrowManageFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_borrow_manage, container, false);

        recyclerBorrowRequests = view.findViewById(R.id.recyclerBorrowRequests);
        recyclerBorrowRequests.setLayoutManager(new LinearLayoutManager(requireContext()));

        borrowRepository = new BorrowRepository();
        sessionManager = new SessionManager(requireContext());

        borrowRequestAdapter = new BorrowRequestAdapter(
                requestList,
                new BorrowRequestAdapter.OnBorrowRequestActionListener() {
                    @Override
                    public void onApprove(BorrowRequest request) {
                        confirmAction(
                                "Duyệt yêu cầu",
                                "Bạn có muốn duyệt yêu cầu mượn sách \"" + request.getBookTitle() + "\" không?",
                                () -> approveBorrow(request)
                        );
                    }

                    @Override
                    public void onReject(BorrowRequest request) {
                        confirmAction(
                                "Từ chối yêu cầu",
                                "Bạn có muốn từ chối yêu cầu mượn sách \"" + request.getBookTitle() + "\" không?",
                                () -> rejectBorrow(request)
                        );
                    }

                    @Override
                    public void onReturn(BorrowRequest request) {
                        confirmAction(
                                "Xác nhận trả sách",
                                "Xác nhận người dùng đã trả sách \"" + request.getBookTitle() + "\"?",
                                () -> returnBorrow(request)
                        );
                    }
                }
        );

        recyclerBorrowRequests.setAdapter(borrowRequestAdapter);

        loadRequests();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadRequests();
    }

    private void loadRequests() {
        String token = sessionManager.getToken();

        if (token == null || token.trim().isEmpty()) {
            Toast.makeText(requireContext(), "Bạn chưa đăng nhập", Toast.LENGTH_SHORT).show();
            return;
        }

        borrowRepository.getAllBorrows(token)
                .enqueue(new retrofit2.Callback<List<Borrow>>() {
                    @Override
                    public void onResponse(@NonNull retrofit2.Call<List<Borrow>> call,
                                           @NonNull retrofit2.Response<List<Borrow>> response) {
                        if (!isAdded()) return;

                        if (response.isSuccessful() && response.body() != null) {
                            requestList.clear();

                            for (Borrow borrow : response.body()) {
                                requestList.add(BorrowRequest.fromBorrow(borrow));
                            }

                            borrowRequestAdapter.setRequestList(requestList);
                        } else {
                            Toast.makeText(
                                    requireContext(),
                                    "Không tải được danh sách mượn/trả. Code: " + response.code(),
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull retrofit2.Call<List<Borrow>> call,
                                          @NonNull Throwable t) {
                        if (!isAdded()) return;

                        Toast.makeText(
                                requireContext(),
                                "Lỗi kết nối BE: " + t.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });
    }

    private void approveBorrow(BorrowRequest request) {
        String token = sessionManager.getToken();

        borrowRepository.approveBorrow(token, request.getId())
                .enqueue(new retrofit2.Callback<Borrow>() {
                    @Override
                    public void onResponse(@NonNull retrofit2.Call<Borrow> call,
                                           @NonNull retrofit2.Response<Borrow> response) {
                        if (!isAdded()) return;

                        if (response.isSuccessful()) {
                            Toast.makeText(requireContext(), "Đã duyệt yêu cầu", Toast.LENGTH_SHORT).show();
                            loadRequests();
                        } else {
                            Toast.makeText(requireContext(), "Duyệt thất bại. Code: " + response.code(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull retrofit2.Call<Borrow> call,
                                          @NonNull Throwable t) {
                        if (!isAdded()) return;
                        Toast.makeText(requireContext(), "Lỗi kết nối BE: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void rejectBorrow(BorrowRequest request) {
        String token = sessionManager.getToken();

        borrowRepository.rejectBorrow(token, request.getId())
                .enqueue(new retrofit2.Callback<Borrow>() {
                    @Override
                    public void onResponse(@NonNull retrofit2.Call<Borrow> call,
                                           @NonNull retrofit2.Response<Borrow> response) {
                        if (!isAdded()) return;

                        if (response.isSuccessful()) {
                            Toast.makeText(requireContext(), "Đã từ chối yêu cầu", Toast.LENGTH_SHORT).show();
                            loadRequests();
                        } else {
                            Toast.makeText(requireContext(), "Từ chối thất bại. Code: " + response.code(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull retrofit2.Call<Borrow> call,
                                          @NonNull Throwable t) {
                        if (!isAdded()) return;
                        Toast.makeText(requireContext(), "Lỗi kết nối BE: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void returnBorrow(BorrowRequest request) {
        String token = sessionManager.getToken();

        borrowRepository.returnBorrow(token, request.getId())
                .enqueue(new retrofit2.Callback<Borrow>() {
                    @Override
                    public void onResponse(@NonNull retrofit2.Call<Borrow> call,
                                           @NonNull retrofit2.Response<Borrow> response) {
                        if (!isAdded()) return;

                        if (response.isSuccessful()) {
                            Toast.makeText(requireContext(), "Đã xác nhận trả sách", Toast.LENGTH_SHORT).show();
                            loadRequests();
                        } else {
                            Toast.makeText(requireContext(), "Trả sách thất bại. Code: " + response.code(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull retrofit2.Call<Borrow> call,
                                          @NonNull Throwable t) {
                        if (!isAdded()) return;
                        Toast.makeText(requireContext(), "Lỗi kết nối BE: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void confirmAction(String title, String message, Runnable action) {
        new AlertDialog.Builder(requireContext())
                .setTitle(title)
                .setMessage(message)
                .setNegativeButton("Huỷ", null)
                .setPositiveButton("Đồng ý", (dialog, which) -> action.run())
                .show();
    }
}