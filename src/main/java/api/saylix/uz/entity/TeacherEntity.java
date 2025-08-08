package api.saylix.uz.entity;

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

    @Column(name = "bio", columnDefinition = "text")
    private String bio;

    @Column(name = "photo_url")
    private String photoUrl;

    @Column(name = "photo_key")
    private String photoKey;

    @Column(name = "experience_years")
    private Integer experienceYears;

    @Column(name = "visible")
    private Boolean visible = true;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

}

