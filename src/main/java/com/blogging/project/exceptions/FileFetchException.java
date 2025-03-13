package com.blogging.project.exceptions;

public class FileFetchException extends RuntimeException {
    public FileFetchException(String message) {
        super(message);
    }

    public FileFetchException(String message, Throwable cause) {
        super(message, cause);
    }
}
