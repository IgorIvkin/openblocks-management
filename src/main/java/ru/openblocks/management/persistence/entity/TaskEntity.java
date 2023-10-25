package ru.openblocks.management.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ru.openblocks.management.model.task.TaskPriority;
import ru.openblocks.management.model.task.TaskStatus;
import ru.openblocks.management.model.task.TaskType;
import ru.openblocks.management.persistence.converter.TaskPriorityConverter;
import ru.openblocks.management.persistence.converter.TaskStatusConverter;
import ru.openblocks.management.persistence.converter.TaskTypeConverter;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "task")
public class TaskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "status")
    @Convert(converter = TaskStatusConverter.class)
    private TaskStatus status;

    @NotNull
    @Column(name = "task_type")
    @Convert(converter = TaskTypeConverter.class)
    private TaskType taskType;

    @NotNull
    @Column(name = "priority")
    @Convert(converter = TaskPriorityConverter.class)
    private TaskPriority priority;

    @Column(name = "code")
    private String code;

    @ManyToOne
    @JoinColumn(name = "project_code", referencedColumnName = "code")
    private ProjectEntity project;

    @Column(name = "subject")
    private String subject;

    @Column(name = "explanation")
    private String explanation;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private UserDataEntity owner;

    @ManyToOne
    @JoinColumn(name = "executor_id")
    private UserDataEntity executor;

    @Column(name = "estimation")
    private Integer estimation;

    @ManyToOne
    @JoinColumn(name = "sprint", referencedColumnName = "id")
    private SprintEntity sprint;
}
