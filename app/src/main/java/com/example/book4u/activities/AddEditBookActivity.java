//package com.example.book4u.activities;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.widget.Button;
//import android.widget.CheckBox;
//import android.widget.EditText;
//
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.book4u.R;
//import com.google.android.material.appbar.MaterialToolbar;
//import com.google.android.material.snackbar.Snackbar;
//
//public class AddEditBookActivity extends AppCompatActivity {
//
//    private MaterialToolbar toolbarAddEditBook;
//    private EditText edtBookTitle, edtBookAuthor, edtBookIntro;
//    private CheckBox cbBookAvailable;
//    private Button btnSaveBook;
//
//    private int bookIndex = -1;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_add_edit_book);
//
//        toolbarAddEditBook = findViewById(R.id.toolbarAddEditBook);
//        edtBookTitle = findViewById(R.id.edtBookTitle);
//        edtBookAuthor = findViewById(R.id.edtBookAuthor);
//        edtBookIntro = findViewById(R.id.edtBookIntro);
//        cbBookAvailable = findViewById(R.id.cbBookAvailable);
//        btnSaveBook = findViewById(R.id.btnSaveBook);
//
//        String title = getIntent().getStringExtra("title");
//        String author = getIntent().getStringExtra("author");
//        String intro = getIntent().getStringExtra("intro");
//        boolean available = getIntent().getBooleanExtra("available", true);
//        bookIndex = getIntent().getIntExtra("bookIndex", -1);
//
//        boolean isEditMode = bookIndex >= 0;
//
//        if (isEditMode) {
//            toolbarAddEditBook.setTitle("Edit Book");
//            btnSaveBook.setText("Update");
//
//            edtBookTitle.setText(title);
//            edtBookAuthor.setText(author);
//            edtBookIntro.setText(intro);
//            cbBookAvailable.setChecked(available);
//        } else {
//            toolbarAddEditBook.setTitle("Add Book");
//            btnSaveBook.setText("Save");
//        }
//
//        toolbarAddEditBook.setNavigationOnClickListener(v -> finish());
//        btnSaveBook.setOnClickListener(v -> saveBook(v, isEditMode));
//    }
//
//    private void saveBook(android.view.View view, boolean isEditMode) {
//        String title = edtBookTitle.getText().toString().trim();
//        String author = edtBookAuthor.getText().toString().trim();
//        String intro = edtBookIntro.getText().toString().trim();
//        boolean available = cbBookAvailable.isChecked();
//
//        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(author) || TextUtils.isEmpty(intro)) {
//            Snackbar.make(view, "Please fill in all fields", Snackbar.LENGTH_SHORT).show();
//            return;
//        }
//
//        Intent resultIntent = new Intent();
//        resultIntent.putExtra("title", title);
//        resultIntent.putExtra("author", author);
//        resultIntent.putExtra("intro", intro);
//        resultIntent.putExtra("available", available);
//        resultIntent.putExtra("bookIndex", bookIndex);
//        resultIntent.putExtra("isEditMode", isEditMode);
//
//        setResult(RESULT_OK, resultIntent);
//        finish();
//    }
//}
package com.example.book4u.activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.book4u.R;
import com.example.book4u.models.Book;
import com.example.book4u.repository.BookRepository;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.snackbar.Snackbar;

public class AddEditBookActivity extends AppCompatActivity {

    private MaterialToolbar toolbarAddEditBook;
    private EditText edtBookTitle, edtBookAuthor, edtBookIntro;
    private CheckBox cbBookAvailable;
    private Button btnSaveBook;

    private BookRepository bookRepository;
    private int bookId = -1;
    private Book currentBook;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_book);

        toolbarAddEditBook = findViewById(R.id.toolbarAddEditBook);
        edtBookTitle = findViewById(R.id.edtBookTitle);
        edtBookAuthor = findViewById(R.id.edtBookAuthor);
        edtBookIntro = findViewById(R.id.edtBookIntro);
        cbBookAvailable = findViewById(R.id.cbBookAvailable);
        btnSaveBook = findViewById(R.id.btnSaveBook);

        bookRepository = new BookRepository(this);

        bookId = getIntent().getIntExtra("bookId", -1);
        boolean isEditMode = bookId != -1;

        if (isEditMode) {
            toolbarAddEditBook.setTitle("Edit Book");
            btnSaveBook.setText("Update");
            loadBook();
        } else {
            toolbarAddEditBook.setTitle("Add Book");
            btnSaveBook.setText("Save");
        }

        toolbarAddEditBook.setNavigationOnClickListener(v -> finish());
        btnSaveBook.setOnClickListener(v -> saveBook());
    }

    private void loadBook() {
        bookRepository.getLocalBookById(bookId, book ->
                new Handler(Looper.getMainLooper()).post(() -> {
                    if (book != null) {
                        currentBook = book;
                        edtBookTitle.setText(book.getTitle());
                        edtBookAuthor.setText(book.getAuthor());
                        edtBookIntro.setText(book.getIntro());
                        cbBookAvailable.setChecked(book.isAvailable());
                    }
                }));
    }

    private void saveBook() {
        String title = edtBookTitle.getText().toString().trim();
        String author = edtBookAuthor.getText().toString().trim();
        String intro = edtBookIntro.getText().toString().trim();
        boolean available = cbBookAvailable.isChecked();

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(author) || TextUtils.isEmpty(intro)) {
            Snackbar.make(btnSaveBook, "Please fill in all fields", Snackbar.LENGTH_SHORT).show();
            return;
        }

        if (bookId == -1) {
            Book newBook = new Book(title, author, intro, available);
            bookRepository.insertLocal(newBook);
            Snackbar.make(btnSaveBook, "Book added", Snackbar.LENGTH_SHORT).show();
        } else {
            currentBook.setTitle(title);
            currentBook.setAuthor(author);
            currentBook.setIntro(intro);
            currentBook.setAvailable(available);
            bookRepository.updateLocal(currentBook);
            Snackbar.make(btnSaveBook, "Book updated", Snackbar.LENGTH_SHORT).show();
        }

        finish();
    }
}