package com.example.practicalwork.utils;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Setter
@Getter
@Hidden
public class FileUriResponse {

    private String uri;
    private LocalDateTime timestamp;

    public FileUriResponse(String uri) {
        this.uri = uri;
        timestamp = LocalDateTime.now();
    }
}
