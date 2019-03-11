package com.saltkatt.returnservice.controller;

public class Introduction {

    private String message;

    public Introduction() {
    }

    public Introduction(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
