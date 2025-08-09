package api.saylix.uz.controller;

import api.saylix.uz.dto.subject.SubjectPublicResponseDTO;
import api.saylix.uz.dto.subject.SubjectRequestDTO;
import api.saylix.uz.dto.users.teacher.TeacherUpdatePhotoDTO;
import api.saylix.uz.dto.utils.AppResponse;
import api.saylix.uz.entity.SubjectEntity;
import api.saylix.uz.enums.AppLanguage;
import api.saylix.uz.services.SubjectService;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/subject")
public class SubjectController {

    @Autowired
    private SubjectService subjectService;


    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping("/create")
    public ResponseEntity<AppResponse> createSubject(@Valid @RequestBody SubjectRequestDTO dto, @RequestHeader(value = "Accept-Language", defaultValue = "uz") AppLanguage language) {
        return ResponseEntity.ok().body(subjectService.createSubject(dto, language));
    }

    @PreAuthorize("hasRole('TEACHER')")
    @PutMapping("/update/{id}")
    public ResponseEntity<AppResponse> updateSubject(@Valid @RequestBody SubjectRequestDTO dto, @RequestHeader(value = "Accept-Language", defaultValue = "uz") AppLanguage language, @PathVariable String id) {
        return ResponseEntity.ok().body(subjectService.updateSubject(id, dto, language));
    }

    @PreAuthorize("hasRole('TEACHER')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<AppResponse> deleteSubject(@RequestHeader(value = "Accept-Language", defaultValue = "uz") AppLanguage language, @PathVariable String id) {
        return ResponseEntity.ok().body(subjectService.deleteSubject(id, language));
    }

    @GetMapping
    public ResponseEntity<Page> getAllSubjects(
            @PageableDefault(size = 10) Pageable pageable,
            @RequestHeader(value = "Accept-Language", defaultValue = "uz") AppLanguage language) {
        return ResponseEntity.ok(subjectService.getAllSubjects(pageable, language));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppResponse<SubjectPublicResponseDTO>> getSubjectById(
            @PathVariable String id,
            @RequestHeader(value = "Accept-Language", defaultValue = "uz") AppLanguage language) {
        return ResponseEntity.ok(subjectService.getSubjectById(id, language));
    }

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/{subjectId}/enroll")
    public ResponseEntity<AppResponse<?>> enrollToSubject(
            @PathVariable String subjectId,
            @RequestHeader(value = "Accept-Language", defaultValue = "uz") AppLanguage language
    ) {
        return ResponseEntity.ok(subjectService.enrollToSubject(subjectId, language));
    }

}
