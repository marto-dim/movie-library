package com.telerikacademy.web.movielibrary.exception;

import java.time.LocalDateTime;
import java.util.List;

public record ApiError(
        int status,
        String error,
        List<String> messages,
        LocalDateTime timestamp
) {

    public ApiError(int status, String error, List<String> messages) {
        this(status, error, messages, LocalDateTime.now());
    }
}
