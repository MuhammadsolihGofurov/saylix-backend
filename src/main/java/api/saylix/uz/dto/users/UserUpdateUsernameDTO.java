package api.saylix.uz.dto.users;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserUpdateUsernameDTO {
    @NotBlank(message = "New Username is required")
    private String newUsername;
}
