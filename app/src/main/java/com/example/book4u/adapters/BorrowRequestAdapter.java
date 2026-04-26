package com.example.book4u.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.book4u.R;
import com.example.book4u.models.BorrowRequest;

import java.util.List;

public class BorrowRequestAdapter extends RecyclerView.Adapter<BorrowRequestAdapter.BorrowRequestViewHolder> {

    public interface OnBorrowRequestActionListener {
        void onApprove(BorrowRequest request);
        void onReject(BorrowRequest request);
        void onReturn(BorrowRequest request);
    }

    private List<BorrowRequest> requestList;
    private final OnBorrowRequestActionListener listener;

    public BorrowRequestAdapter(List<BorrowRequest> requestList, OnBorrowRequestActionListener listener) {
        this.requestList = requestList;
        this.listener = listener;
    }

    public void setRequestList(List<BorrowRequest> requestList) {
        this.requestList = requestList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BorrowRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_borrow_request, parent, false);
        return new BorrowRequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BorrowRequestViewHolder holder, int position) {
        BorrowRequest request = requestList.get(position);

        holder.tvRequestBookTitle.setText(request.getBookTitle());

        String userLine = "User: " + request.getUserName();
        if (!request.getUserEmail().isEmpty()) {
            userLine += " (" + request.getUserEmail() + ")";
        }
        holder.tvRequestUserName.setText(userLine);

        String dateLine;
        if (request.isPending()) {
            dateLine = "Request date: " + request.getRequestDate();
        } else {
            dateLine = "Borrow: " + request.getBorrowDate() + " | Due: " + request.getDueDate();
        }
        holder.tvRequestDate.setText(dateLine);

        holder.tvRequestStatus.setText(request.getStatus());
        holder.tvRequestStatus.setTextColor(getStatusColor(request.getStatus()));

        holder.tvApproveRequest.setVisibility(View.VISIBLE);
        holder.tvRejectRequest.setVisibility(View.VISIBLE);

        holder.tvApproveRequest.setEnabled(true);
        holder.tvRejectRequest.setEnabled(true);

        holder.tvApproveRequest.setAlpha(1f);
        holder.tvRejectRequest.setAlpha(1f);

        holder.tvApproveRequest.setOnClickListener(null);
        holder.tvRejectRequest.setOnClickListener(null);

        if (request.isPending()) {
            holder.tvApproveRequest.setText("Approve");
            holder.tvApproveRequest.setTextColor(Color.parseColor("#16A34A"));

            holder.tvRejectRequest.setText("Reject");
            holder.tvRejectRequest.setTextColor(Color.parseColor("#DC2626"));

            holder.tvApproveRequest.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onApprove(request);
                }
            });

            holder.tvRejectRequest.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onReject(request);
                }
            });

        } else if (request.isBorrowing()) {
            holder.tvApproveRequest.setVisibility(View.GONE);

            holder.tvRejectRequest.setText("Return");
            holder.tvRejectRequest.setTextColor(Color.parseColor("#2563EB"));

            holder.tvRejectRequest.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onReturn(request);
                }
            });

        } else {
            holder.tvApproveRequest.setVisibility(View.GONE);

            holder.tvRejectRequest.setText("Processed");
            holder.tvRejectRequest.setEnabled(false);
            holder.tvRejectRequest.setAlpha(0.45f);
            holder.tvRejectRequest.setTextColor(Color.parseColor("#6B7280"));

            holder.tvRejectRequest.setOnClickListener(v ->
                    Toast.makeText(v.getContext(), "Request already processed", Toast.LENGTH_SHORT).show()
            );
        }
    }

    private int getStatusColor(String status) {
        if ("Borrowing".equalsIgnoreCase(status) || "Returned".equalsIgnoreCase(status)) {
            return Color.parseColor("#16A34A");
        }

        if ("Rejected".equalsIgnoreCase(status) || "Overdue".equalsIgnoreCase(status)) {
            return Color.parseColor("#DC2626");
        }

        return Color.parseColor("#D97706");
    }

    @Override
    public int getItemCount() {
        return requestList == null ? 0 : requestList.size();
    }

    static class BorrowRequestViewHolder extends RecyclerView.ViewHolder {
        TextView tvRequestBookTitle, tvRequestUserName, tvRequestDate, tvRequestStatus;
        TextView tvApproveRequest, tvRejectRequest;

        public BorrowRequestViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRequestBookTitle = itemView.findViewById(R.id.tvRequestBookTitle);
            tvRequestUserName = itemView.findViewById(R.id.tvRequestUserName);
            tvRequestDate = itemView.findViewById(R.id.tvRequestDate);
            tvRequestStatus = itemView.findViewById(R.id.tvRequestStatus);
            tvApproveRequest = itemView.findViewById(R.id.tvApproveRequest);
            tvRejectRequest = itemView.findViewById(R.id.tvRejectRequest);
        }
    }
}