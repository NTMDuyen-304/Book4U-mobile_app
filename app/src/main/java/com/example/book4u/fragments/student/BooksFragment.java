package com.example.book4u.fragments.student;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.book4u.R;
import com.example.book4u.adapters.BookAdapter;
import com.example.book4u.models.Book;

import java.util.ArrayList;
import java.util.List;

public class BooksFragment extends Fragment {

    private EditText edtSearchBooks;
    private RecyclerView recyclerBooks;
    private BookAdapter bookAdapter;

    private final List<Book> fullBookList = new ArrayList<>();
    private final List<Book> filteredBookList = new ArrayList<>();

    public BooksFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_books, container, false);

        edtSearchBooks = view.findViewById(R.id.edtSearchBooks);
        recyclerBooks = view.findViewById(R.id.recyclerBooks);

        recyclerBooks.setLayoutManager(new LinearLayoutManager(requireContext()));

        setupMockBooks();

        filteredBookList.addAll(fullBookList);
        bookAdapter = new BookAdapter(filteredBookList);
        recyclerBooks.setAdapter(bookAdapter);

        edtSearchBooks.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterBooks(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        return view;
    }

    private void setupMockBooks() {
        fullBookList.add(new Book(
                "Clean Code",
                "Robert C. Martin",
                "A practical guide to writing cleaner, more maintainable, and more readable code.",
                true
        ));

        fullBookList.add(new Book(
                "Atomic Habits",
                "James Clear",
                "A book about building good habits, breaking bad ones, and improving yourself step by step.",
                true
        ));

        fullBookList.add(new Book(
                "Android Programming Basics",
                "Google Developers",
                "An introduction to Android development concepts, UI design, and app building fundamentals.",
                true
        ));

        fullBookList.add(new Book(
                "The Pragmatic Programmer",
                "Andrew Hunt",
                "A classic software engineering book that shares practical advice for becoming a better programmer.",
                true
        ));

        fullBookList.add(new Book(
                "Design Patterns",
                "Erich Gamma",
                "An influential book introducing reusable object-oriented design patterns for software development.",
                false
        ));
    }

    private void filterBooks(String keyword) {
        String query = keyword.trim().toLowerCase();

        filteredBookList.clear();

        for (Book book : fullBookList) {
            if (book.getTitle().toLowerCase().contains(query)
                    || book.getAuthor().toLowerCase().contains(query)) {
                filteredBookList.add(book);
            }
        }

        bookAdapter.setBookList(filteredBookList);
    }
}