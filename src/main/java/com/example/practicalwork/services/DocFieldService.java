package com.example.practicalwork.services;

import com.example.practicalwork.models.DocField;
import com.example.practicalwork.repositories.DocFieldRepository;
import com.example.practicalwork.utils.field.DocFieldIsDeletedException;
import com.example.practicalwork.utils.field.DocFieldNotDeletedException;
import com.example.practicalwork.utils.field.DocFieldNotFoundException;
import com.example.practicalwork.utils.template.DocTemplateIsDeletedException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class DocFieldService {

    private final DocFieldRepository docFieldRepository;
//    private final DocFieldDTO docFieldDTO;

    public List<DocField> findAll() {

        return docFieldRepository.findAll();
    }

    public List<DocField> findCurrent() {
        return docFieldRepository.findAll()
                .stream().filter(el -> !el.isRemoved())
                .toList();
    }

    public DocField findFieldByName(String nameOfField) {
        return docFieldRepository.findDocFieldByName(nameOfField);
    }

    public DocField read(Long id) {
        Optional<DocField> foundField = docFieldRepository.findById(id);
        return foundField.orElseThrow(DocFieldNotFoundException::new);
    }

    @Transactional
    public void write(DocField docField) {

        docFieldRepository.save(docField);
    }

    @Transactional
    public void delete(Long id) {
        DocField docField = read(id);
        if (docField.isRemoved()) {
            throw new DocFieldIsDeletedException();
        } else {
            docField.setRemoved(true);
            docFieldRepository.save(docField);
        }
    }

    @Transactional
    public void recovery(Long id) {
        DocField docField = read(id);
        if (!docField.isRemoved()) {
            throw new DocFieldNotDeletedException();
        } else {
        docField.setRemoved(false);
        docFieldRepository.save(docField);
        }
    }

    @Transactional
    public void update(DocField entity) {

        // TODO Change to ModelMapper

        DocField myDocField = read(entity.getId());
        myDocField.setName(entity.getName());
        myDocField.setType(entity.getType());
        myDocField.setPlaceholder(entity.getPlaceholder());
        myDocField.setDefaultValue(entity.getDefaultValue());

        docFieldRepository.save(myDocField);

    }

}
