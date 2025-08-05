package api.saylix.uz.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "admin")
@Getter
@Setter
public class AdminEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @OneToOne
//    @MapsId
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "position")
    private String position;

    @Column(name = "visible")
    private Boolean visible = true;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

}
