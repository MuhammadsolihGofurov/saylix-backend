package api.saylix.uz.dto.users.teacher;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TeacherUpdateDetails {
    @NotBlank(message = "Name is required")
    private String name;
    @NotBlank(message = "Surname is required")
    private String surname;
    @NotBlank(message = "Bio is required")
    private String bio;
    @NotNull(message = "Experience years is required")
    private Integer experienceYears;

}
