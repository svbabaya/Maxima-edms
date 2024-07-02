package com.example.practicalwork.converters;

import com.example.practicalwork.DTO.DocFileDTO;
import com.example.practicalwork.models.DocFile;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DocFileConverter {

    private ModelMapper modelMapper;

    public DocFileDTO convertToDto(DocFile entity) {

        return modelMapper.map(entity, DocFileDTO.class);
    }

    public DocFile convertToEntity(DocFileDTO dto) {

        return modelMapper.map(dto, DocFile.class);
    }
}
