package com.qdo.votingapi.responses;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class CustomApiErrorResponse {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime timestamp;
    private int status;
    private List<String> errors;

    public CustomApiErrorResponse() {}

    public CustomApiErrorResponse(String error, int status, LocalDateTime timestamp) {
        this.timestamp = timestamp;
        this.status = status;
        this.errors = Arrays.asList(error);
    }

    public CustomApiErrorResponse(String error, int status) {
        this(error, status, LocalDateTime.now());
    }

    public CustomApiErrorResponse(List<String> errors, int status, LocalDateTime timestamp) {
        this.timestamp = timestamp;
        this.status = status;
        this.errors = errors;
    }

    public CustomApiErrorResponse(List<String> errors, int status) {
        this(errors, status, LocalDateTime.now());
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public void setError(String error) {
        this.errors = Arrays.asList(error);
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public List<String> getErrors() {
        return errors;
    }
}
