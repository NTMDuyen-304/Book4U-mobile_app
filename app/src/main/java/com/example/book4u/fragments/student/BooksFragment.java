package com.example.book4u.fragments.student;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.book4u.R;
import com.example.book4u.adapters.BookAdapter;
import com.example.book4u.models.Book;
import com.example.book4u.repository.BookRepository;
import com.example.book4u.repository.BorrowRepository;
import com.example.book4u.storage.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class BooksFragment extends Fragment {

    private EditText edtSearchBooks;
    private RecyclerView recyclerBooks;
    private BookAdapter bookAdapter;

    private final List<Book> bookList = new ArrayList<>();
    private BookRepository bookRepository;
    private BorrowRepository borrowRepository;
    private SessionManager sessionManager;

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

        bookRepository = new BookRepository(requireContext());
        borrowRepository = new BorrowRepository();
        sessionManager = new SessionManager(requireContext());

        recyclerBooks.setLayoutManager(new LinearLayoutManager(requireContext()));

        bookAdapter = new BookAdapter(bookList, book -> {
            borrowRepository.createBorrowLocal(requireContext(), book, sessionManager,
                    new BorrowRepository.ActionCallback() {
                        @Override
                        public void onSuccess(String message) {
                            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                            loadBooks();
                        }

                        @Override
                        public void onError(String message) {
                            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        recyclerBooks.setAdapter(bookAdapter);

        edtSearchBooks.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                loadBooks();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadBooks();
    }

    private void loadBooks() {
        String keyword = edtSearchBooks.getText().toString().trim();

        if (keyword.isEmpty()) {
            bookRepository.getAllLocalBooks(books ->
                    new Handler(Looper.getMainLooper()).post(() -> updateList(books)));
        } else {
            bookRepository.searchLocalBooks(keyword, books ->
                    new Handler(Looper.getMainLooper()).post(() -> updateList(books)));
        }
    }

    private void updateList(List<Book> books) {
        bookList.clear();
        bookList.addAll(books);
        bookAdapter.setBookList(bookList);
    }
}