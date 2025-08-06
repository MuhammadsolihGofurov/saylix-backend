package api.saylix.uz.entity;

import api.saylix.uz.enums.TeacherStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "teacher")
@Getter
@Setter
public class TeacherEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @OneToOne
//    @MapsId
    @JoinColumn(name = "user_id")
    private UsersEntity user;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "bio")
    private String bio;

    @Column(name = "experience_years")
    private Integer experience_years;

    @Column(name = "visible")
    private Boolean visible = true;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private TeacherStatus status;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

}

