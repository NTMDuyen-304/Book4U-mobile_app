package com.example.book4u.fragments.student;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.book4u.R;
import com.example.book4u.adapters.BorrowAdapter;
import com.example.book4u.models.Borrow;
import com.example.book4u.models.BorrowItem;
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
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_borrow, container, false);

        recyclerMyBorrow = view.findViewById(R.id.recyclerMyBorrow);
        recyclerMyBorrow.setLayoutManager(new LinearLayoutManager(requireContext()));

        borrowRepository = new BorrowRepository();
        sessionManager = new SessionManager(requireContext());

        borrowAdapter = new BorrowAdapter(borrowItemList);
        recyclerMyBorrow.setAdapter(borrowAdapter);

        loadMyBorrows();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadMyBorrows();
    }

    private void loadMyBorrows() {
        String token = sessionManager.getToken();

        if (token == null || token.trim().isEmpty()) {
            Toast.makeText(requireContext(), "Bạn chưa đăng nhập", Toast.LENGTH_SHORT).show();
            return;
        }

        borrowRepository.getMyBorrows(token)
                .enqueue(new retrofit2.Callback<List<Borrow>>() {
                    @Override
                    public void onResponse(@NonNull retrofit2.Call<List<Borrow>> call,
                                           @NonNull retrofit2.Response<List<Borrow>> response) {
                        if (!isAdded()) return;

                        if (response.isSuccessful() && response.body() != null) {
                            borrowItemList.clear();

                            for (Borrow borrow : response.body()) {
                                if (!borrow.isReturned()) {
                                    borrowItemList.add(new BorrowItem(
                                            borrow.getBookTitle(),
                                            borrow.getBorrowDate(),
                                            borrow.getDueDate(),
                                            borrow.getStatus()
                                    ));
                                }
                            }

                            borrowAdapter.setBorrowList(borrowItemList);
                        } else {
                            Toast.makeText(
                                    requireContext(),
                                    "Không tải được phiếu mượn. Code: " + response.code(),
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
}