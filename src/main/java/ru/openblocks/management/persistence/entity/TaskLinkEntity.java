package ru.openblocks.management.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.openblocks.management.model.task.TaskLinkType;
import ru.openblocks.management.persistence.converter.TaskLinkTypeConverter;

import java.time.Instant;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "task_link")
public class TaskLinkEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "link_type")
    @Convert(converter = TaskLinkTypeConverter.class)
    private TaskLinkType linkType;

    @ManyToOne
    @JoinColumn(name = "task_code", referencedColumnName = "code")
    private TaskEntity task;

    @ManyToOne
    @JoinColumn(name = "connected_task_code", referencedColumnName = "code")
    private TaskEntity connectedTask;

    @Column(name = "created_at")
    private Instant createdAt;
}
