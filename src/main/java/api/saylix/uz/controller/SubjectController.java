package api.saylix.uz.controller;

import api.saylix.uz.dto.users.teacher.TeacherUpdatePhotoDTO;
import api.saylix.uz.dto.utils.AppResponse;
import api.saylix.uz.enums.AppLanguage;
import api.saylix.uz.services.SubjectService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/subject")
public class SubjectController {

    @Autowired
    private SubjectService subjectService;


    @PostMapping("/update/photo")
    public ResponseEntity<AppResponse<String>> createSubject(@Valid @RequestBody TeacherUpdatePhotoDTO dto, @RequestHeader(value = "Accept-Language", defaultValue = "uz") AppLanguage language) throws IOException {
        return ResponseEntity.ok().body(subjectService.createSubject(dto, language));
    }

}
