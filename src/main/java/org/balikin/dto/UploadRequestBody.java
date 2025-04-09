package org.balikin.dto;

import org.jboss.resteasy.reactive.PartType;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.core.MediaType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UploadRequestBody {

    @FormParam("file")
    @PartType(MediaType.APPLICATION_OCTET_STREAM) // Pastikan diterima sebagai file
    private byte[] file;

    @FormParam("filePath")
    @PartType(MediaType.TEXT_PLAIN) // Diterima sebagai teks biasa
    private String filePath;
}
