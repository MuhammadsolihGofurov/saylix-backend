package api.saylix.uz.dto.users.student;

import api.saylix.uz.enums.UsersRoles;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StudentPublicResponseDTO {

    private String id;
    private String name;
    private String surname;
    private Integer age;
    private String photoUrl;
    private String photoKey;
    //    user
    private UsersRoles role;
    private Boolean visible;
    private LocalDateTime createdAt;

}


