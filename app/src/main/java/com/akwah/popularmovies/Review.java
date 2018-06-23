package com.akwah.popularmovies;

public class Review {
    private String content;
    private String author;

    public Review(String content, String author) {
        this.content = content;
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public String getAuthor() {
        return author;
    }
}
