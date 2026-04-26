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
import com.example.book4u.adapters.HistoryAdapter;
import com.example.book4u.models.Borrow;
import com.example.book4u.models.HistoryItem;
import com.example.book4u.repository.BorrowRepository;
import com.example.book4u.storage.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment {

    private RecyclerView recyclerHistory;
    private HistoryAdapter historyAdapter;

    private final List<HistoryItem> historyItemList = new ArrayList<>();

    private BorrowRepository borrowRepository;
    private SessionManager sessionManager;

    public HistoryFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        recyclerHistory = view.findViewById(R.id.recyclerHistory);
        recyclerHistory.setLayoutManager(new LinearLayoutManager(requireContext()));

        borrowRepository = new BorrowRepository();
        sessionManager = new SessionManager(requireContext());

        historyAdapter = new HistoryAdapter(historyItemList);
        recyclerHistory.setAdapter(historyAdapter);

        loadHistory();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadHistory();
    }

    private void loadHistory() {
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
                            historyItemList.clear();

                            for (Borrow borrow : response.body()) {
                                if (borrow.isReturned() || borrow.isRejected()) {
                                    historyItemList.add(new HistoryItem(
                                            borrow.getBookTitle(),
                                            borrow.getBorrowDate(),
                                            borrow.getDueDate(),
                                            borrow.getStatus()
                                    ));
                                }
                            }

                            historyAdapter.setHistoryList(historyItemList);
                        } else {
                            Toast.makeText(
                                    requireContext(),
                                    "Không tải được lịch sử. Code: " + response.code(),
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