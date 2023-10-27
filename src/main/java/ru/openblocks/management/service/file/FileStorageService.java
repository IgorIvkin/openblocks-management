package ru.openblocks.management.service.file;


import ru.openblocks.management.model.file.FileStorageType;

import java.io.InputStream;

/**
 * Basic interface to file storage services.
 */
public interface FileStorageService {

    FileStorageType getStorageType();

    boolean isAllowedMimeType(String mimeType);

    String store(InputStream fileStream);

    InputStream get(String fileName);

    void delete(String filePath);
}
