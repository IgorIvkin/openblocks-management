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
@Table(name = "user_position")
public class UserPositionEntity {

    @EmbeddedId
    private UserPositionId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    private UserDataEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("roleId")
    private UserRoleEntity role;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "rmv")
    private Boolean rmv;
}
