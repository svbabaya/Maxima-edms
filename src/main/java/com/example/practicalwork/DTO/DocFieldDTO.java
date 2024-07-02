package com.example.practicalwork.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@Schema(description = "Information about field of document and template")
public class DocFieldDTO {
//    @Parameter(hidden = true)
    @Schema (description = "Id of field")
    private Long id;
    @Schema (description = "Name of field")
    @NotBlank
    @Size(min = 1, max = 50)
    private String name;
    @Schema (description = "Type of field")
    @NotBlank
    @Size(min = 1, max = 50)
    private String type;
    @Schema (description = "Placeholder of field")
    @NotBlank
    @Size(min = 1, max = 200)
    private String placeholder;
    @Schema (description = "Default value of field")
    @NotBlank
    @JsonProperty("default")
    @Size(min = 1, max = 2000)
    private String defaultValue;

}
