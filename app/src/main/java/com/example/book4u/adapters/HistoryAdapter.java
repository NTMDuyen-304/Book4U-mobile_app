package com.example.book4u.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.book4u.R;
import com.example.book4u.models.HistoryItem;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private List<HistoryItem> historyList;

    public HistoryAdapter(List<HistoryItem> historyList) {
        this.historyList = historyList;
    }

    public void setHistoryList(List<HistoryItem> historyList) {
        this.historyList = historyList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        HistoryItem item = historyList.get(position);
        holder.tvHistoryBookTitle.setText(item.getTitle());
        holder.tvHistoryBorrowDate.setText("Borrowed: " + item.getBorrowDate());
        holder.tvHistoryReturnDate.setText("Returned: " + item.getReturnDate());
        holder.tvHistoryStatus.setText(item.getStatus());
    }

    @Override
    public int getItemCount() {
        return historyList == null ? 0 : historyList.size();
    }

    static class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvHistoryBookTitle, tvHistoryBorrowDate, tvHistoryReturnDate, tvHistoryStatus;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHistoryBookTitle = itemView.findViewById(R.id.tvHistoryBookTitle);
            tvHistoryBorrowDate = itemView.findViewById(R.id.tvHistoryBorrowDate);
            tvHistoryReturnDate = itemView.findViewById(R.id.tvHistoryReturnDate);
            tvHistoryStatus = itemView.findViewById(R.id.tvHistoryStatus);
        }
    }
}