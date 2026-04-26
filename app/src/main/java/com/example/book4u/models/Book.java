package com.example.book4u.models;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "books")
public class Book {

    // Dùng cho Room local database cũ
    @PrimaryKey(autoGenerate = true)
    private int localId;

    // Dùng cho BE MongoDB
    @SerializedName(value = "id", alternate = {"_id"})
    private String id;

    private String title;
    private String author;
    private String category;

    private String intro;
    private String description;

    private String coverImage;
    private String img;
    private String image;
    private String fileUrl;

    private int pages;
    private int stock;
    private int availableCopies;

    // BE trả available dạng số, ví dụ: 5
    private int available;

    public Book() {
    }

    // Constructor này giữ lại để code local cũ chưa bị lỗi
    @Ignore
    public Book(String title, String author, String intro, boolean availableBool) {
        this.title = title;
        this.author = author;
        this.intro = intro;
        this.description = intro;
        this.available = availableBool ? 1 : 0;
        this.availableCopies = availableBool ? 1 : 0;
        this.stock = availableBool ? 1 : 0;
    }

    // ===== ID cho Room local =====
    public int getLocalId() {
        return localId;
    }

    public void setLocalId(int localId) {
        this.localId = localId;
    }

    // ===== ID từ BE =====
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // ===== Basic info =====
    public String getTitle() {
        return title != null ? title : "";
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author != null ? author : "";
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category != null ? category : "";
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getIntro() {
        if (intro != null && !intro.isEmpty()) {
            return intro;
        }

        if (description != null && !description.isEmpty()) {
            return description;
        }

        return "";
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getDescription() {
        if (description != null && !description.isEmpty()) {
            return description;
        }

        if (intro != null && !intro.isEmpty()) {
            return intro;
        }

        return "";
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // ===== Images =====
    public String getCoverImage() {
        return coverImage != null ? coverImage : "";
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public String getImg() {
        return img != null ? img : "";
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getImage() {
        return image != null ? image : "";
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImageUrl() {
        if (img != null && !img.isEmpty()) {
            return img;
        }

        if (image != null && !image.isEmpty()) {
            return image;
        }

        if (coverImage != null && !coverImage.isEmpty()) {
            return coverImage;
        }

        return "";
    }

    // ===== File / pages =====
    public String getFileUrl() {
        return fileUrl != null ? fileUrl : "";
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    // ===== Stock =====
    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getAvailableCopies() {
        return availableCopies;
    }

    public void setAvailableCopies(int availableCopies) {
        this.availableCopies = availableCopies;
    }

    public int getAvailableCount() {
        return available;
    }

    public int getAvailable() {
        return available;
    }

    public void setAvailableCount(int available) {
        this.available = available;
    }

    // Method này giữ lại cho code cũ đang gọi book.isAvailable()
    public boolean isAvailable() {
        return available > 0;
    }

    // Method này giữ lại cho code cũ đang gọi book.setAvailable(true/false)
    public void setAvailable(boolean availableBool) {
        this.available = availableBool ? 1 : 0;
        this.availableCopies = availableBool ? 1 : 0;

        if (this.stock <= 0) {
            this.stock = availableBool ? 1 : 0;
        }
    }

    // Method này dùng khi BE trả hoặc mình muốn set số lượng
    public void setAvailable(int available) {
        this.available = available;
    }
}