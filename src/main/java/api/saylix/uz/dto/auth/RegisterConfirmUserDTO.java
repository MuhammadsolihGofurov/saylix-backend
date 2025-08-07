package api.saylix.uz.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterConfirmUserDTO {
    @NotBlank(message = "Username is required")
    private String username;
    @NotBlank(message = "Code is required")
    private String code;
}
