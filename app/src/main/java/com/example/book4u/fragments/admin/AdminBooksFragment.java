package com.example.book4u.fragments.admin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class AdminBooksFragment extends Fragment {

    private EditText edtAdminSearchBooks;
    private RecyclerView recyclerAdminBooks;
    private Button btnAddBook;
    private AdminBookAdapter adminBookAdapter;

    private final List<Book> fullBookList = new ArrayList<>();
    private final List<Book> filteredBookList = new ArrayList<>();

    private final ActivityResultLauncher<Intent> addEditBookLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                            Intent data = result.getData();

                            String title = data.getStringExtra("title");
                            String author = data.getStringExtra("author");
                            String intro = data.getStringExtra("intro");
                            boolean available = data.getBooleanExtra("available", true);
                            int bookIndex = data.getIntExtra("bookIndex", -1);
                            boolean isEditMode = data.getBooleanExtra("isEditMode", false);

                            if (isEditMode && bookIndex >= 0 && bookIndex < fullBookList.size()) {
                                Book bookToUpdate = fullBookList.get(bookIndex);
                                bookToUpdate.setTitle(title);
                                bookToUpdate.setAuthor(author);
                                bookToUpdate.setIntro(intro);
                                bookToUpdate.setAvailable(available);

                                Snackbar.make(requireView(), "Book updated: " + title, Snackbar.LENGTH_SHORT).show();
                            } else {
                                Book newBook = new Book(title, author, intro, available);
                                fullBookList.add(0, newBook);

                                Snackbar.make(requireView(), "Book added: " + title, Snackbar.LENGTH_SHORT).show();
                            }

                            filterBooks(edtAdminSearchBooks.getText().toString());
                        }
                    }
            );

    public AdminBooksFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_books, container, false);

        edtAdminSearchBooks = view.findViewById(R.id.edtAdminSearchBooks);
        recyclerAdminBooks = view.findViewById(R.id.recyclerAdminBooks);
        btnAddBook = view.findViewById(R.id.btnAddBook);

        recyclerAdminBooks.setLayoutManager(new LinearLayoutManager(requireContext()));

        if (fullBookList.isEmpty()) {
            setupMockBooks();
        }

        filteredBookList.clear();
        filteredBookList.addAll(fullBookList);

        adminBookAdapter = new AdminBookAdapter(filteredBookList, new AdminBookAdapter.OnAdminBookActionListener() {
            @Override
            public void onEdit(Book book, int position) {
                int originalIndex = fullBookList.indexOf(book);

                Intent intent = new Intent(requireContext(), AddEditBookActivity.class);
                intent.putExtra("title", book.getTitle());
                intent.putExtra("author", book.getAuthor());
                intent.putExtra("intro", book.getIntro());
                intent.putExtra("available", book.isAvailable());
                intent.putExtra("bookIndex", originalIndex);
                addEditBookLauncher.launch(intent);
            }

            @Override
            public void onDelete(Book book, int position) {
                new AlertDialog.Builder(requireContext())
                        .setTitle("Delete book")
                        .setMessage("Do you want to delete the book \"" + book.getTitle() + "\"?")
                        .setNegativeButton("Cancel", null)
                        .setPositiveButton("Delete", (dialog, which) -> {
                            fullBookList.remove(book);
                            filterBooks(edtAdminSearchBooks.getText().toString());

                            Snackbar.make(requireView(), "Deleted: " + book.getTitle(), Snackbar.LENGTH_SHORT).show();
                        })
                        .show();
            }

            @Override
            public void onToggleStock(Book book, int position) {
                book.setAvailable(!book.isAvailable());
                filterBooks(edtAdminSearchBooks.getText().toString());

                String msg = book.isAvailable() ? "Marked available" : "Marked unavailable";
                Snackbar.make(requireView(), msg + ": " + book.getTitle(), Snackbar.LENGTH_SHORT).show();
            }
        });

        recyclerAdminBooks.setAdapter(adminBookAdapter);

        edtAdminSearchBooks.addTextChangedListener(new TextWatcher() {
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

        btnAddBook.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), AddEditBookActivity.class);
            addEditBookLauncher.launch(intent);
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
                false
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
        String query = keyword == null ? "" : keyword.trim().toLowerCase();

        filteredBookList.clear();

        for (Book book : fullBookList) {
            String title = book.getTitle() == null ? "" : book.getTitle().toLowerCase();
            String author = book.getAuthor() == null ? "" : book.getAuthor().toLowerCase();

            if (title.contains(query) || author.contains(query)) {
                filteredBookList.add(book);
            }
        }

        adminBookAdapter.setBookList(filteredBookList);
    }
}