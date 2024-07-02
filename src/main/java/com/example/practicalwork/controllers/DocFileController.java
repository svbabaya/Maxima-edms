package com.example.practicalwork.controllers;

import com.example.practicalwork.DTO.DocFileDTO;
import com.example.practicalwork.converters.*;
import com.example.practicalwork.converters.fileconverters.Distributor;
import com.example.practicalwork.models.DocFile;
import com.example.practicalwork.models.Document;
import com.example.practicalwork.services.DocFileService;
import com.example.practicalwork.services.DocumentService;
import com.example.practicalwork.utils.*;
import com.example.practicalwork.utils.document.DocIsDeletedException;
import com.example.practicalwork.utils.document.DocNotFoundException;
import com.example.practicalwork.utils.file.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/file")
@AllArgsConstructor
@Tag(name = "DocFileController", description = "API for files")
public class DocFileController {

    private final DocFileService docFileService;
    private final DocFileConverter docFileConverter;
    private final BindingResultHandler bindingResultHandler;
    private final DocumentService documentService;
    private final Distributor distributor;

    @GetMapping("/all")
    @Operation(summary = "Get files", description = "Get all current and removed files")
    public List<DocFileDTO> getAll() {
        List<DocFileDTO> dto = docFileService.findAll()
                .stream().map(docFileConverter::convertToDto)
                .toList();
        if (dto.isEmpty()) {
            throw new DocFileListIsEmptyException();
        }
        return dto;
    }

    @GetMapping("/current")
    @Operation(summary = "Get files", description = "Get current files only (removed = false)")
    public List<DocFileDTO> getCurrent() {
        List<DocFileDTO> dto = docFileService.findCurrent()
                .stream().map(docFileConverter::convertToDto)
                .toList();
        if (dto.isEmpty()) {
            throw new DocFileListIsEmptyException();
        }
        return dto;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get file", description = "Get one file by id")
    public DocFileDTO getById(@PathVariable("id") Long id) {

        // Exception set in DocFileService
        return docFileConverter.convertToDto(docFileService.read(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete file", description = "Delete one file by id")
    public ResponseEntity<HttpStatus> deleteById(@PathVariable("id") Long id) {

        // Exception set in DocFieldService
        docFileService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/recovery/{id}")
    @Operation(summary = "Recovery file", description = "Recovery one file by id")
    public ResponseEntity<HttpStatus> recovery(@PathVariable("id") Long id) {

        // Exception set in DocFileService
        docFileService.recovery(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    /*****
     * POST localhost:8080/api/file/from/doc/{id}
     * RequestBody requires JSON
     *  {
     *    "extension": "PDF", // DOCX, XLSX, PDF
     *    "zip": true,
     *    "description": "Description of file"
     *  }
     * Response OK = 200
     * Response BAD_REQUEST = 404
     *  {
     *    "message": "Invalid file extension. Allowed: DOCX, XLSX, PDF",
     *    "timestamp": "2023-04-06T13:25:30.788+00:00"
     *  }
     *  {
     *    "message": "Document not found",
     *    "timestamp": "2023-04-06T13:25:30.788+00:00"
     *  }
     *  {
     *    "message": "zip : ..., description : ...",
     *    "timestamp": "2023-04-06T13:25:30.788+00:00"
     *  }
     * */
    @PostMapping("/from/doc/{id}")
    @Operation(summary = "Create file", description = "Create new file using data in json")
    public ResponseEntity<FileUriResponse> create(@PathVariable("id") Long docId,
                                             @RequestBody @Valid DocFileDTO fileDto,
                                             BindingResult bindingResult) throws IOException {

        Document doc = documentService.read(docId);

        if (doc.isRemoved()) {
            throw new DocIsDeletedException();
        }

        if (bindingResult.hasErrors()) {
            String str = bindingResultHandler.createErrorMessage(bindingResult);
            throw new DocFileNotCreatedException(str);
        }

//        Extension format = Extension.findByValue(dto.getExtension());
//        if (format == null) {
//            throw new DocFileInvalidFormatOfFileException();
//        }

        DocFile docFile = docFileConverter.convertToEntity(fileDto);

        distributor.createFileOnDisk(doc, docFile);

        String uri = docFile.getStore() + docFile.getName();
        FileUriResponse fileUriResponse = new FileUriResponse(uri);
        return new ResponseEntity<>(fileUriResponse, HttpStatus.OK);

    }

    @PutMapping("/update")
    @Operation(summary = "Update file", description = "Update current file by id in json")
    public ResponseEntity<HttpStatus> update(@RequestBody DocFileDTO dto,
                                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String str = bindingResultHandler.createErrorMessage(bindingResult);
            throw new DocFileNotCreatedException(str);
        }
        docFileService.update(docFileConverter.convertToEntity(dto));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handlerException(DocFileListIsEmptyException e) {
        e.printStackTrace();
        ErrorResponse response = new ErrorResponse();
        response.setMessage("No files for this request");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handlerException(DocFileNotFoundException e) {
        e.printStackTrace();
        ErrorResponse response = new ErrorResponse();
        response.setMessage("File not found");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handlerException(DocFileNotDeletedException e) {
        e.printStackTrace();
        ErrorResponse response = new ErrorResponse();
        response.setMessage("File doesn't need in recovery");
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handlerException(DocFileNotCreatedException e) {
        e.printStackTrace();
        ErrorResponse response = new ErrorResponse();
        response.setMessage(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Duplicated from DocumentController
    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handlerException(DocNotFoundException e) {
        e.printStackTrace();
        ErrorResponse response = new ErrorResponse();
        response.setMessage("Document not found");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    // Duplicated from DocumentController
    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handlerException(DocIsDeletedException e) {
        e.printStackTrace();
        ErrorResponse response = new ErrorResponse();
        response.setMessage("Document is deleted");
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

//  //Use handler for String extension in DocFileDTO
//    @ExceptionHandler
//    public ResponseEntity<ErrorResponse> handlerException(DocFileInvalidExtensionOfFileException e) {
//        e.printStackTrace();
//        ErrorResponse response = new ErrorResponse();
//        response.setMessage("Invalid file extension. Allowed: docx, xlsx, pdf");
//        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handlerException(HttpMessageNotReadableException e) {
        e.printStackTrace();
        ErrorResponse response = new ErrorResponse();
        response.setMessage("Invalid file extension. Allowed: DOCX, XLSX, PDF");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handlerException(FileNotFoundException e) {
        e.printStackTrace();
        ErrorResponse response = new ErrorResponse();
        response.setMessage("File-on-disk not found");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
