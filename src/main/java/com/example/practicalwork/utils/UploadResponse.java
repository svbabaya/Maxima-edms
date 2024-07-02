package com.example.practicalwork.utils;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class UploadResponse {
        private String message;
        private LocalDateTime timestamp;

        public UploadResponse() {

                timestamp = LocalDateTime.now();
        }

}

