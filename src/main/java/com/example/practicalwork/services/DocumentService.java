package com.example.practicalwork.services;

import com.example.practicalwork.models.Document;
import com.example.practicalwork.repositories.DocRepository;
import com.example.practicalwork.utils.document.DocIsDeletedException;
import com.example.practicalwork.utils.document.DocNotFoundException;
import com.example.practicalwork.utils.document.DocNotDeletedException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class DocumentService {
    private final DocRepository docRepository;
//    private final DocumentDTO documentDTO;

    public List<Document> findAll() {

        return docRepository.findAll();
    }

    public List<Document> findCurrent() {

        return docRepository.findAll()
                .stream().filter(el -> !el.isRemoved())
                .toList();
    }

    public Document read(Long id) {
        Optional<Document> foundDocument = docRepository.findById(id);
        return foundDocument.orElseThrow(DocNotFoundException::new);
    }

    @Transactional
    public void write(Document document) {

        docRepository.save(document);
    }

    @Transactional
    public void delete(Long id) {
        Document doc = read(id);
        if (doc.isRemoved()) {
            throw new DocIsDeletedException();
        } else {
            doc.setRemoved(true);
            docRepository.save(doc);
        }
    }

    @Transactional
    public void recovery(Long id) {
        Document doc = read(id);
        if (!doc.isRemoved()) {
            throw new DocNotDeletedException();
        } else {
            doc.setRemoved(false);
            docRepository.save(doc);
        }
    }

    @Transactional
    public void update(Document entity) {

        // TODO Change to ModelMapper

        Document myDoc = read(entity.getId());
        myDoc.setNumber(entity.getNumber());
        myDoc.setDocTitle(entity.getDocTitle());

        docRepository.save(myDoc);

    }

}
