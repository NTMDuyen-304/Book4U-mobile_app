package com.example.book4u.models;

public class BookRequest {

    private String title;
    private String author;
    private String category;
    private String img;
    private String description;
    private String intro;
    private String fileUrl;
    private int pages;
    private int stock;
    private int available;
    private int availableCopies;

    public BookRequest(String title,
                       String author,
                       String category,
                       String img,
                       String description,
                       String intro,
                       String fileUrl,
                       int pages,
                       int stock,
                       int available,
                       int availableCopies) {
        this.title = title;
        this.author = author;
        this.category = category;
        this.img = img;
        this.description = description;
        this.intro = intro;
        this.fileUrl = fileUrl;
        this.pages = pages;
        this.stock = stock;
        this.available = available;
        this.availableCopies = availableCopies;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getCategory() {
        return category;
    }

    public String getImg() {
        return img;
    }

    public String getDescription() {
        return description;
    }

    public String getIntro() {
        return intro;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public int getPages() {
        return pages;
    }

    public int getStock() {
        return stock;
    }

    public int getAvailable() {
        return available;
    }

    public int getAvailableCopies() {
        return availableCopies;
    }
}