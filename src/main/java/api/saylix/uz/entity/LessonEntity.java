package api.saylix.uz.entity;

import api.saylix.uz.enums.LessonType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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

    @Column(name = "is_finished")
    private boolean isFinished;

    @Column(name = "score")
    private Integer score;

    @Column(name = "content")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id")
    private SectionEntity section;

}
