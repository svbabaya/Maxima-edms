package com.example.practicalwork.utils;

import com.example.practicalwork.models.DocField;
import com.example.practicalwork.models.DocTemplate;
import com.example.practicalwork.services.DocTemplateService;
import lombok.AllArgsConstructor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component
@AllArgsConstructor
public class TemplateCreator {

    private DocTemplateService docTemplateService;

    public StringBuilder createFromFile(MultipartFile file, String type) {

        StringBuilder info = new StringBuilder();

        try (InputStream is = file.getInputStream()) {

            info.append("Uploaded file : ")
                    .append(file.getOriginalFilename())
                    .append(", Size : ")
                    .append(is.available()).append(" Bytes");

            XWPFDocument docxFile = new XWPFDocument(OPCPackage.open(is));
            List<XWPFParagraph> paragraphs = docxFile.getParagraphs();
            StringBuilder content = new StringBuilder();
            for (XWPFParagraph p : paragraphs) {
                content.append(p.getText());
            }

            // Create field and template from content
            DocField field = new DocField();
            DocTemplate template = new DocTemplate();
            field.setName("Uploaded from file");
            field.setType(type);
            field.setPlaceholder("Divide this field and set name, type, placeholder");
            field.setDefaultValue(content.toString());

            List<DocField> fields = List.of(field);
            template.setFields(fields);
            docTemplateService.write(template);

        } catch (IOException | InvalidFormatException ex) {
            throw new RuntimeException(ex);
        }

        return info;
    }
}
