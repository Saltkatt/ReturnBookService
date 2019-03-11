package com.saltkatt.returnservice.models;

import java.util.Set;

public class Author {

    private long author_id;
    private String firstName;
    private String lastName;
    private Set<Book> books;

    public Author() {
    }

    public Author(long author_id, String firstName, String lastName, Set<Book> books) {
        this.author_id = author_id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.books = books;
    }

    public long getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(long author_id) {
        this.author_id = author_id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }
}
