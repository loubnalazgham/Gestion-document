package com.example.api_doc.Exceptions;

public class DocumentNotFoundException extends Exception{

    private static final long serialVersionUID = 1L;
    public DocumentNotFoundException(String msg) {
        super(msg);
    }
}
