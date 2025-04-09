package org.balikin.config;



import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Application;
import org.eclipse.microprofile.openapi.annotations.Components;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@ApplicationScoped
@OpenAPIDefinition(
        tags = {
                @Tag(name = "balikin", description = "Service Balikin")
        },
        components = @Components(
                securitySchemes = {
                        @SecurityScheme(
                                securitySchemeName = "bearerAuth",
                                type = SecuritySchemeType.HTTP,
                                scheme = "bearer",
                                bearerFormat = "JWT"
                        )
                }
        ),
        security = {
                @SecurityRequirement(name = "bearerAuth")
        },
        info = @Info(
                title = "Balikin Service",
                version = "V1.0.0",
                contact = @Contact(
                        name = "Balikin Contact Service",
                        url = "https://balikin.com/contact",
                        email = "radenmuhamadrifki340@gmail.com"
                ),
                license = @License(
                        name = "Balikin App",
                        url = "https://www.apache.org/licenses/LICENSE-2.0.html"
                )
        )
)
public class SwaggerConfig extends Application {
}