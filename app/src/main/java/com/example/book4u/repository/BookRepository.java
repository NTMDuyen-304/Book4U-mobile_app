//package com.example.book4u.repository;
//
//import com.example.book4u.models.Book;
//import com.example.book4u.models.BookRequest;
//import com.example.book4u.models.MessageResponse;
//import com.example.book4u.network.ApiService;
//import com.example.book4u.network.RetrofitClient;
//import com.example.book4u.utils.Constants;
//
//import java.util.List;
//
//import retrofit2.Call;
//
//public class BookRepository {
//    private final ApiService apiService;
//
//    public BookRepository() {
//        apiService = RetrofitClient.getApiService();
//    }
//
//    private String bearer(String token) {
//        return Constants.BEARER_PREFIX + token;
//    }
//
//    public Call<List<Book>> getBooks(String token, String keyword, String category) {
//        return apiService.getBooks(bearer(token), keyword, category);
//    }
//
//    public Call<Book> getBookById(String token, String bookId) {
//        return apiService.getBookById(bearer(token), bookId);
//    }
//
//    public Call<Book> createBook(String token, BookRequest request) {
//        return apiService.createBook(bearer(token), request);
//    }
//
//    public Call<Book> updateBook(String token, String bookId, BookRequest request) {
//        return apiService.updateBook(bearer(token), bookId, request);
//    }
//
//    public Call<MessageResponse> deleteBook(String token, String bookId) {
//        return apiService.deleteBook(bearer(token), bookId);
//    }
//}
package com.example.book4u.repository;

import android.content.Context;

import com.example.book4u.local.AppDatabase;
import com.example.book4u.local.BookDao;
import com.example.book4u.models.Book;
import com.example.book4u.models.BookRequest;
import com.example.book4u.models.MessageResponse;
import com.example.book4u.network.ApiService;
import com.example.book4u.network.RetrofitClient;
import com.example.book4u.utils.Constants;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;

public class BookRepository {
    private final ApiService apiService;
    private BookDao bookDao;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public interface LocalBooksCallback {
        void onResult(List<Book> books);
    }

    public interface LocalBookCallback {
        void onResult(Book book);
    }

    public BookRepository() {
        apiService = RetrofitClient.getApiService();
    }

    public BookRepository(Context context) {
        apiService = RetrofitClient.getApiService();
        bookDao = AppDatabase.getInstance(context).bookDao();
    }

    private String bearer(String token) {
        return Constants.BEARER_PREFIX + token;
    }

    // ===== Remote API =====
    public Call<List<Book>> getBooks(String token, String keyword, String category) {
        return apiService.getBooks(bearer(token), keyword, category);
    }

    public Call<Book> getBookById(String token, String bookId) {
        return apiService.getBookById(bearer(token), bookId);
    }

    public Call<Book> createBook(String token, BookRequest request) {
        return apiService.createBook(bearer(token), request);
    }

    public Call<Book> updateBook(String token, String bookId, BookRequest request) {
        return apiService.updateBook(bearer(token), bookId, request);
    }

    public Call<MessageResponse> deleteBook(String token, String bookId) {
        return apiService.deleteBook(bearer(token), bookId);
    }

    // ===== Local Room =====
    public void insertLocal(Book book) {
        executorService.execute(() -> bookDao.insert(book));
    }

    public void updateLocal(Book book) {
        executorService.execute(() -> bookDao.update(book));
    }

    public void deleteLocal(Book book) {
        executorService.execute(() -> bookDao.delete(book));
    }

    public void toggleAvailabilityLocal(Book book) {
        executorService.execute(() -> {
            book.setAvailable(!book.isAvailable());
            bookDao.update(book);
        });
    }

    public void getAllLocalBooks(LocalBooksCallback callback) {
        executorService.execute(() -> callback.onResult(bookDao.getAllBooks()));
    }

    public void searchLocalBooks(String keyword, LocalBooksCallback callback) {
        executorService.execute(() -> callback.onResult(bookDao.searchBooks(keyword)));
    }

    public void getLocalBookById(int bookId, LocalBookCallback callback) {
        executorService.execute(() -> callback.onResult(bookDao.getBookById(bookId)));
    }

    public void seedBooksIfNeeded() {
        executorService.execute(() -> {
            if (bookDao.countBooks() == 0) {
                bookDao.insert(new Book("Clean Code", "Robert C. Martin",
                        "A practical guide to writing cleaner, more maintainable, and more readable code.", true));

                bookDao.insert(new Book("Atomic Habits", "James Clear",
                        "A book about building good habits, breaking bad ones, and improving yourself step by step.", false));

                bookDao.insert(new Book("Android Programming Basics", "Google Developers",
                        "An introduction to Android development concepts, UI design, and app building fundamentals.", true));

                bookDao.insert(new Book("The Pragmatic Programmer", "Andrew Hunt",
                        "A classic software engineering book that shares practical advice for becoming a better programmer.", true));

                bookDao.insert(new Book("Design Patterns", "Erich Gamma",
                        "An influential book introducing reusable object-oriented design patterns for software development.", false));
            }
        });
    }
}