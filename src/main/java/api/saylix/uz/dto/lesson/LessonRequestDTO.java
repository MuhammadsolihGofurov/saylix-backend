package api.saylix.uz.dto.lesson;

import api.saylix.uz.enums.LessonType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LessonRequestDTO {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Lesson type is required")
    private LessonType type;

    @NotNull(message = "Score is required")
    private Integer score;

    @NotBlank(message = "Content is required")
    private String content;

    @NotBlank(message = "Additional Details is required")
    private String additionalDetails;

    @NotBlank(message = "Section Id is required")
    private String sectionId;

}
