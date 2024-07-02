package com.example.practicalwork.utils;

import com.example.practicalwork.models.DocFile;
import com.example.practicalwork.services.DocFileService;
import com.example.practicalwork.utils.file.FileOnDiskNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import java.io.File;

@Component
public class FileSizeCreator {
    DocFileService docFileService;
    public String create(DocFile docFile) {
        File file = new File(docFile.getStore() + docFile.getName());
        if (!file.exists() || !file.isFile()) {
            throw new FileOnDiskNotFoundException();
        }

        return file.length() / 1024 + " KB";

    }
}
