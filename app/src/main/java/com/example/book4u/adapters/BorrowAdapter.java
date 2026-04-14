package com.example.book4u.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.book4u.R;
import com.example.book4u.models.BorrowItem;

import java.util.List;

public class BorrowAdapter extends RecyclerView.Adapter<BorrowAdapter.BorrowViewHolder> {

    private List<BorrowItem> borrowList;

    public BorrowAdapter(List<BorrowItem> borrowList) {
        this.borrowList = borrowList;
    }

    public void setBorrowList(List<BorrowItem> borrowList) {
        this.borrowList = borrowList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BorrowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_borrow, parent, false);
        return new BorrowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BorrowViewHolder holder, int position) {
        BorrowItem item = borrowList.get(position);
        holder.tvBorrowBookTitle.setText(item.getTitle());
        holder.tvBorrowDate.setText("Borrowed: " + item.getBorrowDate());
        holder.tvDueDate.setText("Due: " + item.getDueDate());
        holder.tvBorrowStatus.setText(item.getStatus());
    }

    @Override
    public int getItemCount() {
        return borrowList == null ? 0 : borrowList.size();
    }

    static class BorrowViewHolder extends RecyclerView.ViewHolder {
        TextView tvBorrowBookTitle, tvBorrowDate, tvDueDate, tvBorrowStatus;

        public BorrowViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBorrowBookTitle = itemView.findViewById(R.id.tvBorrowBookTitle);
            tvBorrowDate = itemView.findViewById(R.id.tvBorrowDate);
            tvDueDate = itemView.findViewById(R.id.tvDueDate);
            tvBorrowStatus = itemView.findViewById(R.id.tvBorrowStatus);
        }
    }
}