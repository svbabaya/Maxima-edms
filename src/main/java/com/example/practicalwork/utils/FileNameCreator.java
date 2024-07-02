package com.example.practicalwork.utils;

import com.example.practicalwork.models.Document;
import org.springframework.stereotype.Component;

@Component
public class FileNameCreator {

    public String create(Document doc) {

        return doc.getDocTitle().getLabel() +
                "-" +
                doc.getNumber() +
                "-" +
                doc.getCreatedAt().toString().substring(0, 10) + "." +
                doc.getFile().getExtension().toString().toLowerCase();

    }
}
