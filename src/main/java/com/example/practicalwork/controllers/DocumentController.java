package com.example.practicalwork.controllers;

import com.example.practicalwork.DTO.DocumentDTO;
import com.example.practicalwork.converters.*;
import com.example.practicalwork.models.*;
import com.example.practicalwork.services.DocFieldService;
import com.example.practicalwork.services.DocTemplateService;
import com.example.practicalwork.services.DocumentService;
import com.example.practicalwork.utils.*;
import com.example.practicalwork.utils.document.*;
import com.example.practicalwork.utils.field.DocFieldNotFoundException;
import com.example.practicalwork.utils.template.DocTemplateNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/document")
@AllArgsConstructor
@Tag(name = "DocumentController", description = "API for documents")
public class DocumentController {
    private final DocumentService docService;
    private final DocFieldService docFieldService;
    private final DocTemplateService templateService;
    private final DocumentConverter docConverter;
    private final BindingResultHandler bindingResultHandler;

    @GetMapping("/all")
    @Operation(summary = "Get documents", description = "Get all current and removed documents")
    public List<DocumentDTO> getAll() {
        List<DocumentDTO> dto = docService.findAll()
                .stream().map(docConverter::convertToDto)
                .toList();
        if (dto.isEmpty()) {
            throw new DocListIsEmptyException();
        }
        return dto;
    }

    @GetMapping("/current")
    @Operation(summary = "Get documents", description = "Get current documents only (removed = false)")
    public List<DocumentDTO> getCurrent() {
        List<DocumentDTO> dto = docService.findCurrent()
                .stream().map(docConverter::convertToDto)
                .toList();
        if (dto.isEmpty()) {
            throw new DocListIsEmptyException();
        }
        return dto;
    }

