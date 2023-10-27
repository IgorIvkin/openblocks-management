package ru.openblocks.management.service.file;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import ru.openblocks.management.model.file.FileStorageType;

import java.io.InputStream;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "app.file-storage", name = "type", havingValue = "NONE")
public class NoneFileStorage implements FileStorageService {

    @Override
    public FileStorageType getStorageType() {
        return FileStorageType.NONE;
    }

    @Override
    public boolean isAllowedMimeType(String mimeType) {
        return true;
    }

    @Override
    public String store(InputStream fileStream) {
        log.warn("NONE file storage will NOT actually store the file!");
        return null;
    }

    @Override
    public InputStream get(String id) {
        log.warn("NONE file store will NOT actually return get file!");
        return null;
    }

    @Override
    public void delete(String filePath) {
        log.warn("NONE file storage will NOT actually delete the file!");
    }
}
