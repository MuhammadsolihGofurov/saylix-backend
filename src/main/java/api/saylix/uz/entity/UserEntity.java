package api.saylix.uz.entity;

import api.saylix.uz.enums.UserRoles;
import api.saylix.uz.enums.UserStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "user")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "user_role")
    @Enumerated(EnumType.STRING)
    private UserRoles userRole;

    @Column(name = "visible")
    private Boolean visible = true;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

}
