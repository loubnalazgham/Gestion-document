package com.example.api_doc.Exceptions;

public class DocumentUpdateFailException extends Exception{
    private static final long serialVersionUID = 1L;

    public DocumentUpdateFailException(String msg) {
        super(msg);
    }
}
