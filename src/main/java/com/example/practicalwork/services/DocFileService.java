package com.example.practicalwork.services;

import com.example.practicalwork.models.DocFile;
import com.example.practicalwork.repositories.DocFileRepository;
import com.example.practicalwork.utils.file.DocFileNotDeletedException;
import com.example.practicalwork.utils.file.DocFileNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class DocFileService {
    private final DocFileRepository docFileRepository;

    public List<DocFile> findAll() {

        return docFileRepository.findAll();
    }

    public List<DocFile> findCurrent() {
        return docFileRepository.findAll()
                .stream().filter(el -> !el.isRemoved())
                .toList();
    }

    public DocFile read(Long id) {
        Optional<DocFile> foundDocument = docFileRepository.findById(id);
        return foundDocument.orElseThrow(DocFileNotFoundException::new);
    }

    @Transactional
    public void write(DocFile docFile) {

        docFileRepository.save(docFile);
    }

    @Transactional
    public void delete(Long id) {
        DocFile docFile = read(id);
        docFile.setRemoved(true);
        docFileRepository.save(docFile);
    }

    @Transactional
    public void recovery(Long id) {
        DocFile docFile = read(id);
        if (!docFile.isRemoved()) {
            throw new DocFileNotDeletedException();
        } else {
            docFile.setRemoved(false);
            docFileRepository.save(docFile);
        }
    }

    @Transactional
    public void update(DocFile entity) {

        // TODO Change to ModelMapper

        // TODO Full update with converting entity to file ang deleting previous version of file

        DocFile myDocFile = read(entity.getId());
        myDocFile.setName(entity.getName());
        myDocFile.setDescription(entity.getDescription());

        docFileRepository.save(myDocFile);

    }

}
