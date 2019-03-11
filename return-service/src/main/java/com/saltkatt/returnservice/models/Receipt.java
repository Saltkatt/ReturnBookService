package com.saltkatt.returnservice.models;

import java.util.Set;

public class Receipt {

    private final String TITLE = "RECEIPT";
    private String bookTitle;
    private Set<Author> authors;
    private String returnDate;
    private String message;

    public Receipt() {
    }

    public Receipt(String bookTitle, Set<Author> authors, String returnDate, String message) {
        this.bookTitle = bookTitle;
        this.authors = authors;
        this.returnDate = returnDate;
        this.message = message;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public Set<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
