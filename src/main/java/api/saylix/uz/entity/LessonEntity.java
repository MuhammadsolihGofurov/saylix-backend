package api.saylix.uz.entity;

import api.saylix.uz.enums.LessonType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "lesson")
public class LessonEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "title")
    private String title;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "type_of_lesson")
    @Enumerated(EnumType.STRING)
    private LessonType typeOfLesson;

    @Column(name = "finished")
    private Boolean finished = false;

    @Column(name = "score")
    private Integer score;

    @Column(name = "content")
    private String content;

    @Column(name = "additional_details", columnDefinition = "text")
    private String additionalDetails;

    @Column(name = "visible")
    private Boolean visible = true;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id")
    @JsonBackReference
    private SectionEntity section;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

}
