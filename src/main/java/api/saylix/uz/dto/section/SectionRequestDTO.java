package api.saylix.uz.dto.section;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SectionRequestDTO {
    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Subject ID is required")
    private String subjectId;
}
