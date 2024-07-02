package com.example.practicalwork.services;

import com.example.practicalwork.models.DocTemplate;
import com.example.practicalwork.repositories.DocTemplateRepository;
import com.example.practicalwork.utils.document.DocIsDeletedException;
import com.example.practicalwork.utils.template.DocTemplateIsDeletedException;
import com.example.practicalwork.utils.template.DocTemplateNotDeletedException;
import com.example.practicalwork.utils.template.DocTemplateNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class DocTemplateService {

    private final DocTemplateRepository docTemplateRepository;

    public List<DocTemplate> findAll() {

        return docTemplateRepository.findAll();
    }

    public List<DocTemplate> findCurrent() {
        return docTemplateRepository.findAll()
                .stream().filter(el -> !el.isRemoved())
                .toList();
    }

    public DocTemplate read(Long id) {
        Optional<DocTemplate> foundDocument = docTemplateRepository.findById(id);
        return foundDocument.orElseThrow(DocTemplateNotFoundException::new);
    }

    @Transactional
    public void write(DocTemplate docTemplate) {

        docTemplateRepository.save(docTemplate);
    }

    @Transactional
    public void delete(Long id) {
        DocTemplate docTemplate = read(id);
        if (docTemplate.isRemoved()) {
            throw new DocTemplateIsDeletedException();
        } else {
            docTemplate.setRemoved(true);
            docTemplateRepository.save(docTemplate);
        }
    }

    @Transactional
    public void recovery(Long id) {
        DocTemplate docTemplate = read(id);
        if (!docTemplate.isRemoved()) {
            throw new DocTemplateNotDeletedException();
        } else {
            docTemplate.setRemoved(false);
            docTemplateRepository.save(docTemplate);
        }
    }

    @Transactional
    public void update(DocTemplate entity) {

        // TODO Change to ModelMapper

        DocTemplate myDocTemplate = read(entity.getId());
        myDocTemplate.setTitle(entity.getTitle());
        myDocTemplate.setVersion(entity.getVersion());
        myDocTemplate.setDocTitle(entity.getDocTitle());

        docTemplateRepository.save(myDocTemplate);

    }

}
