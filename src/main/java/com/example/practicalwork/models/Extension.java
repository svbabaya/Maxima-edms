package com.example.practicalwork.models;

import lombok.Getter;

@Getter
public enum Extension {
    DOCX("docx"),
    XLSX("xlsx"),
    PDF("pdf");
    private final String label;
    Extension(String label) {
        this.label = label;
    }
    public static Extension findByValue(String value) {
        for (Extension m : Extension.values()) {
            if (m.name().equalsIgnoreCase(value)) {
                return m;
            }
        }
        return null;
//        throw new DocumentInvalidFormatOfFileException();
    }
}
