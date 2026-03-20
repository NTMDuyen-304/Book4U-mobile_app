package com.example.book4u.models;

public class BookRequest {
    private String title;
    private String author;
    private String category;
    private String coverImage;
    private String description;
    private String intro;
    private String fileUrl;
    private int pages;
    private int stock;
    private int availableCopies;
    private int available;

    public BookRequest(String title, String author, String category,
                       String coverImage, String description, String intro,
                       String fileUrl, int pages, int stock,
                       int availableCopies, int available) {
        this.title = title;
        this.author = author;
        this.category = category;
        this.coverImage = coverImage;
        this.description = description;
        this.intro = intro;
        this.fileUrl = fileUrl;
        this.pages = pages;
        this.stock = stock;
        this.availableCopies = availableCopies;
        this.available = available;
    }
}