package com.example.book4u.models;

public class Book {
    private String title;
    private String author;
    private String intro;
    private boolean available;

    public Book(String title, String author, String intro, boolean available) {
        this.title = title;
        this.author = author;
        this.intro = intro;
        this.available = available;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getIntro() {
        return intro;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}