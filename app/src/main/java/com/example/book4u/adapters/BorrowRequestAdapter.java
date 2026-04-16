package com.example.book4u.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.book4u.R;
import com.example.book4u.models.BorrowRequest;

import java.util.List;

public class BorrowRequestAdapter extends RecyclerView.Adapter<BorrowRequestAdapter.BorrowRequestViewHolder> {

    public interface OnBorrowRequestActionListener {
        void onApprove(BorrowRequest request);
        void onReject(BorrowRequest request);
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_borrow_request, parent, false);
        return new BorrowRequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BorrowRequestViewHolder holder, int position) {
        BorrowRequest request = requestList.get(position);

        holder.tvRequestBookTitle.setText(request.getBookTitle());
        holder.tvRequestUserName.setText("Requested by: " + request.getUserName());
        holder.tvRequestDate.setText("Date: " + request.getRequestDate());
        setStatusStyle(holder.tvRequestStatus, request.getStatus());

        boolean isPending = "Pending".equalsIgnoreCase(request.getStatus());

        holder.tvApproveRequest.setEnabled(isPending);
        holder.tvRejectRequest.setEnabled(isPending);
        holder.tvApproveRequest.setAlpha(isPending ? 1f : 0.4f);
        holder.tvRejectRequest.setAlpha(isPending ? 1f : 0.4f);

        holder.tvApproveRequest.setOnClickListener(v -> {
            if (isPending && listener != null) listener.onApprove(request);
        });

        holder.tvRejectRequest.setOnClickListener(v -> {
            if (isPending && listener != null) listener.onReject(request);
        });
    }

    private void setStatusStyle(TextView tvStatus, String status) {
        tvStatus.setText(status);

        if ("Approved".equalsIgnoreCase(status)) {
            tvStatus.setTextColor(Color.parseColor("#16A34A"));
        } else if ("Rejected".equalsIgnoreCase(status)) {
            tvStatus.setTextColor(Color.parseColor("#DC2626"));
        } else {
            tvStatus.setTextColor(Color.parseColor("#D97706"));
        }
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