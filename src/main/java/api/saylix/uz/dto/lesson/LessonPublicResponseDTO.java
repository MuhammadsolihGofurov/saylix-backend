package api.saylix.uz.dto.lesson;


import api.saylix.uz.enums.LessonType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LessonPublicResponseDTO {

    private String id;
    private String title;
    private String description;
    private LessonType lessonType;
    private Integer score;
    private String content;
    private String additionalDetails;
    private Boolean visible;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isFinished;

}
