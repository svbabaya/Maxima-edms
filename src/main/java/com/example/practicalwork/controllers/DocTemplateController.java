package com.example.practicalwork.controllers;

import com.example.practicalwork.DTO.DocTemplateDTO;
import com.example.practicalwork.converters.DocTemplateConverter;
import com.example.practicalwork.models.DocField;
import com.example.practicalwork.models.DocTemplate;
import com.example.practicalwork.models.DocTitle;
import com.example.practicalwork.services.DocFieldService;
import com.example.practicalwork.services.DocTemplateService;
import com.example.practicalwork.utils.*;
import com.example.practicalwork.utils.field.DocFieldNotFoundException;
import com.example.practicalwork.utils.template.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/api/template")
@AllArgsConstructor
@Tag(name = "DocTemplateController", description = "API for templates")
public class DocTemplateController {
    private final DocTemplateService docTemplateService;
    private final DocFieldService docFieldService;
    private final DocTemplateConverter docTemplateConverter;
    private final BindingResultHandler bindingResultHandler;
    private final TemplateCreator templateCreator;

    @GetMapping("/all")
    @Operation(summary = "Get templates", description = "Get all current and removed templates")
    public List<DocTemplateDTO> getAll() {

        List<DocTemplateDTO> dto = docTemplateService.findAll()
                .stream().map(docTemplateConverter::convertToDto)
                .toList();
        if (dto.isEmpty()) {
            throw new DocTemplateListIsEmptyException();
        }
        return dto;
    }

    @GetMapping("/current")
    @Operation(summary = "Get templates", description = "Get current templates only (removed = false)")
    public List<DocTemplateDTO> getCurrent() {
        List<DocTemplateDTO> dto = docTemplateService.findCurrent()
                .stream().map(docTemplateConverter::convertToDto)
                .toList();
        if (dto.isEmpty()) {
            throw new DocTemplateListIsEmptyException();
        }
        return dto;
    }

