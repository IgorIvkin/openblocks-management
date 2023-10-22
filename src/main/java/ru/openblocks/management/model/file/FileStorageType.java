package ru.openblocks.management.model.file;

import java.util.stream.Stream;


public enum FileStorageType {

    NONE(1L),

    FILESYSTEM(2L);

    private final Long type;

    FileStorageType(Long type) {
        this.type = type;
    }

    public Long asLong() {
        return type;
    }

    public static FileStorageType of(Long storageType) {
        return Stream.of(FileStorageType.values())
                .filter(t -> t.asLong().equals(storageType))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
