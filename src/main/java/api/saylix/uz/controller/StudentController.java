package api.saylix.uz.controller;

import api.saylix.uz.dto.users.student.StudentUpdateDetailsDTO;
import api.saylix.uz.dto.users.teacher.TeacherUpdateDetails;
import api.saylix.uz.dto.users.teacher.TeacherUpdatePhotoDTO;
import api.saylix.uz.dto.utils.AppResponse;
import api.saylix.uz.enums.AppLanguage;
import api.saylix.uz.services.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@PreAuthorize("hasRole('STUDENT')")
@RestController
@RequestMapping("/api/v1/student")
public class StudentController {

    @Autowired
    private StudentService studentService;


    @PutMapping("/update/details")
    public ResponseEntity<AppResponse<String>> updateDetails(@Valid @RequestBody StudentUpdateDetailsDTO dto, @RequestHeader(value = "Accept-Language", defaultValue = "uz") AppLanguage language) {
        return ResponseEntity.ok().body(studentService.updateDetails(dto, language));
    }

    @PutMapping("/update/photo")
    public ResponseEntity<AppResponse<String>> updatePhoto(@Valid @RequestBody TeacherUpdatePhotoDTO dto, @RequestHeader(value = "Accept-Language", defaultValue = "uz") AppLanguage language) throws IOException {
        return ResponseEntity.ok().body(studentService.updatePhoto(dto, language));
    }


}
