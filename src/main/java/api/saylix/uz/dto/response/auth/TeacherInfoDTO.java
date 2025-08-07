package api.saylix.uz.dto.response.auth;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TeacherInfoDTO {

    private String id;
    private String userId;
    private String name;
    private String surname;
    private String bio;
    private String experience_years;
    private Boolean visible;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
