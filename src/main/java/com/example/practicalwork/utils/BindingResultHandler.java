package com.example.practicalwork.utils;

import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import java.util.List;

@Component
public class BindingResultHandler {
    public String createErrorMessage(BindingResult bindingResult) {
        StringBuilder bld = new StringBuilder();

//            bindingResult.getFieldErrors().forEach(error -> bld.append(error
//                    .getField()).append(" : ")
//                    .append(error.getDefaultMessage()));

        List<FieldError> errors = bindingResult.getFieldErrors();
        for (int i = 0; i < errors.size(); i++) {
            bld.append(errors.get(i).getField())
                    .append(" : ")
                    .append((errors.get(i).getDefaultMessage()));
            if (i != errors.size() - 1) {
                bld.append(", ");
            }
        }

        return bld.toString();
    }
}
