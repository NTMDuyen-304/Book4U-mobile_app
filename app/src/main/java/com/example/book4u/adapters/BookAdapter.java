package com.example.book4u.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.book4u.R;
import com.example.book4u.models.Book;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    private List<Book> bookList;

    public BookAdapter(List<Book> bookList) {
        this.bookList = bookList;
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

        holder.btnBorrow.setOnClickListener(v ->
                Toast.makeText(v.getContext(), "Borrow: " + book.getTitle(), Toast.LENGTH_SHORT).show()
        );
        holder.itemView.setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(v.getContext(), com.example.book4u.activities.BookDetailActivity.class);
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
        TextView tvBookTitle, tvBookAuthor;
        Button btnBorrow;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBookTitle = itemView.findViewById(R.id.tvBookTitle);
            tvBookAuthor = itemView.findViewById(R.id.tvBookAuthor);
            btnBorrow = itemView.findViewById(R.id.btnBorrow);
        }
    }
}