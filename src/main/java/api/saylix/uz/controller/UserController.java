package api.saylix.uz.controller;

import api.saylix.uz.dto.auth.RegisterUserDTO;
import api.saylix.uz.enums.AppLanguage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {


    @PostMapping("/registration")
    public ResponseEntity<String> registration(@RequestBody RegisterUserDTO dto, @RequestHeader(value = "Accept-Language", defaultValue = "uz") AppLanguage language) {
        return ResponseEntity.ok().body("success");
    }

}
