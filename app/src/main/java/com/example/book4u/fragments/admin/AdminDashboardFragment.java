package com.example.book4u.fragments.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.book4u.R;
import com.example.book4u.models.Book;
import com.example.book4u.models.Borrow;
import com.example.book4u.repository.BookRepository;
import com.example.book4u.repository.BorrowRepository;
import com.example.book4u.storage.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class AdminDashboardFragment extends Fragment {

    private TextView tvTotalBooks;
    private TextView tvAvailableBooks;
    private TextView tvPendingRequests;
    private TextView tvBorrowing;
    private TextView tvReturned;
    private TextView tvRejected;

    private Button btnRefreshDashboard;
    private Button btnManageBooks;
    private Button btnManageBorrow;

    private BookRepository bookRepository;
    private BorrowRepository borrowRepository;
    private SessionManager sessionManager;

    public AdminDashboardFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_dashboard, container, false);

        tvTotalBooks = view.findViewById(R.id.tvTotalBooks);
        tvAvailableBooks = view.findViewById(R.id.tvAvailableBooks);
        tvPendingRequests = view.findViewById(R.id.tvPendingRequests);
        tvBorrowing = view.findViewById(R.id.tvBorrowing);
        tvReturned = view.findViewById(R.id.tvReturned);
        tvRejected = view.findViewById(R.id.tvRejected);

        btnRefreshDashboard = view.findViewById(R.id.btnRefreshDashboard);
        btnManageBooks = view.findViewById(R.id.btnManageBooks);
        btnManageBorrow = view.findViewById(R.id.btnManageBorrow);

        bookRepository = new BookRepository();
        borrowRepository = new BorrowRepository();
        sessionManager = new SessionManager(requireContext());

        setupButtons();
        loadDashboard();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadDashboard();
    }

    private void setupButtons() {
        btnRefreshDashboard.setOnClickListener(v -> loadDashboard());

        btnManageBooks.setOnClickListener(v -> {
            BottomNavigationView bottomNav = requireActivity().findViewById(R.id.bottomNavAdmin);
            if (bottomNav != null) {
                bottomNav.setSelectedItemId(R.id.nav_admin_books);
            }
        });

        btnManageBorrow.setOnClickListener(v -> {
            BottomNavigationView bottomNav = requireActivity().findViewById(R.id.bottomNavAdmin);
            if (bottomNav != null) {
                bottomNav.setSelectedItemId(R.id.nav_admin_borrow);
            }
        });
    }

    private void loadDashboard() {
        String token = sessionManager.getToken();

        if (token == null || token.trim().isEmpty()) {
            Toast.makeText(requireContext(), "Bạn chưa đăng nhập", Toast.LENGTH_SHORT).show();
            return;
        }

        loadBookStats(token);
        loadBorrowStats(token);
    }

    private void loadBookStats(String token) {
        bookRepository.getBooks(token, null, null)
                .enqueue(new retrofit2.Callback<List<Book>>() {
                    @Override
                    public void onResponse(@NonNull retrofit2.Call<List<Book>> call,
                                           @NonNull retrofit2.Response<List<Book>> response) {
                        if (!isAdded()) return;

                        if (response.isSuccessful() && response.body() != null) {
                            List<Book> books = response.body();

                            int totalBooks = books.size();
                            int availableBooks = 0;

                            for (Book book : books) {
                                if (book.isAvailable()) {
                                    availableBooks++;
                                }
                            }

                            tvTotalBooks.setText(String.valueOf(totalBooks));
                            tvAvailableBooks.setText(String.valueOf(availableBooks));
                        } else {
                            Toast.makeText(
                                    requireContext(),
                                    "Không tải được thống kê sách. Code: " + response.code(),
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
                                "Lỗi tải thống kê sách: " + t.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });
    }

    private void loadBorrowStats(String token) {
        borrowRepository.getAllBorrows(token)
                .enqueue(new retrofit2.Callback<List<Borrow>>() {
                    @Override
                    public void onResponse(@NonNull retrofit2.Call<List<Borrow>> call,
                                           @NonNull retrofit2.Response<List<Borrow>> response) {
                        if (!isAdded()) return;

                        if (response.isSuccessful() && response.body() != null) {
                            List<Borrow> borrows = response.body();

                            int pending = 0;
                            int borrowing = 0;
                            int returned = 0;
                            int rejected = 0;

                            for (Borrow borrow : borrows) {
                                String status = borrow.getStatus();

                                if (status == null) {
                                    continue;
                                }

                                if ("Pending Approval".equalsIgnoreCase(status)
                                        || "Pending".equalsIgnoreCase(status)) {
                                    pending++;
                                } else if ("Borrowing".equalsIgnoreCase(status)
                                        || "Overdue".equalsIgnoreCase(status)) {
                                    borrowing++;
                                } else if ("Returned".equalsIgnoreCase(status)) {
                                    returned++;
                                } else if ("Rejected".equalsIgnoreCase(status)) {
                                    rejected++;
                                }
                            }

                            tvPendingRequests.setText(String.valueOf(pending));
                            tvBorrowing.setText(String.valueOf(borrowing));
                            tvReturned.setText(String.valueOf(returned));
                            tvRejected.setText(String.valueOf(rejected));
                        } else {
                            Toast.makeText(
                                    requireContext(),
                                    "Không tải được thống kê mượn/trả. Code: " + response.code(),
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull retrofit2.Call<List<Borrow>> call,
                                          @NonNull Throwable t) {
                        if (!isAdded()) return;

                        Toast.makeText(
                                requireContext(),
                                "Lỗi tải thống kê mượn/trả: " + t.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });
    }
}