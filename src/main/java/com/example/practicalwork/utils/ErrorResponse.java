package com.example.practicalwork.utils;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class ErrorResponse {
        private String message;
        private LocalDateTime timestamp;

        public ErrorResponse() {

                timestamp = LocalDateTime.now();
        }

}

