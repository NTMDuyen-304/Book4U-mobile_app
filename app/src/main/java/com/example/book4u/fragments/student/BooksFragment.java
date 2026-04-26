package com.example.book4u.fragments.student;

import android.os.Bundle;
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
import com.example.book4u.models.Borrow;
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

        bookRepository = new BookRepository();
        borrowRepository = new BorrowRepository();
        sessionManager = new SessionManager(requireContext());

        recyclerBooks.setLayoutManager(new LinearLayoutManager(requireContext()));

        bookAdapter = new BookAdapter(bookList, this::borrowBook);
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

        loadBooks();

        return view;
    }

    private void loadBooks() {
        String token = sessionManager.getToken();

        if (token == null || token.trim().isEmpty()) {
            Toast.makeText(requireContext(), "Bạn chưa đăng nhập", Toast.LENGTH_SHORT).show();
            return;
        }

        String keyword = edtSearchBooks.getText().toString().trim();

        bookRepository.getBooks(
                token,
                keyword.isEmpty() ? null : keyword,
                null
        ).enqueue(new retrofit2.Callback<List<Book>>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<List<Book>> call,
                                   @NonNull retrofit2.Response<List<Book>> response) {

                if (!isAdded()) return;

                if (response.isSuccessful() && response.body() != null) {
                    updateList(response.body());
                } else {
                    Toast.makeText(
                            requireContext(),
                            "Không tải được danh sách sách. Code: " + response.code(),
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }

            @Override
            public void onFailure(@NonNull retrofit2.Call<List<Book>> call,
                                  @NonNull Throwable t) {

                if (!isAdded()) return;

                Toast.makeText(
                        requireContext(),
                        "Lỗi kết nối BE: " + t.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }

    private void borrowBook(Book book) {
        String token = sessionManager.getToken();

        if (token == null || token.trim().isEmpty()) {
            Toast.makeText(requireContext(), "Bạn chưa đăng nhập", Toast.LENGTH_SHORT).show();
            return;
        }

        if (book.getId() == null || book.getId().trim().isEmpty()) {
            Toast.makeText(requireContext(), "Sách chưa có ID từ backend", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!book.isAvailable()) {
            Toast.makeText(requireContext(), "Sách đã hết", Toast.LENGTH_SHORT).show();
            return;
        }

        borrowRepository.createBorrow(token, book.getId())
                .enqueue(new retrofit2.Callback<Borrow>() {
                    @Override
                    public void onResponse(@NonNull retrofit2.Call<Borrow> call,
                                           @NonNull retrofit2.Response<Borrow> response) {

                        if (!isAdded()) return;

                        if (response.isSuccessful()) {
                            Toast.makeText(
                                    requireContext(),
                                    "Đã gửi yêu cầu mượn sách",
                                    Toast.LENGTH_SHORT
                            ).show();

                            loadBooks();
                        } else {
                            Toast.makeText(
                                    requireContext(),
                                    "Không thể mượn sách. Code: " + response.code(),
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull retrofit2.Call<Borrow> call,
                                          @NonNull Throwable t) {

                        if (!isAdded()) return;

                        Toast.makeText(
                                requireContext(),
                                "Lỗi kết nối BE: " + t.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });
    }

    private void updateList(List<Book> books) {
        bookList.clear();

        if (books != null) {
            bookList.addAll(books);
        }

        bookAdapter.setBookList(bookList);
    }
}