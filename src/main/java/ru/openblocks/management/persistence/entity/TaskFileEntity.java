package ru.openblocks.management.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ru.openblocks.management.model.file.FileStorageType;
import ru.openblocks.management.persistence.converter.FileStorageTypeConverter;

import java.time.Instant;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "task_file")
public class TaskFileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "file_storage_type")
    @Convert(converter = FileStorageTypeConverter.class)
    private FileStorageType fileStorageType;

    @ManyToOne
    @JoinColumn(name = "task_code", referencedColumnName = "code")
    private TaskEntity task;

    @Column(name = "created_at")
    private Instant createdAt;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private UserDataEntity owner;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "mime_type")
    private String mimeType;

    @Column(name = "file_name")
    private String fileName;
}
