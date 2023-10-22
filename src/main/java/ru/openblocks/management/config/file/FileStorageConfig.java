package ru.openblocks.management.config.file;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "app.file-storage")
public class FileStorageConfig {

    private String type;

    private String path;

    private Set<String> allowedMimeTypes;
}
