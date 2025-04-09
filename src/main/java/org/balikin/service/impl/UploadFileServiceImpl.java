package org.balikin.service.impl;

import com.cloudinary.Cloudinary;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.balikin.config.CloudinaryConfig;
import org.balikin.dto.UploadFileDto;
import org.balikin.service.uploadFile.UploadFileService;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

@Slf4j
@ApplicationScoped
public class UploadFileServiceImpl implements UploadFileService {

    @Inject
    CloudinaryConfig cloudinaryConfig;

    @Override
    public UploadFileDto uploadFile(File file) throws IOException {
        Cloudinary cloudinary = cloudinaryConfig.getCloudinary();

        Map uploadResult = cloudinary.uploader().upload(file, Collections.emptyMap());
        String imageUrl = uploadResult.get("secure_url").toString();
        UploadFileDto uploadFileDto = new UploadFileDto();
        uploadFileDto.setUrlImage(imageUrl);
        return uploadFileDto;
    }
}
