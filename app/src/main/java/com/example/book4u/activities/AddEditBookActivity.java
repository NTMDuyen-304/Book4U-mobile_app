package com.example.book4u.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.book4u.R;
import com.example.book4u.models.Book;
import com.example.book4u.models.BookRequest;
import com.example.book4u.repository.BookRepository;
import com.example.book4u.storage.SessionManager;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddEditBookActivity extends AppCompatActivity {

    private MaterialToolbar toolbarAddEditBook;
    private EditText edtBookTitle, edtBookAuthor, edtBookIntro;
    private CheckBox cbBookAvailable;
    private Button btnSaveBook;

    private BookRepository bookRepository;
    private SessionManager sessionManager;

    private String bookId = null;
    private boolean isEditMode = false;

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

        bookRepository = new BookRepository();
        sessionManager = new SessionManager(this);

        bookId = getIntent().getStringExtra("bookId");
        isEditMode = bookId != null && !bookId.trim().isEmpty();

        setupToolbar();

        if (isEditMode) {
            loadBookDetail();
        } else {
            toolbarAddEditBook.setTitle("Thêm sách");
            btnSaveBook.setText("Thêm sách");
            cbBookAvailable.setChecked(true);
        }

        btnSaveBook.setOnClickListener(v -> saveBook());
    }

    private void setupToolbar() {
        setSupportActionBar(toolbarAddEditBook);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(isEditMode ? "Sửa sách" : "Thêm sách");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        toolbarAddEditBook.setNavigationOnClickListener(v -> finish());
    }

    private void loadBookDetail() {
        String token = sessionManager.getToken();

        if (token == null || token.trim().isEmpty()) {
            Toast.makeText(this, "Bạn chưa đăng nhập", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        setLoading(true);

        bookRepository.getBookById(token, bookId).enqueue(new Callback<Book>() {
            @Override
            public void onResponse(@NonNull Call<Book> call,
                                   @NonNull Response<Book> response) {
                setLoading(false);

                if (response.isSuccessful() && response.body() != null) {
                    currentBook = response.body();
                    bindBookToForm(currentBook);
                } else {
                    Toast.makeText(
                            AddEditBookActivity.this,
                            "Không tải được thông tin sách. Code: " + response.code(),
                            Toast.LENGTH_SHORT
                    ).show();
                    finish();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Book> call,
                                  @NonNull Throwable t) {
                setLoading(false);
                Toast.makeText(
                        AddEditBookActivity.this,
                        "Lỗi kết nối BE: " + t.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
                finish();
            }
        });
    }

    private void bindBookToForm(Book book) {
        edtBookTitle.setText(book.getTitle());
        edtBookAuthor.setText(book.getAuthor());
        edtBookIntro.setText(book.getIntro());
        cbBookAvailable.setChecked(book.isAvailable());
    }

    private void saveBook() {
        String title = edtBookTitle.getText().toString().trim();
        String author = edtBookAuthor.getText().toString().trim();
        String intro = edtBookIntro.getText().toString().trim();
        boolean availableChecked = cbBookAvailable.isChecked();

        if (TextUtils.isEmpty(title)) {
            edtBookTitle.setError("Vui lòng nhập tên sách");
            edtBookTitle.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(author)) {
            edtBookAuthor.setError("Vui lòng nhập tác giả");
            edtBookAuthor.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(intro)) {
            edtBookIntro.setError("Vui lòng nhập mô tả sách");
            edtBookIntro.requestFocus();
            return;
        }

        String token = sessionManager.getToken();

        if (token == null || token.trim().isEmpty()) {
            Toast.makeText(this, "Bạn chưa đăng nhập", Toast.LENGTH_SHORT).show();
            return;
        }

        int available = availableChecked ? 1 : 0;
        int stock = availableChecked ? 1 : 0;

        String category = currentBook != null ? currentBook.getCategory() : "general";
        String imageUrl = currentBook != null ? currentBook.getImageUrl() : "";
        String fileUrl = currentBook != null ? currentBook.getFileUrl() : "";
        int pages = currentBook != null ? currentBook.getPages() : 0;

        BookRequest request = new BookRequest(
                title,
                author,
                category,
                imageUrl,
                intro,
                intro,
                fileUrl,
                pages,
                stock,
                available,
                available
        );

        if (isEditMode) {
            updateBook(token, request);
        } else {
            createBook(token, request);
        }
    }

    private void createBook(String token, BookRequest request) {
        setLoading(true);

        bookRepository.createBook(token, request).enqueue(new Callback<Book>() {
            @Override
            public void onResponse(@NonNull Call<Book> call,
                                   @NonNull Response<Book> response) {
                setLoading(false);

                if (response.isSuccessful()) {
                    Snackbar.make(btnSaveBook, "Thêm sách thành công", Snackbar.LENGTH_SHORT).show();
                    btnSaveBook.postDelayed(() -> finish(), 700);
                } else {
                    Toast.makeText(
                            AddEditBookActivity.this,
                            "Không thêm được sách. Code: " + response.code(),
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Book> call,
                                  @NonNull Throwable t) {
                setLoading(false);
                Toast.makeText(
                        AddEditBookActivity.this,
                        "Lỗi kết nối BE: " + t.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }

    private void updateBook(String token, BookRequest request) {
        setLoading(true);

        bookRepository.updateBook(token, bookId, request).enqueue(new Callback<Book>() {
            @Override
            public void onResponse(@NonNull Call<Book> call,
                                   @NonNull Response<Book> response) {
                setLoading(false);

                if (response.isSuccessful()) {
                    Snackbar.make(btnSaveBook, "Cập nhật sách thành công", Snackbar.LENGTH_SHORT).show();
                    btnSaveBook.postDelayed(() -> finish(), 700);
                } else {
                    Toast.makeText(
                            AddEditBookActivity.this,
                            "Không cập nhật được sách. Code: " + response.code(),
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Book> call,
                                  @NonNull Throwable t) {
                setLoading(false);
                Toast.makeText(
                        AddEditBookActivity.this,
                        "Lỗi kết nối BE: " + t.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }

    private void setLoading(boolean isLoading) {
        btnSaveBook.setEnabled(!isLoading);

        if (isLoading) {
            btnSaveBook.setText(isEditMode ? "Đang cập nhật..." : "Đang thêm...");
        } else {
            btnSaveBook.setText(isEditMode ? "Cập nhật sách" : "Thêm sách");
        }
    }
}