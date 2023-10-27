package ru.openblocks.management.service.file;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import ru.openblocks.management.config.file.FileStorageConfig;
import ru.openblocks.management.model.file.FileStorageType;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.time.Clock;
import java.time.LocalDate;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "app.file-storage", name = "type", havingValue = "FILESYSTEM")
public class FileSystemFileStorage implements FileStorageService {

    private static final String FILE_NAME = "{0}/{1}";

    private static final String DIRECTORY_NAME = "{0}/{1,number,#}";

    private final FileStorageConfig fileStorageConfig;

    private Clock clock = Clock.systemDefaultZone();

    @PostConstruct
    public void initialize() {
        try {

            final String path = fileStorageConfig.getPath();
            final Path fileStoragePath = Paths.get(path);

            if (!Files.exists(fileStoragePath)) {
                log.warn("There is no file path {}, will try to use classpath then", path);
                final Path relativePath = Paths.get(ClassLoader.getSystemResource(path).toURI());
                if (!Files.exists(relativePath)) {
                    throw new IllegalStateException("Cannot file the file path for file storage: " + path);
                }
            }

            log.info("File storage configuration is initialized");

        } catch (URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Sets the clock for file system storage service.
     *
     * @param clock clock
     */
    public void setClock(Clock clock) {
        this.clock = clock;
    }

    /**
     * Returns a type of storage.
     *
     * @return type of storage
     */
    @Override
    public FileStorageType getStorageType() {
        return FileStorageType.FILESYSTEM;
    }

    /**
     * Checks whether a provided mime-type of file is allowed or not.
     *
     * @param mimeType mime-type of file
     * @return true, if mime-type is allowed
     */
    @Override
    public boolean isAllowedMimeType(String mimeType) {
        if (fileStorageConfig.getAllowedMimeTypes() != null) {
            return fileStorageConfig.getAllowedMimeTypes().contains(mimeType);
        }
        return false;
    }

    /**
     * Stores a file to file system from input stream.
     *
     * @param fileStream input stream of file
     * @return file path of new file
     */
    @Override
    public String store(InputStream fileStream) {

        log.info("Started to store the picture");

        final String newPictureFileName = generateNewPictureFileName();
        final File targetFile = new File(newPictureFileName);
        try {
            FileUtils.copyInputStreamToFile(fileStream, targetFile);
        } catch (IOException ex) {
            throw new IllegalStateException("Cannot store file, reason: " + ex.getMessage());
        }

        log.info("New picture is successfully stored - {}", newPictureFileName);
        return newPictureFileName.replace(fileStorageConfig.getPath(), "");
    }

    /**
     * Returns the content of file from storage by id of file.
     *
     * @param fileName id of file
     * @return input stream with content of file
     */
    @Override
    public InputStream get(String fileName) {
        if (!Files.exists(Paths.get(fileStorageConfig.getPath() + fileName))) {
            throw new IllegalStateException("File " + fileName + " does not exist");
        }
        final File file = new File(fileStorageConfig.getPath() + fileName);
        try {
            return FileUtils.openInputStream(file);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }


    /**
     * Checks that the file exists by its given path.
     *
     * @param filePath file path to check
     * @return true, if file exists
     */
    public boolean fileExists(String filePath) {
        return Files.exists(Paths.get(filePath));
    }

    /**
     * Deletes file.
     *
     * @param filePath file path
     */
    @Override
    public void delete(String filePath) {
        if (filePath != null) {

            final Path path = Paths.get(fileStorageConfig.getPath() + filePath);

            if (Files.exists(path)) {
                try {
                    Files.delete(path);
                    log.info("File {} was successfully deleted", filePath);
                } catch (IOException ex) {
                    throw new IllegalStateException(
                            MessageFormat.format("Cannot delete file {0}, reason: {1}", filePath, ex.getMessage())
                    );
                }
            } else {
                log.warn("Will not delete {} picture because it does not exist", filePath);
            }
        }
    }

    private String generateNewPictureFileName() {
        return MessageFormat.format(
                FILE_NAME,
                generateDailyDirectory(),
                UUID.randomUUID()
        );
    }

    private String generateDailyDirectory() {
        final LocalDate now = LocalDate.now(clock);

        // Create "year" directory if it doesn't exist
        final String yearDirectory = MessageFormat.format(DIRECTORY_NAME, fileStorageConfig.getPath(), now.getYear());
        createDirectoryIfNotExists(yearDirectory);

        // Create "month" directory if it doesn't exist
        final String monthDirectory = MessageFormat.format(DIRECTORY_NAME, yearDirectory, now.getMonth().getValue());
        createDirectoryIfNotExists(monthDirectory);

        // Create "day" directory if it doesn't exist
        final String dailyDirectory = MessageFormat.format(DIRECTORY_NAME, monthDirectory, now.getDayOfMonth());
        createDirectoryIfNotExists(dailyDirectory);

        return dailyDirectory;
    }

    private void createDirectoryIfNotExists(String directoryName) {
        try {
            final Path path = Paths.get(directoryName);
            if (!Files.exists(path)) {
                log.info("No directory {}, going to create now", directoryName);
                Files.createDirectory(path);
            }
        } catch (IOException ex) {
            throw new IllegalStateException(
                    "Cannot save picture, cannot create daily directory, reason: " + ex.getMessage()
            );
        }
    }
}
