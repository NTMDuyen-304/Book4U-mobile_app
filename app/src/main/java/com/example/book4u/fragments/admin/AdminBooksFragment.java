package com.example.book4u.fragments.admin;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.book4u.R;
import com.example.book4u.activities.AddEditBookActivity;
import com.example.book4u.adapters.AdminBookAdapter;
import com.example.book4u.models.Book;
import com.example.book4u.models.BookRequest;
import com.example.book4u.models.MessageResponse;
import com.example.book4u.repository.BookRepository;
import com.example.book4u.storage.SessionManager;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminBooksFragment extends Fragment {

    private EditText edtAdminSearchBooks;
    private RecyclerView recyclerAdminBooks;
    private Button btnAddBook;
    private AdminBookAdapter adminBookAdapter;

    private final List<Book> bookList = new ArrayList<>();

    private BookRepository bookRepository;
    private SessionManager sessionManager;

    public AdminBooksFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_books, container, false);

        edtAdminSearchBooks = view.findViewById(R.id.edtAdminSearchBooks);
        recyclerAdminBooks = view.findViewById(R.id.recyclerAdminBooks);
        btnAddBook = view.findViewById(R.id.btnAddBook);

        bookRepository = new BookRepository();
        sessionManager = new SessionManager(requireContext());

        recyclerAdminBooks.setLayoutManager(new LinearLayoutManager(requireContext()));

        adminBookAdapter = new AdminBookAdapter(bookList, new AdminBookAdapter.OnAdminBookActionListener() {
            @Override
            public void onEdit(Book book, int position) {
                openEditBookScreen(book);
            }

            @Override
            public void onDelete(Book book, int position) {
                confirmDeleteBook(book);
            }

            @Override
            public void onToggleStock(Book book, int position) {
                toggleBookAvailability(book);
            }
        });

        recyclerAdminBooks.setAdapter(adminBookAdapter);

        btnAddBook.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), AddEditBookActivity.class);
            startActivity(intent);
        });

        edtAdminSearchBooks.addTextChangedListener(new TextWatcher() {
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

    @Override
    public void onResume() {
        super.onResume();
        loadBooks();
    }

    private void loadBooks() {
        String token = sessionManager.getToken();

        if (token == null || token.trim().isEmpty()) {
            Toast.makeText(requireContext(), "Bạn chưa đăng nhập", Toast.LENGTH_SHORT).show();
            return;
        }

        String keyword = edtAdminSearchBooks.getText().toString().trim();

        bookRepository.getBooks(
                token,
                keyword.isEmpty() ? null : keyword,
                null
        ).enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(@NonNull Call<List<Book>> call,
                                   @NonNull Response<List<Book>> response) {
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
            public void onFailure(@NonNull Call<List<Book>> call,
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

    private void openEditBookScreen(Book book) {
        if (book.getId() == null || book.getId().trim().isEmpty()) {
            Toast.makeText(requireContext(), "Sách chưa có ID từ backend", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(requireContext(), AddEditBookActivity.class);
        intent.putExtra("bookId", book.getId());
        startActivity(intent);
    }

    private void confirmDeleteBook(Book book) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Xoá sách")
                .setMessage("Bạn có muốn xoá sách \"" + book.getTitle() + "\" không?")
                .setNegativeButton("Huỷ", null)
                .setPositiveButton("Xoá", (dialog, which) -> deleteBook(book))
                .show();
    }

    private void deleteBook(Book book) {
        String token = sessionManager.getToken();

        if (token == null || token.trim().isEmpty()) {
            Toast.makeText(requireContext(), "Bạn chưa đăng nhập", Toast.LENGTH_SHORT).show();
            return;
        }

        if (book.getId() == null || book.getId().trim().isEmpty()) {
            Toast.makeText(requireContext(), "Sách chưa có ID từ backend", Toast.LENGTH_SHORT).show();
            return;
        }

        bookRepository.deleteBook(token, book.getId())
                .enqueue(new Callback<MessageResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<MessageResponse> call,
                                           @NonNull Response<MessageResponse> response) {
                        if (!isAdded()) return;

                        if (response.isSuccessful()) {
                            Snackbar.make(requireView(), "Đã xoá: " + book.getTitle(), Snackbar.LENGTH_SHORT).show();
                            loadBooks();
                        } else {
                            Toast.makeText(
                                    requireContext(),
                                    "Không xoá được sách. Code: " + response.code(),
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<MessageResponse> call,
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

    private void toggleBookAvailability(Book book) {
        String token = sessionManager.getToken();

        if (token == null || token.trim().isEmpty()) {
            Toast.makeText(requireContext(), "Bạn chưa đăng nhập", Toast.LENGTH_SHORT).show();
            return;
        }

        if (book.getId() == null || book.getId().trim().isEmpty()) {
            Toast.makeText(requireContext(), "Sách chưa có ID từ backend", Toast.LENGTH_SHORT).show();
            return;
        }

        int newAvailable = book.isAvailable() ? 0 : 1;
        int newStock = Math.max(book.getStock(), newAvailable);

        BookRequest request = new BookRequest(
                book.getTitle(),
                book.getAuthor(),
                book.getCategory(),
                book.getImageUrl(),
                book.getDescription(),
                book.getIntro(),
                book.getFileUrl(),
                book.getPages(),
                newStock,
                newAvailable,
                newAvailable
        );

        bookRepository.updateBook(token, book.getId(), request)
                .enqueue(new Callback<Book>() {
                    @Override
                    public void onResponse(@NonNull Call<Book> call,
                                           @NonNull Response<Book> response) {
                        if (!isAdded()) return;

                        if (response.isSuccessful()) {
                            String msg = newAvailable > 0 ? "Đã bật trạng thái còn sách" : "Đã đánh dấu hết sách";
                            Snackbar.make(requireView(), msg + ": " + book.getTitle(), Snackbar.LENGTH_SHORT).show();
                            loadBooks();
                        } else {
                            Toast.makeText(
                                    requireContext(),
                                    "Không cập nhật được sách. Code: " + response.code(),
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Book> call,
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

        adminBookAdapter.setBookList(bookList);
    }
}