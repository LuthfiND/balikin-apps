package org.balikin.service.uploadFile;

import org.balikin.dto.UploadFileDto;
import org.balikin.dto.UploadRequestBody;

import java.io.File;
import java.io.IOException;

public interface UploadFileService {
    public UploadFileDto uploadFile(File file) throws IOException;
}
