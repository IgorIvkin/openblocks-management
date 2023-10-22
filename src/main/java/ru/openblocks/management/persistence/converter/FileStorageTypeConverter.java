package ru.openblocks.management.persistence.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import ru.openblocks.management.model.file.FileStorageType;

@Converter(autoApply = true)
public class FileStorageTypeConverter implements AttributeConverter<FileStorageType, Long> {

    @Override
    public Long convertToDatabaseColumn(FileStorageType fileStorageType) {
        if (fileStorageType == null) {
            return null;
        }
        return fileStorageType.asLong();
    }

    @Override
    public FileStorageType convertToEntityAttribute(Long fileStorageTypeId) {
        if (fileStorageTypeId == null) {
            return null;
        }
        return FileStorageType.of(fileStorageTypeId);
    }
}
