package org.balikin.config;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import com.cloudinary.Cloudinary;


@Setter
@Getter
@AllArgsConstructor
@ApplicationScoped
public class CloudinaryConfig {
    @Getter
    private final Cloudinary cloudinary;
    @Inject
    public CloudinaryConfig(ApplicationPropertiesConfig applicationPropertiesConfig) {
        this.cloudinary = new Cloudinary(applicationPropertiesConfig.getCloudinaryUrl());
    }
}