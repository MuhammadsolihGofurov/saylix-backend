package api.saylix.uz.controller;

import api.saylix.uz.dto.auth.*;
import api.saylix.uz.dto.response.auth.LoginResponseDTO;
import api.saylix.uz.dto.utils.AppResponse;
import api.saylix.uz.enums.AppLanguage;
import api.saylix.uz.services.UsersService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UsersController {

    @Autowired
    private UsersService usersService;

    @PostMapping("/auth/registration")
    public ResponseEntity<AppResponse<String>> registration(@Valid @RequestBody RegisterUserDTO dto, @RequestHeader(value = "Accept-Language", defaultValue = "uz") AppLanguage language) {
        return ResponseEntity.ok().body(usersService.registrationUser(dto, language));
    }

    @PostMapping("/auth/registration/confirm")
    public ResponseEntity<AppResponse<String>> registrationConfirm(@Valid @RequestBody RegisterConfirmUserDTO dto, @RequestHeader(value = "Accept-Language", defaultValue = "uz") AppLanguage language) {
        return ResponseEntity.ok().body(usersService.registrationConfirm(dto, language));
    }

    @PostMapping("/auth/reset-password")
    public ResponseEntity<AppResponse<String>> resetPassword(@Valid @RequestBody ResetPasswordDTO dto, @RequestHeader(value = "Accept-Language", defaultValue = "uz") AppLanguage language) {
        return ResponseEntity.ok().body(usersService.resetPassword(dto, language));
    }

    @PostMapping("/auth/reset-password/confirm")
    public ResponseEntity<AppResponse<String>> resetPasswordConfirm(@Valid @RequestBody ResetPasswordConfirmDTO dto, @RequestHeader(value = "Accept-Language", defaultValue = "uz") AppLanguage language) {
        return ResponseEntity.ok().body(usersService.resetPasswordConfirm(dto, language));
    }


    @PostMapping("/auth/login")
    public ResponseEntity<AppResponse<LoginResponseDTO>> login(@Valid @RequestBody LoginUserDTO dto, @RequestHeader(value = "Accept-Language", defaultValue = "uz") AppLanguage language) {
        return ResponseEntity.ok().body(usersService.login(dto, language));
    }

    @GetMapping("/me")
    public ResponseEntity<AppResponse<LoginResponseDTO>> getMe(@RequestHeader(value = "Accept-Language", defaultValue = "uz") AppLanguage language) {
        return ResponseEntity.ok().body(usersService.getMe(language));
    }

}
