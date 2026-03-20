package com.example.book4u.models;

import com.google.gson.annotations.SerializedName;

public class Book {
    @SerializedName(value = "id", alternate = {"_id"})
    private String id;

    private String title;
    private String author;
    private String category;
    private String coverImage;
    private String img;
    private String image;
    private String description;
    private String intro;
    private String fileUrl;
    private int pages;
    private int availableCopies;
    private int stock;
    private int available;

    public Book() {
    }

    public String getId() {
        return id;
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

    public String getCoverImage() {
        return coverImage;
    }

    public String getImg() {
        return img;
    }

    public String getImage() {
        return image;
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

    public int getAvailableCopies() {
        return availableCopies;
    }

    public int getStock() {
        return stock;
    }

    public int getAvailable() {
        return available;
    }

    public String getDisplayImage() {
        if (img != null && !img.isEmpty()) return img;
        if (image != null && !image.isEmpty()) return image;
        if (coverImage != null && !coverImage.isEmpty()) return coverImage;
        return "";
    }
}