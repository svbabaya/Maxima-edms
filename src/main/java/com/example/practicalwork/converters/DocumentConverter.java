package com.example.practicalwork.converters;

import com.example.practicalwork.DTO.DocumentDTO;
import com.example.practicalwork.models.DocField;
import com.example.practicalwork.models.DocTemplate;
import com.example.practicalwork.models.Document;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@AllArgsConstructor
public class DocumentConverter {
    private ModelMapper modelMapper;

    public DocumentDTO convertToDto(Document entity) {
//        DocumentDTO dto = modelMapper.map(entity, DocumentDTO.class);
//        dto.setFields(entity.getFields().stream()
//                .map(el -> modelMapper.map(el, DocFieldDTO.class)).toList());
//        return dto;
        return modelMapper.map(entity, DocumentDTO.class);
    }

    public Document convertToEntity(DocumentDTO dto) {

        return modelMapper.map(dto, Document.class);
    }

    public Document createDocFromTemplate(DocTemplate template) {
        Document creatingDoc = new Document();
        ArrayList<DocField> contractFields = new ArrayList<>();
        for (DocField f : template.getFields()) {
            try {
                DocField tmpField = (DocField) f.clone();
                tmpField.setId(null);
                contractFields.add(tmpField);
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
        creatingDoc.setFields(contractFields);
        creatingDoc.setDocTitle(template.getDocTitle());
        return creatingDoc;
    }
}

