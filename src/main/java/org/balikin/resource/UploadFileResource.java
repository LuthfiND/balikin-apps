package org.balikin.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.balikin.dto.UploadFileDto;
import org.balikin.model.ApiResponse;
import org.balikin.service.impl.UploadFileServiceImpl;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.PartType;

import java.io.File;
import java.io.IOException;

@Path("upload")
@Consumes(MediaType.MULTIPART_FORM_DATA)
@Produces(MediaType.APPLICATION_JSON)
public class UploadFileResource {

    @Inject
    UploadFileServiceImpl uploadFileServiceImpl;

    @POST
    public ApiResponse<UploadFileDto> uploadFile(
            @RestForm("file") @PartType(MediaType.APPLICATION_OCTET_STREAM) File file
    ) {
        try {
            UploadFileDto response = uploadFileServiceImpl.uploadFile(file);
            return new ApiResponse<>("Success Upload", response, 200);
        } catch (IOException e) {
            return new ApiResponse<>("Gagal mengunggah file", null, 500);
        }
    }
}