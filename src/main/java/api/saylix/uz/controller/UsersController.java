package api.saylix.uz.controller;

import api.saylix.uz.dto.auth.RegisterUserDTO;
import api.saylix.uz.dto.utils.AppResponse;
import api.saylix.uz.enums.AppLanguage;
import api.saylix.uz.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UsersController {

    @Autowired
    private UsersService usersService;

    @PostMapping("/registration")
    public ResponseEntity<AppResponse<String>> registration(@RequestBody RegisterUserDTO dto, @RequestHeader(value = "Accept-Language", defaultValue = "uz") AppLanguage language) {
        AppResponse<String> response = usersService.registrationUser(dto, language);
        return ResponseEntity.ok().body(response);
    }

}
