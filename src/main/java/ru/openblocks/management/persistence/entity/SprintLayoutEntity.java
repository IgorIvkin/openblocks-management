package ru.openblocks.management.persistence.entity;

import io.hypersistence.utils.hibernate.type.array.ListArrayType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sprint_layout")
public class SprintLayoutEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "project_code", referencedColumnName = "code")
    private ProjectEntity project;

    @ManyToOne
    @JoinColumn(name = "sprint_id", referencedColumnName = "id")
    private SprintEntity sprint;

    @Type(ListArrayType.class)
    @Column(name = "sprint_layout", columnDefinition = "varchar[]")
    private List<String> sprintLayout;
}
