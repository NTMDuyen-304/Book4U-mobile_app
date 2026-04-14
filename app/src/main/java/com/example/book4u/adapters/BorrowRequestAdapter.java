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

    private List<BorrowRequest> requestList;

    public BorrowRequestAdapter(List<BorrowRequest> requestList) {
        this.requestList = requestList;
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

        holder.tvApproveRequest.setOnClickListener(null);
        holder.tvRejectRequest.setOnClickListener(null);

        if (isPending) {
            holder.tvApproveRequest.setOnClickListener(v -> {
                request.setStatus("Approved");
                notifyItemChanged(holder.getAdapterPosition());

                v.animate().scaleX(1.08f).scaleY(1.08f).setDuration(100).withEndAction(() ->
                        v.animate().scaleX(1f).scaleY(1f).setDuration(100)
                ).start();

                Toast.makeText(v.getContext(), "Approved: " + request.getBookTitle(), Toast.LENGTH_SHORT).show();
            });

            holder.tvRejectRequest.setOnClickListener(v -> {
                request.setStatus("Rejected");
                notifyItemChanged(holder.getAdapterPosition());

                v.animate().scaleX(1.08f).scaleY(1.08f).setDuration(100).withEndAction(() ->
                        v.animate().scaleX(1f).scaleY(1f).setDuration(100)
                ).start();

                Toast.makeText(v.getContext(), "Rejected: " + request.getBookTitle(), Toast.LENGTH_SHORT).show();
            });
        }
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