package com.example.api_doc.Exceptions;

public class DocumentNotDeletedException extends Exception{
    private static final long serialVersionUID = 1L;
    public DocumentNotDeletedException(String msg) {
        super(msg);
    }
}
