package com.example.practicalwork.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "Document Management System Api",
                description = "Document Management System", version = "1.0.0",
                contact = @Contact(
                        name = "Maxima",
                        email = "pr@mxm.email",
                        url = "https://maxima.life"
                )
        )
)
public class OpenApiConfig {

}
