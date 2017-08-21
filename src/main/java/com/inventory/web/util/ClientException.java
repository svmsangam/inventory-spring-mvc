package com.inventory.web.util;


public class ClientException extends Exception {

    private static final long serialVersionUID = 1L;
    private String message;

    public ClientException() {

    }

    public ClientException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
