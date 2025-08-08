package api.saylix.uz.dto.subject;


import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SubjectRequestDTO {
    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "PhotoUrl is required")
    private String photoUrl;

    @NotBlank(message = "PhotoKey is required")
    private String photoKey;

}