    @GetMapping("type/{type}")
    @Operation(summary = "Get templates", description = "Get one type current templates (contract, agreement, application, act, reference, account)")
    public List<DocTemplateDTO> getByType(@PathVariable("type") String type) {

        if (DocTitle.findByValue(type) == null) {
            throw new DocTemplateUnknownTypeOfDocException();
        }

        List<DocTemplate> list = docTemplateService.findCurrent()
                .stream().filter(el -> el.getDocTitle().name().equalsIgnoreCase(type))
                .toList();

        if (list.isEmpty()) {
            throw new DocTemplateListIsEmptyException();
        }

        return list.stream().map(docTemplateConverter::convertToDto).toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get template", description = "Get one template by id")
    public DocTemplateDTO getById(@PathVariable("id") Long id) {

        // Exception set in DocTemplateService
        return docTemplateConverter.convertToDto(docTemplateService.read(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete template", description = "Delete one template by id")
    public ResponseEntity<HttpStatus> deleteById(@PathVariable("id") Long id) {

        // Exception set in DocTemplateService
        docTemplateService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/recovery/{id}")
    @Operation(summary = "Recovery template", description = "Recovery one template by id")
    public ResponseEntity<HttpStatus> recovery(@PathVariable("id") Long id) {

        // Exception set in DocTemplateService
        docTemplateService.recovery(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    /*****
     * POST localhost:8080/api/template/new
     * RequestBody requires JSON
     *  {
     *    "title": "Template for simple contract with individual",
     *    "version": "V1.1",
     *    "type": "contract" // contract, reference, application, act, account, agreement
     *  }
     * Response OK = 200
     * Response BAD_REQUEST = 400
     *  {
     *    "message": "Incorrect type of template",
     *    "timestamp": "2023-04-06T13:25:30.788+00:00"
     *  }
     *  {
     *    "message": "version : ..., title : ...",
     *    "timestamp": "2023-04-06T13:25:30.788+00:00"
     *  }
     * */
    @PostMapping("/new")
    @Operation(summary = "Create template", description = "Create new template using data in json")
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid DocTemplateDTO dto,
                                             BindingResult bindingResult) {

        throwExceptionIfTypeIncorrect(dto);

        if (bindingResult.hasErrors()) {
            String str = bindingResultHandler.createErrorMessage(bindingResult);
            throw new DocTemplateNotCreatedException(str);
        }

        docTemplateService.write(docTemplateConverter.convertToEntity(dto));
        return ResponseEntity.ok(HttpStatus.OK);

    }

    @PostMapping(value = "/new/from/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Create template", description = "Upload file and create new template with one field")
    public ResponseEntity<UploadResponse> createFromFile(@RequestParam("file") MultipartFile file,
                                                     @RequestParam("type") String type) {

        DocTitle docTitle = DocTitle.findByValue(type);
        if (docTitle == null) {
            throw new DocTemplateUnknownTypeOfDocException();
        }

        StringBuilder info = templateCreator.createFromFile(file, type);

        UploadResponse response = new UploadResponse();
        response.setMessage(info.toString());
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    /*****
     * PUT localhost:8080/api/template/update
     * RequestBody requires JSON
     *  {
     *    "id": 6,
     *    "title": "Template for simple contract with individual",
     *    "version": "V1.1",
     *    "type": "contract" // contract, reference, application, act, account, agreement
     *  }
     * Response OK = 200
     * Response BAD_REQUEST = 400
     *  {
     *    "message": "Incorrect type of template",
     *    "timestamp": "2023-04-06T13:25:30.788+00:00"
     *  }
     *  {
     *    "message": "Template not found",
     *    "timestamp": "2023-04-06T13:25:30.788+00:00"
     *  }
     *  {
     *    "message": "version : ..., title : ...",
     *    "timestamp": "2023-04-06T13:25:30.788+00:00"
     *  }
     * */
    @PutMapping("/update")
    @Operation(summary = "Update template", description = "Update current template by id in json")
    public ResponseEntity<HttpStatus> update(@RequestBody @Valid DocTemplateDTO dto,
                                             BindingResult bindingResult) {

        throwExceptionIfTypeIncorrect(dto);

        if (bindingResult.hasErrors()) {
            String str = bindingResultHandler.createErrorMessage(bindingResult);
            throw new DocTemplateNotCreatedException(str);
        }

        docTemplateService.update(docTemplateConverter.convertToEntity(dto));
        return ResponseEntity.ok(HttpStatus.OK);

    }

    @PostMapping("{templateId}/attach/field/{fieldId}")
    @Operation(summary = "Attach field", description = "Attach existing field to template")
    public ResponseEntity<HttpStatus> attachField(@PathVariable("templateId") Long templateId,
                                                  @PathVariable("fieldId") Long fieldId) {

        // Exception set in DocTemplateService, DocFieldService
        DocField attachingField = docFieldService.read(fieldId);
        DocTemplate template = docTemplateService.read(templateId);
        template.getFields().add(attachingField);
        docTemplateService.write(template);
        return ResponseEntity.ok(HttpStatus.OK);

    }

    @PostMapping("{templateId}/detach/field/{fieldId}")
    @Operation(summary = "Detach field", description = "Detach field from template")
    public ResponseEntity<HttpStatus> detachField(@PathVariable("templateId") Long templateId,
                                                  @PathVariable("fieldId") Long fieldId) {

        // Exception set in DocTemplateService, DocFieldService
        DocTemplate template = docTemplateService.read(templateId);
        template.getFields().remove(template.getFields().stream()
                .filter(x -> x.getId().equals(fieldId)).findFirst()
                .orElseThrow(DocFieldNotFoundException::new));
        docTemplateService.write(template);
        return ResponseEntity.ok(HttpStatus.OK);

    }

    void throwExceptionIfTypeIncorrect(DocTemplateDTO dto) {
        DocTitle docTitle = DocTitle.findByValue(dto.getDocTitle());
        if (docTitle == null) {
            throw new DocTemplateUnknownTypeOfDocException();
        }
        dto.setDocTitle(docTitle.name().toUpperCase());
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handlerException(DocTemplateListIsEmptyException e) {
        e.printStackTrace();
        ErrorResponse response = new ErrorResponse();
        response.setMessage("No templates for this request");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handlerException(DocTemplateNotFoundException e) {
        e.printStackTrace();
        ErrorResponse response = new ErrorResponse();
        response.setMessage("Template not found");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handlerException(DocTemplateNotDeletedException e) {
        e.printStackTrace();
        ErrorResponse response = new ErrorResponse();
        response.setMessage("Template doesn't need in recovery");
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handlerException(DocTemplateIsDeletedException e) {
        e.printStackTrace();
        ErrorResponse response = new ErrorResponse();
        response.setMessage("Template is deleted already");
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handlerException(DocTemplateNotCreatedException e) {
        e.printStackTrace();
        ErrorResponse response = new ErrorResponse();
        response.setMessage(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handlerException(DocTemplateUnknownTypeOfDocException e) {
        e.printStackTrace();
        ErrorResponse response = new ErrorResponse();
        response.setMessage("Incorrect type of template");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // @ExceptionHandler
    // ...

}
