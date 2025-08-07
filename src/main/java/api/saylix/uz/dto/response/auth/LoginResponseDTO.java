package api.saylix.uz.dto.response.auth;

import api.saylix.uz.enums.UsersRoles;
import api.saylix.uz.enums.UsersStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LoginResponseDTO {

    private String id;
    private String username;
    private UsersStatus status;
    private UsersRoles role;
    private Boolean visible;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;

    private StudentInfoDTO student;
    private TeacherInfoDTO teacher;


    private String token;


}
