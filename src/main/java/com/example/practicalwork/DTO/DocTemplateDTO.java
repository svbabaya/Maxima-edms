package com.example.practicalwork.DTO;

import com.example.practicalwork.models.DocTitle;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import java.util.List;

@Setter
@Getter
@Component
@Schema (description = "Information about document's template")
public class DocTemplateDTO {
//    @Parameter (hidden = true)
    @Schema (description = "Id of template")
    private Long id;
    @Schema (description = "Title of template")
    @NotBlank
    @Size (min = 1, max = 200)
    private String title;
    @Schema (description = "Version of template")
    @NotBlank
    @Size (min = 1, max = 50)
    private String version;
    @Schema (description = "Type of template")
//    @Enumerated (EnumType.STRING)
    @JsonProperty("type")
    @NotBlank
    private String docTitle;
    @Schema (description = "Fields of template")
    private List<DocFieldDTO> fields;

}
