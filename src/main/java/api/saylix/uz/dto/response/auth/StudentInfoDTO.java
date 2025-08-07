package api.saylix.uz.dto.response.auth;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StudentInfoDTO {

    private String id;
    private String userId;
    private String name;
    private String surname;
    private Integer age;
    private Boolean visible;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
