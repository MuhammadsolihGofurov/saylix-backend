package api.saylix.uz.dto.users.teacher;

import api.saylix.uz.enums.UsersRoles;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TeacherPublicResponseDTO {

    private String id;
    private String name;
    private String surname;
    private String bio;
    private String photoUrl;
    private String photoKey;
    private Integer experienceYears;

//    user
    private UsersRoles role;

    private Boolean visible;
    private LocalDateTime createdAt;

}