    @GetMapping("/filter")
    @Operation(summary = "Get documents", description = "Get documents filtered by type, number, date(YYYY-MM-DD) of creation, id of contractor and deletion status")
    public List<DocumentDTO> getFiltered(@RequestParam(value = "type", required = false) String enumType,
                                         @RequestParam(value = "number", required = false) String number,
                                         @RequestParam(value = "date", required = false) String date,
//                                         @RequestParam(value = "contractor", required = false) String contractorId,
                                         @RequestParam(value = "current", required = false, defaultValue = "true")
                                         String current) {

        List<DocumentDTO> list = docService.findAll().stream()
                .filter(el -> {
                    if (Boolean.parseBoolean(current)) {
                        return !el.isRemoved();
                    } else return true;
                })

                .filter(el -> {
                    if (enumType != null) {
                        return el.getDocTitle().name().equalsIgnoreCase(enumType);
                    } else return true;
                })

//                .filter(el -> {
//                    if (contractorId != null) {
//                        return el.getContractors().stream().anyMatch(c -> c.getId().equals(Long.parseLong(contractorId)));
//                    } else return true;
//                })

                .filter(el -> {
                    if (number != null) {
                        return el.getNumber().contains(number);
                    } else return true;
                })

                .filter(el -> {
                    if (date != null) {
                        return date.equals(el.getCreatedAt().toString().substring(0, 10));
                    } else return true;
                })

                .map(docConverter::convertToDto)
                .toList();

        if (list.isEmpty()) {
            throw new DocListIsEmptyException();
        }
        return list;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get document", description = "Get one document by id")
    public DocumentDTO getById(@PathVariable("id") Long id) {

        // Exception set in DocumentService
        return docConverter.convertToDto(docService.read(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete document", description = "Delete one document by id")
    public ResponseEntity<HttpStatus> deleteById(@PathVariable("id") Long id) {

        // Exception set in DocumentService
        docService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/recovery/{id}")
    @Operation(summary = "Recovery document", description = "Recovery one document by id")
    public ResponseEntity<HttpStatus> recovery(@PathVariable("id") Long id) {

        // Exception set in DocumentService
        docService.recovery(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("{docId}/attach/field/{fieldId}")
    @Operation(summary = "Attach field", description = "Attach existing field to document")
    public ResponseEntity<HttpStatus> attachField(@PathVariable("docId") Long templateId,
                                                  @PathVariable("fieldId") Long fieldId) {

        // Exception set in DocumentController, DocFieldController
        DocField attachingField = docFieldService.read(fieldId);
        Document doc = docService.read(templateId);
        doc.getFields().add(attachingField);
        docService.write(doc);
        return ResponseEntity.ok(HttpStatus.OK);

    }

    @PostMapping("{docId}/detach/field/{fieldId}")
    @Operation(summary = "Detach field", description = "Detach field from document")
    public ResponseEntity<HttpStatus> detachField(@PathVariable("docId") Long templateId,
                                                  @PathVariable("fieldId") Long fieldId) {

        // Exception set in DocumentController, DocFieldController
        Document doc = docService.read(templateId);
        doc.getFields().remove(doc.getFields().stream()
                .filter(x -> x.getId().equals(fieldId)).findFirst()
                .orElseThrow(DocFieldNotFoundException::new));
        docService.write(doc);
        return ResponseEntity.ok(HttpStatus.OK);

    }

    @PostMapping("{docId}/attach/related/{docAttachId}")
    @Operation(summary = "Attach related", description = "Attach related document to document")
    public ResponseEntity<HttpStatus> attachRelated(@PathVariable("docId") Long docId,
                                                  @PathVariable("docAttachId") Long docAttachId) {

        // Check equality docId and docAttachId
        if (docId.equals(docAttachId)) {
            throw new DocRelatedIdEqualDocIdException();
        }

        // Exception set in DocumentController
        Document baseDoc = docService.read(docId);
        Document attachDoc = docService.read(docAttachId);

        // Check existence of relation
        if (baseDoc.getDocRelatedList().stream().anyMatch(el -> el.getRelatedId()
                .equals(attachDoc.getId()))) {
            throw new DocRelationExistsAlreadyException();
        }

        // TODO check document inheritance rules
//        throw  new DocViolationOfInheritanceRulesException();

        DocRelated docRelated = new DocRelated();
        docRelated.setRelatedId(docAttachId);
        baseDoc.getDocRelatedList().add(docRelated);

        docService.write(baseDoc);
        return ResponseEntity.ok(HttpStatus.OK);

    }

    @PostMapping("{docId}/detach/related/{docDetachId}")
    @Operation(summary = "Detach related", description = "Detach related document from document")
    public ResponseEntity<HttpStatus> detachRelated(@PathVariable("docId") Long docId,
                                                    @PathVariable("docDetachId") Long docDetachId) {

        // Exception set in DocumentController
        Document baseDoc = docService.read(docId);

        DocRelated docRelated = baseDoc.getDocRelatedList().stream()
                .filter(el -> el.getRelatedId().equals(docDetachId)).findFirst()
                .orElseThrow(DocNoSuchRelationInDocumentException::new);

        baseDoc.getDocRelatedList().remove(docRelated);

        docService.write(baseDoc);
        return ResponseEntity.ok(HttpStatus.OK);

    }

    @PostMapping("/new/from/template/{id}")
    @Operation(summary = "Create document", description = "Create new document from template by id")
    public ResponseEntity<HttpStatus> createFromTemplate(@PathVariable("id") Long idTemplate) {

        // Exception set in DocTemplateService
        DocTemplate template = templateService.read(idTemplate);

        docService.write(docConverter.createDocFromTemplate(template));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    /*****
     * PUT localhost:8080/api/document/update
     * RequestBody requires JSON
     *  {
     *    "id": 6,
     *    "number": "01105",
     *    "type": "contract" // contract, reference, application, act, account, agreement
     *  }
     * Response OK = 200
     * Response BAD_REQUEST = 400
     *  {
     *    "message": "Incorrect type of document",
     *    "timestamp": "2023-04-06T13:25:30.788+00:00"
     *  }
     *  {
     *    "message": "Document not found",
     *    "timestamp": "2023-04-06T13:25:30.788+00:00"
     *  }
     *  {
     *    "message": "number : ...",
     *    "timestamp": "2023-04-06T13:25:30.788+00:00"
     *  }
     * */
    @PutMapping("/update")
    @Operation(summary = "Update document", description = "Update current document by id in json")
    public ResponseEntity<HttpStatus> update(@RequestBody @Valid DocumentDTO dto,
                                             BindingResult bindingResult) {
        throwExceptionIfTypeIncorrect(dto);

        if (bindingResult.hasErrors()) {
            String str = bindingResultHandler.createErrorMessage(bindingResult);
            throw new DocNotCreatedException(str);
        }

        docService.update(docConverter.convertToEntity(dto));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    void throwExceptionIfTypeIncorrect(DocumentDTO dto) {
        DocTitle docTitle = DocTitle.findByValue(dto.getDocTitle());
        if (docTitle == null) {
            throw new DocInvalidTypeOfDocException();
        } dto.setDocTitle(docTitle.name().toUpperCase());
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handlerException(DocListIsEmptyException e) {
        e.printStackTrace();
        ErrorResponse response = new ErrorResponse();
        response.setMessage("No documents for this request");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handlerException(DocNotFoundException e) {
        e.printStackTrace();
        ErrorResponse response = new ErrorResponse();
        response.setMessage("Document not found");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handlerException(DocNotDeletedException e) {
        e.printStackTrace();
        ErrorResponse response = new ErrorResponse();
        response.setMessage("Document doesn't need in recovery");
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handlerException(DocIsDeletedException e) {
        e.printStackTrace();
        ErrorResponse response = new ErrorResponse();
        response.setMessage("Document is deleted already");
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    // Duplicated from DocTemplateController
    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handlerException(DocTemplateNotFoundException e) {
        e.printStackTrace();
        ErrorResponse response = new ErrorResponse();
        response.setMessage("Template not found");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handlerException(DocInvalidTypeOfDocException e) {
        e.printStackTrace();
        ErrorResponse response = new ErrorResponse();
        response.setMessage("Incorrect type of document");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handlerException(DocNotCreatedException e) {
        e.printStackTrace();
        ErrorResponse response = new ErrorResponse();
        response.setMessage(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handlerException(DocRelatedIdEqualDocIdException e) {
        e.printStackTrace();
        ErrorResponse response = new ErrorResponse();
        response.setMessage("Document IDs must not match");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handlerException(DocViolationOfInheritanceRulesException e) {
        e.printStackTrace();
        ErrorResponse response = new ErrorResponse();
        response.setMessage("Violation of inheritance rules");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handlerException(DocNoSuchRelationInDocumentException e) {
        e.printStackTrace();
        ErrorResponse response = new ErrorResponse();
        response.setMessage("There is no related document with such ID");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handlerException(DocRelationExistsAlreadyException e) {
        e.printStackTrace();
        ErrorResponse response = new ErrorResponse();
        response.setMessage("This relation already exists");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

//    @ExceptionHandler
//    public ResponseEntity<ErrorResponse> handlerException(DocInvalidFormatOfFileException e) {
//        e.printStackTrace();
//        ErrorResponse response = new ErrorResponse();
//        response.setMessage("Invalid format of file. Allowed: /docx, /xlsx, /pdf");
//        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//    }

    // @ExceptionHandler
    // ...

}
