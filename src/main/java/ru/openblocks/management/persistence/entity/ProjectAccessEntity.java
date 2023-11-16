package ru.openblocks.management.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "project_access")
public class ProjectAccessEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "project_code")
    private String projectCode;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "project_admin")
    private Boolean projectAdmin;
}
