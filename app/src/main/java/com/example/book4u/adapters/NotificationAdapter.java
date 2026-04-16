package com.example.book4u.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.book4u.R;
import com.example.book4u.models.NotificationItem;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    public interface OnNotificationClickListener {
        void onNotificationClick(NotificationItem item);
    }

    private List<NotificationItem> notificationList;
    private final OnNotificationClickListener listener;

    public NotificationAdapter(List<NotificationItem> notificationList,
                               OnNotificationClickListener listener) {
        this.notificationList = notificationList;
        this.listener = listener;
    }

    public void setNotificationList(List<NotificationItem> notificationList) {
        this.notificationList = notificationList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        NotificationItem item = notificationList.get(position);

        holder.tvNotificationTitle.setText(item.getTitle());
        holder.tvNotificationMessage.setText(item.getMessage());
        holder.tvNotificationDate.setText(item.getCreatedAt());

        if (!item.isRead()) {
            holder.layoutNotificationItem.setBackgroundColor(Color.parseColor("#F3EEFF"));
            holder.tvNotificationTitle.setTextColor(Color.parseColor("#2A2A2A"));
        } else {
            holder.layoutNotificationItem.setBackgroundColor(Color.parseColor("#FFFFFF"));
            holder.tvNotificationTitle.setTextColor(Color.parseColor("#4B5563"));
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onNotificationClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notificationList == null ? 0 : notificationList.size();
    }

    static class NotificationViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layoutNotificationItem;
        TextView tvNotificationTitle, tvNotificationMessage, tvNotificationDate;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            layoutNotificationItem = itemView.findViewById(R.id.layoutNotificationItem);
            tvNotificationTitle = itemView.findViewById(R.id.tvNotificationTitle);
            tvNotificationMessage = itemView.findViewById(R.id.tvNotificationMessage);
            tvNotificationDate = itemView.findViewById(R.id.tvNotificationDate);
        }
    }
}