package com.example.book4u.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.book4u.R;
import com.example.book4u.models.Book;

import java.util.List;

public class AdminBookAdapter extends RecyclerView.Adapter<AdminBookAdapter.AdminBookViewHolder> {

    public interface OnAdminBookActionListener {
        void onEdit(Book book, int position);
        void onDelete(Book book, int position);
        void onToggleStock(Book book, int position);
    }

    private List<Book> bookList;
    private final OnAdminBookActionListener listener;

    public AdminBookAdapter(List<Book> bookList, OnAdminBookActionListener listener) {
        this.bookList = bookList;
        this.listener = listener;
    }

    public void setBookList(List<Book> bookList) {
        this.bookList = bookList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AdminBookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin_book, parent, false);
        return new AdminBookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminBookViewHolder holder, int position) {
        Book book = bookList.get(position);

        holder.tvAdminBookTitle.setText(book.getTitle());
        holder.tvAdminBookAuthor.setText(book.getAuthor());

        if (book.isAvailable()) {
            holder.tvAdminBookStatus.setText("Available");
            holder.tvAdminBookStatus.setTextColor(Color.parseColor("#6F42C1"));
            holder.tvToggleStock.setText("Mark unavailable");
        } else {
            holder.tvAdminBookStatus.setText("Out of stock");
            holder.tvAdminBookStatus.setTextColor(Color.parseColor("#D97706"));
            holder.tvToggleStock.setText("Mark available");
        }

        holder.tvEditBook.setOnClickListener(v -> {
            int currentPosition = holder.getAdapterPosition();
            if (currentPosition != RecyclerView.NO_POSITION && listener != null) {
                listener.onEdit(bookList.get(currentPosition), currentPosition);
            }
        });

        holder.tvDeleteBook.setOnClickListener(v -> {
            int currentPosition = holder.getAdapterPosition();
            if (currentPosition != RecyclerView.NO_POSITION && listener != null) {
                listener.onDelete(bookList.get(currentPosition), currentPosition);
            }
        });

        holder.tvToggleStock.setOnClickListener(v -> {
            int currentPosition = holder.getAdapterPosition();
            if (currentPosition != RecyclerView.NO_POSITION && listener != null) {
                listener.onToggleStock(bookList.get(currentPosition), currentPosition);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookList == null ? 0 : bookList.size();
    }

    static class AdminBookViewHolder extends RecyclerView.ViewHolder {
        TextView tvAdminBookTitle, tvAdminBookAuthor, tvAdminBookStatus;
        TextView tvEditBook, tvDeleteBook, tvToggleStock;

        public AdminBookViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAdminBookTitle = itemView.findViewById(R.id.tvAdminBookTitle);
            tvAdminBookAuthor = itemView.findViewById(R.id.tvAdminBookAuthor);
            tvAdminBookStatus = itemView.findViewById(R.id.tvAdminBookStatus);
            tvEditBook = itemView.findViewById(R.id.tvEditBook);
            tvDeleteBook = itemView.findViewById(R.id.tvDeleteBook);
            tvToggleStock = itemView.findViewById(R.id.tvToggleStock);
        }
    }
}