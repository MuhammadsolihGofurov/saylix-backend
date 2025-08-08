package api.saylix.uz.dto.users;

import lombok.Data;

@Data
public class UserUpdateUsernameConfirmDTO {
    private String newUsername;
    private String code;
}
