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
import com.example.book4u.adapters.HistoryAdapter;
import com.example.book4u.models.HistoryItem;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment {

    private RecyclerView recyclerHistory;
    private HistoryAdapter historyAdapter;
    private final List<HistoryItem> historyItemList = new ArrayList<>();

    public HistoryFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        recyclerHistory = view.findViewById(R.id.recyclerHistory);
        recyclerHistory.setLayoutManager(new LinearLayoutManager(requireContext()));

        setupMockHistoryItems();

        historyAdapter = new HistoryAdapter(historyItemList);
        recyclerHistory.setAdapter(historyAdapter);

        return view;
    }

    private void setupMockHistoryItems() {
        historyItemList.add(new HistoryItem("The Alchemist", "2026-03-01", "2026-03-10", "Returned"));
        historyItemList.add(new HistoryItem("Think and Grow Rich", "2026-03-05", "2026-03-14", "Returned"));
        historyItemList.add(new HistoryItem("Deep Work", "2026-03-08", "2026-03-18", "Returned"));
    }
}