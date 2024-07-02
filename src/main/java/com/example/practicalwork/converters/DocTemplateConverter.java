package com.example.practicalwork.converters;

import com.example.practicalwork.DTO.DocTemplateDTO;
import com.example.practicalwork.models.DocTemplate;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DocTemplateConverter {
    private ModelMapper modelMapper;

    public DocTemplateDTO convertToDto(DocTemplate entity) {
//        DocTemplateDTO dto = modelMapper.map(entity, DocTemplateDTO.class);
//        dto.setFields(entity.getFields().stream()
//                .map(el -> modelMapper.map(el, DocFieldDTO.class)).toList());
//        return dto;
        return modelMapper.map(entity, DocTemplateDTO.class);
    }

    public DocTemplate convertToEntity(DocTemplateDTO dto) {

        return modelMapper.map(dto, DocTemplate.class);
    }
}
