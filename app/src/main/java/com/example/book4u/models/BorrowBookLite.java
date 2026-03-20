package com.example.book4u.models;

import com.google.gson.annotations.SerializedName;

public class BorrowBookLite {
    @SerializedName(value = "id", alternate = {"_id"})
    private String id;

    private String title;
    private String author;
    private String category;
    private String coverImage;
    private String img;
    private String image;

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

    public String getDisplayImage() {
        if (img != null && !img.isEmpty()) return img;
        if (image != null && !image.isEmpty()) return image;
        if (coverImage != null && !coverImage.isEmpty()) return coverImage;
        return "";
    }
}