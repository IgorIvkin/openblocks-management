package ru.openblocks.management.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.openblocks.management.model.task.TaskHistoryChangeObject;
import ru.openblocks.management.persistence.converter.TaskHistoryChangeObjectConverter;

import java.time.Instant;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "task_history")
public class TaskHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at")
    private Instant createdAt;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private UserDataEntity author;

    @ManyToOne
    @JoinColumn(name = "task_code", referencedColumnName = "code")
    private TaskEntity task;

    @Column(name = "change_object")
    @Convert(converter = TaskHistoryChangeObjectConverter.class)
    private TaskHistoryChangeObject changeObject;

    @Column(name = "previous_value")
    private String previousValue;

    @Column(name = "new_value")
    private String newValue;
}
