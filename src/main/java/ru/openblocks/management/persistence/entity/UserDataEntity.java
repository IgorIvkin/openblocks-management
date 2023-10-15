package ru.openblocks.management.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_data")
public class UserDataEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

    @Column(name = "created_at")
    private Instant createdAt;

    @BatchSize(size = 5)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserPositionEntity> roles = new ArrayList<>();

    /**
     * Adds an existing role to a current user.
     *
     * @param role existing role to append to user
     */
    public void addRole(UserRoleEntity role) {
        UserPositionEntity userPosition = new UserPositionEntity();
        userPosition.setUser(this);
        userPosition.setRole(role);
        roles.add(userPosition);
    }
}
