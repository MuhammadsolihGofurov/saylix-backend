package api.saylix.uz.dto;


import api.saylix.uz.enums.UsersRoles;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtResponseDTO {
    private String id;
    private String username;
    private UsersRoles role;

}
