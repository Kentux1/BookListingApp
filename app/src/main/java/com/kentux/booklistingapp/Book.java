package com.kentux.booklistingapp;

public class Book {
    private String mTitle;
    private String mAuthor;
    private String mRating;
    private String mBookUrl;

    public Book(String title, String author, String rating, String bookUrl) {
        mTitle = title;
        mAuthor = author;
        mRating = rating;
        mBookUrl = bookUrl;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getRating() {
        return mRating;
    }

    public String getBookUrl() {
        return mBookUrl;
    }
}
