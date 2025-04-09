package org.balikin.config;


import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class ApplicationPropertiesConfig {
    @ConfigProperty(name = "CLOUDINARY_URL")
    String cloudinaryUrl;
    public String getCloudinaryUrl() {
        return cloudinaryUrl;
    }
}
