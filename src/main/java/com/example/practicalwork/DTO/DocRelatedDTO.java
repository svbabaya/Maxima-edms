package com.example.practicalwork.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@Schema(description = "Information about related documents")
public class DocRelatedDTO {
    @Schema (description = "ID's of related documents")
    @JsonProperty("document_id")
    private Long relatedId;
}
