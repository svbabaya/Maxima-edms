package com.example.practicalwork.controllers;

import com.example.practicalwork.DTO.DocTemplateDTO;
import com.example.practicalwork.utils.BindingResultHandler;
import com.example.practicalwork.utils.ErrorResponse;
import com.example.practicalwork.utils.template.DocTemplateIncorrectFileExtensionException;
import com.example.practicalwork.utils.template.DocTemplateNotCreatedException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/upload")
@AllArgsConstructor
@Tag(name = "FileUploadController", description = "API for upload files and create template")
public class FileUploadController {

//    private BindingResultHandler bindingResultHandler;
//    @PostMapping("/{ext}")
//    @Operation(summary = "Create template from .docx/.xlsx", description = "Create new template using data in json")
//    public ResponseEntity<FileUploadResponce> createFromFile(@RequestBody @Valid DocTemplateDTO dto,
//                                                     @PathVariable("ext") String ext,
//                                                     @RequestParam MultipartFile file,
//                                                     BindingResult bindingResult) {
//
//        if (!ext.equals("docx") && !ext.equals("xlsx")) {
//            throw new DocTemplateIncorrectFileExtensionException();
//        }
//
//        if (bindingResult.hasErrors()) {
//            String str = bindingResultHandler.createErrorMessage(bindingResult);
//            throw new DocTemplateNotCreatedException(str);
//        }
//
//        Attachment attachment =
//
//                docTemplateService.write(docTemplateConverter.convertToEntity(dto));
//        return ResponseEntity.ok(HttpStatus.OK);
//
//    }
//
//    @ExceptionHandler
//    public ResponseEntity<ErrorResponse> handlerException(DocTemplateIncorrectFileExtensionException e) {
//        e.printStackTrace();
//        ErrorResponse response = new ErrorResponse();
//        response.setMessage("Invalid file extension. Allowed: docx, xlsx");
//        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//    }

}
