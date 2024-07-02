package com.example.practicalwork.models;

import lombok.Getter;

import java.io.Serializable;

@Getter
public enum DocTitle implements Serializable {
    CONTRACT("Договор"),
    AGREEMENT("Соглашение"),
    APPLICATION("Приложение"),
    ACT("Акт"),
    ACCOUNT("Счет"),
    REFERENCE("Справка");
    private final String label;
    DocTitle(String label) {
        this.label = label;
    }
    public static DocTitle findByValue(String value) {
        for (DocTitle type : DocTitle.values()) {
            if (type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
//        throw new DocTemplateUnknownTypeOfDocException();
        return null;
    }
}
