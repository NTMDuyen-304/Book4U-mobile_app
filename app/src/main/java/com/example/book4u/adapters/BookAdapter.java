//package com.example.book4u.adapters;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.book4u.R;
//import com.example.book4u.models.Book;
//
//import java.util.List;
//
//public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {
//
//    private List<Book> bookList;
//
//    public BookAdapter(List<Book> bookList) {
//        this.bookList = bookList;
//    }
//
//    public void setBookList(List<Book> bookList) {
//        this.bookList = bookList;
//        notifyDataSetChanged();
//    }
//
//    @NonNull
//    @Override
//    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book, parent, false);
//        return new BookViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
//        Book book = bookList.get(position);
//        holder.tvBookTitle.setText(book.getTitle());
//        holder.tvBookAuthor.setText(book.getAuthor());
//
//        holder.btnBorrow.setOnClickListener(v ->
//                Toast.makeText(v.getContext(), "Borrow: " + book.getTitle(), Toast.LENGTH_SHORT).show()
//        );
//        holder.itemView.setOnClickListener(v -> {
//            android.content.Intent intent = new android.content.Intent(v.getContext(), com.example.book4u.activities.BookDetailActivity.class);
//            intent.putExtra("title", book.getTitle());
//            intent.putExtra("author", book.getAuthor());
//            intent.putExtra("intro", book.getIntro());
//            intent.putExtra("available", book.isAvailable());
//            v.getContext().startActivity(intent);
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return bookList == null ? 0 : bookList.size();
//    }
//
//    static class BookViewHolder extends RecyclerView.ViewHolder {
//        TextView tvBookTitle, tvBookAuthor;
//        Button btnBorrow;
//
//        public BookViewHolder(@NonNull View itemView) {
//            super(itemView);
//            tvBookTitle = itemView.findViewById(R.id.tvBookTitle);
//            tvBookAuthor = itemView.findViewById(R.id.tvBookAuthor);
//            btnBorrow = itemView.findViewById(R.id.btnBorrow);
//        }
//    }
//}
package com.example.book4u.adapters;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.book4u.R;
import com.example.book4u.activities.BookDetailActivity;
import com.example.book4u.models.Book;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    public interface OnBorrowClickListener {
        void onBorrowClick(Book book);
    }

    private List<Book> bookList;
    private final OnBorrowClickListener listener;

    public BookAdapter(List<Book> bookList, OnBorrowClickListener listener) {
        this.bookList = bookList;
        this.listener = listener;
    }

    public void setBookList(List<Book> bookList) {
        this.bookList = bookList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = bookList.get(position);

        holder.tvBookTitle.setText(book.getTitle());
        holder.tvBookAuthor.setText(book.getAuthor());

        if (book.isAvailable()) {
            holder.tvBookStatus.setText("Available");
            holder.tvBookStatus.setTextColor(Color.parseColor("#6F42C1"));
            holder.btnBorrow.setEnabled(true);
            holder.btnBorrow.setAlpha(1f);
            holder.btnBorrow.setText("Borrow");
        } else {
            holder.tvBookStatus.setText("Out of stock");
            holder.tvBookStatus.setTextColor(Color.parseColor("#D97706"));
            holder.btnBorrow.setEnabled(false);
            holder.btnBorrow.setAlpha(0.5f);
            holder.btnBorrow.setText("Unavailable");
        }

        holder.btnBorrow.setOnClickListener(v -> {
            if (!book.isAvailable()) {
                Toast.makeText(v.getContext(), "Book is out of stock", Toast.LENGTH_SHORT).show();
                return;
            }
            if (listener != null) {
                listener.onBorrowClick(book);
            }
        });

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), BookDetailActivity.class);
            intent.putExtra("title", book.getTitle());
            intent.putExtra("author", book.getAuthor());
            intent.putExtra("intro", book.getIntro());
            intent.putExtra("available", book.isAvailable());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return bookList == null ? 0 : bookList.size();
    }

    static class BookViewHolder extends RecyclerView.ViewHolder {
        TextView tvBookTitle, tvBookAuthor, tvBookStatus;
        Button btnBorrow;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBookTitle = itemView.findViewById(R.id.tvBookTitle);
            tvBookAuthor = itemView.findViewById(R.id.tvBookAuthor);
            tvBookStatus = itemView.findViewById(R.id.tvBookStatus);
            btnBorrow = itemView.findViewById(R.id.btnBorrow);
        }
    }
}